package systems.terranatal.nanoerp.spreadsheets.bas

import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.Alert
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import kotlinx.coroutines.runBlocking
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import systems.terranatal.nanoerp.accounting.spreadsheet.BasAccount
import systems.terranatal.nanoerp.accounting.spreadsheet.BasAccount.BasAccountClass
import systems.terranatal.nanoerp.accounting.spreadsheet.BasAccountXlsx2022Processor
import systems.terranatal.nanoerp.spreadsheets.bas.proto.BasServiceChannel
import systems.terranatal.omnijfx.kfx.properties.FxProperty
import systems.terranatal.omnijfx.kfx.treeviews.addColumn
import systems.terranatal.omnijfx.kfx.treeviews.column
import systems.terranatal.omnijfx.kfx.treeviews.valueFactoryFrom
import java.io.InputStream

class BasAccountTreeTable(): TreeTableView<BasAccountTreeTable.BasAccountRecord>(
  TreeItem(BasAccountRecord(-1, "BAS Account Chart 2022"))
) {

  constructor(basAccounts: List<BasAccount>): this() {
    root.children.addAll(convert(basAccounts))
    column("Code") {
      valueFactoryFrom(BasAccountRecord::codeProp)
      prefWidth = 280.0
    }
    column<BasAccountRecord, String?>("Name") {
      valueFactoryFrom(BasAccountRecord::nameProp)
      minWidth = 300.0
    }
    addColumn("Class", BasAccountRecord::classProp)
    selectionModel.selectedItemProperty().addListener(selectionListener)

    isShowRoot = false
  }

  class BasAccountRecord(val codeProp: FxProperty<Int?>, val nameProp: FxProperty<String?>,
    val classProp: FxProperty<String?>) {
    constructor(code: Short, name: String, accountClass: BasAccountClass = BasAccountClass.ACCOUNT_CHART): this(
      FxProperty(code.toInt()),
      FxProperty(name), FxProperty(accountClass.name)
    )

    constructor(basAccount: BasAccount): this(basAccount.code,
      basAccount.description, basAccount.accountClass)

    fun asTreeItem() = TreeItem(this)

    var name: String? by nameProp
    var code: Int? by codeProp
    var accountClass: String? by classProp
  }

  private fun convert(basAccounts: List<BasAccount>): List<TreeItem<BasAccountTreeTable.BasAccountRecord>> {
    val items = mutableListOf<TreeItem<BasAccountTreeTable.BasAccountRecord>>()
    for (basAccount in basAccounts) {
      val item = BasAccountRecord(basAccount).asTreeItem()
      if (basAccount.children.isNotEmpty()) {
        item.children.addAll(convert(basAccount.children))
      }
      items.add(item)
    }
    return items
  }

  companion object {
    fun buildTable(filePath: String): BasAccountTreeTable = try {
      val ist: InputStream = this::class.java.getClassLoader().getResourceAsStream(filePath)!!
      XSSFWorkbook(ist).use {
        val processor = BasAccountXlsx2022Processor(it.getSheetAt(0))
        val result: List<BasAccount> = processor.process()
        return BasAccountTreeTable(result)
      }
    } catch (e: Exception) {
      throw e
    }

    val selectionListener = object : ChangeListener<TreeItem<BasAccountRecord>> {
      override fun changed(
        observable: ObservableValue<out TreeItem<BasAccountRecord>>?,
        oldValue: TreeItem<BasAccountRecord>?,
        newValue: TreeItem<BasAccountRecord>?
      ) {
        if (newValue != null && newValue.children.isEmpty()) {
          val alert = Alert(Alert.AlertType.INFORMATION)
          alert.title = "Account"
          val account = newValue.value

          val result = runBlocking {
            BasServiceChannel.client.addAccount(account)
          }
          alert.contentText = "Server returned ${result.status}: ${result.description}"
          println(alert.contentText)
          alert.showAndWait()
        }
      }
    }
  }
}