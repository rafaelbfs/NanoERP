package systems.terranatal.nanoerp.accounting.datasource

import jakarta.annotation.PostConstruct
import jakarta.annotation.Resource
import jakarta.ejb.Singleton
import jakarta.ejb.Startup
import jakarta.ejb.TransactionManagement
import jakarta.ejb.TransactionManagementType
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

@Startup
@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
open class PostgreSQL {
  @Resource(lookup = "java:/DevPostgresDS") private lateinit var datasource: DataSource

  private val database by lazy {
    Database.connect(datasource=datasource,
    databaseConfig= DatabaseConfig {
      defaultSchema = Schema("ACCOUNTING")
    })
  }

  @PostConstruct open fun initialize() {
    transaction(database) {
      addLogger(StdOutSqlLogger)
      SchemaUtils.create(AccountCharts, AccountDefinitions)
    }
  }
}