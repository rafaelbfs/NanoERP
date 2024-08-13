package systems.terranatal.nanoerp.accounting

import io.grpc.BindableService
import io.grpc.servlet.jakarta.ServletAdapter
import io.grpc.servlet.jakarta.ServletServerBuilder
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import java.io.IOException


class GrpcServletInterface(val servletAdapter: ServletAdapter): HttpServlet() {

  constructor(bindableServices: List<BindableService>): this(loadServices(bindableServices))

  @Throws(IOException::class)
  override fun doGet(request: HttpServletRequest?, response: HttpServletResponse?) {
    servletAdapter.doGet(request, response)
  }

  @Throws(IOException::class)
  override fun doPost(request: HttpServletRequest?, response: HttpServletResponse?) {
    servletAdapter!!.doPost(request, response)
  }

  override fun destroy() {
    servletAdapter!!.destroy()
    super.destroy()
  }

  companion object {
    private fun loadServices(bindableServices: List<BindableService>): ServletAdapter {
      val serverBuilder = ServletServerBuilder()
      bindableServices.forEach { bindableService: BindableService? -> serverBuilder.addService(bindableService) }
      return serverBuilder.buildServletAdapter()
    }
  }
}