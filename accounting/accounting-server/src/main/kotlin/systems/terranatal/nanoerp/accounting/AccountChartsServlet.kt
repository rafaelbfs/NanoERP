package systems.terranatal.nanoerp.accounting
import io.grpc.servlet.jakarta.GrpcServlet
import jakarta.ejb.EJB
import jakarta.inject.Inject
import jakarta.servlet.annotation.WebServlet
import systems.terranatal.nanoerp.accounting.datasource.PostgreSQL

@WebServlet(name = "accountChartsServlet", urlPatterns = ["/accountCharts.Bas/AddAccount"],
  asyncSupported = true)
class AccountChartsServlet @Inject constructor(@EJB private val dao: PostgreSQL):
  GrpcServlet(listOf(BasChartService())) {

  class BasChartService: BasGrpcKt.BasCoroutineImplBase() {
    override suspend fun addAccount(request: AccountDefinition) = result {
      status = "Success"
      description = "Account ${request.description} successfully."
    }
  }
}