package br.com.zup.lucasmiguins.pix.consulta

import br.com.zup.lucasmiguins.grpc.ConsultaChavePixRequest
import br.com.zup.lucasmiguins.grpc.KeymanagerConsultaGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.*

@Controller("/api/v1/clientes/{clienteId}")
class ConsultaChavePixController(
    private val consultaChavePixClient: KeymanagerConsultaGrpcServiceGrpc.KeymanagerConsultaGrpcServiceBlockingStub
) {

    @Get("/pix/{pixId}")
    fun consulta(clienteId: UUID, pixId: UUID): HttpResponse<Any> {

        val response = consultaChavePixClient.consulta(
            ConsultaChavePixRequest.newBuilder()
                .setPixId(
                    ConsultaChavePixRequest.FiltroPorPixId.newBuilder()
                        .setClienteId(clienteId.toString())
                        .setPixId(pixId.toString())
                        .build()
                ).build()
        )

        return HttpResponse.ok(ConsultaChavePixResponse(response))
    }
}