package systems.terranatal.nanoerp.spreadsheets.bas

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.stage.Stage
import systems.terranatal.omnijfx.kfx.extensions.child

class AccountChartsApp : Application() {
  override fun start(primaryStage: Stage) {
    primaryStage.title = "BAS Accounts"
    primaryStage.scene = makeMainScene()
    primaryStage.show()
  }

  companion object {
    val fileName = "Chart-of-accounts-2022.xlsx"

    fun makeMainScene(): Scene {
      val tbl = BasAccountTreeTable.buildTable(fileName)
      val layout = HBox().apply {
        isFillHeight = true
        child(tbl)
        HBox.setHgrow(tbl, Priority.ALWAYS)
      }

      return Scene(layout)
    }

    @JvmStatic
    fun main(args: Array<String>) {
      launch(AccountChartsApp::class.java)
    }
  }
}
