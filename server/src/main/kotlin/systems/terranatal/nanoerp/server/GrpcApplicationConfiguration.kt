package systems.terranatal.nanoerp.server

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.ServerServiceDefinition
import io.ktor.server.engine.*

class GrpcApplicationConfiguration: ApplicationEngine.Configuration() {
  var port: Int = 58585
  var server: Server? = null

  fun makeServer(vararg services: ServerServiceDefinition): Server {
    val builder = ServerBuilder.forPort(port);
    for (service in services) {
      builder.addService(service);
    }
    return builder.build();
  }
}