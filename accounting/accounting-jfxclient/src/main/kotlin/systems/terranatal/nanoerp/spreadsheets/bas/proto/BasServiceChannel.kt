package systems.terranatal.nanoerp.spreadsheets.bas.proto

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import systems.terranatal.nanoerp.accounting.BasGrpcKt
import systems.terranatal.nanoerp.accounting.accountDefinition
import systems.terranatal.nanoerp.spreadsheets.bas.BasAccountTreeTable
import java.io.Closeable
import java.util.concurrent.TimeUnit


class BasServiceChannel(private val channel: ManagedChannel): Closeable {

  private val stub = BasGrpcKt.BasCoroutineStub(channel)

  suspend fun addAccount(ba: BasAccountTreeTable.BasAccountRecord) = stub.addAccount(
    accountDefinition {
      code = ba.code!!
      description = ba.name!!
      accountClass = ba.accountClass!!
    }
  )

  override fun close() {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
  }

  companion object {
    val client by lazy {
      val channel = ManagedChannelBuilder
        //.forTarget("localhost:8080/accounting-jakarta-0.1.0-SNAPSHOT/accountCharts.Bas/AddAccount")
        .forAddress("localhost", 8080)
        .usePlaintext().build()
      println(channel.authority())
      return@lazy BasServiceChannel(channel)
    }
  }
}