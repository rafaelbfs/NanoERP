package systems.terranatal.nanoerp.server

import io.ktor.server.engine.*

class GRpcApplicationEngine(environment: ApplicationEngineEnvironment, configuration: GrpcApplicationConfiguration.() -> Unit) : BaseApplicationEngine(environment) {


  override fun start(wait: Boolean): ApplicationEngine {
    TODO("Not yet implemented")
  }

  override fun stop(gracePeriodMillis: Long, timeoutMillis: Long) {
    TODO("Not yet implemented")
  }

  companion object: ApplicationEngineFactory<GRpcApplicationEngine, GrpcApplicationConfiguration> {
    override fun create(
      environment: ApplicationEngineEnvironment,
      configure: GrpcApplicationConfiguration.() -> Unit): GRpcApplicationEngine {
      return GRpcApplicationEngine(environment, configure)
    }
  }
}