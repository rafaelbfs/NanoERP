package systems.terranatal.nanoerp.accounting.datasource

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object AccountCharts: IntIdTable("account_charts") {
  val name = varchar("name", 255)
  val countryCode = varchar("country_code", 3)
}

object AccountDefinitions: IntIdTable("ACCOUNT_DEFINITIONS") {
  val code: Column<Short> = short("code")
  val chartId: Column<Int> = integer("chart_id").references(AccountCharts.id)
  val name: Column<String> = varchar("name", 255)
  val accountClass: Column<String> = varchar("account_class", 23)

  init {
    uniqueIndex("AD_UN_CODE_CHART_ID", code, chartId)
  }
}
