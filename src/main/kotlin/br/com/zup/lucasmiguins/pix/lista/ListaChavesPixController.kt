package br.com.zup.lucasmiguins.pix.lista

import br.com.zup.lucasmiguins.grpc.KeymanagerListaGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.ListaChavesPixRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class ListaChavesPixController(
    private val listaChavesPixClient: KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub
) {

    @Get("/pix")
    fun lista(clienteId: UUID): HttpResponse<Any> {

        val pix = listaChavesPixClient.lista(
            ListaChavesPixRequest.newBuilder()
                .setClienteId(clienteId.toString())
                .build()
        )

        val chaves = pix.chavesList.map {
            ListaChavesPixResponse(it)
        }

        return HttpResponse.ok(chaves)
    }
}