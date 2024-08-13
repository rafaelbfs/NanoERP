package systems.terranatal.nanoerp.accounting.datasource

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import systems.terranatal.nanoerp.accounting.datasource.AccountDefinition.Companion.backReferencedOn

class AccountChart(id: EntityID<Int>) : IntEntity(id) {
  companion object: IntEntityClass<AccountChart>(AccountCharts)

  var name by AccountCharts.name
  var countryCode by AccountCharts.countryCode
}

class AccountDefinition(id: EntityID<Int>): IntEntity(id) {
  companion object : IntEntityClass<AccountDefinition>(AccountDefinitions)

  var code by AccountDefinitions.code
  var name by AccountDefinitions.name
  var chart by AccountChart referencedOn AccountDefinitions.chartId
}
