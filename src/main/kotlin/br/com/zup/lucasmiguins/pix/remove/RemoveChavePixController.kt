package br.com.zup.lucasmiguins.pix.remove

import br.com.zup.lucasmiguins.grpc.KeymanagerRemoveGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.RemoveChavePixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class RemoveChavePixController(private val removeChavePixClient: KeymanagerRemoveGrpcServiceGrpc.KeymanagerRemoveGrpcServiceBlockingStub) {

    @Delete("/pix/{pixId}")
    fun remove(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        removeChavePixClient.remove(
            RemoveChavePixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .setPixId(pixId.toString())
                .build()
        )

        return HttpResponse.ok()
    }
}