package br.com.zup.lucasmiguins.pix.registra

import br.com.zup.lucasmiguins.grpc.KeymanagerRegistraGrpcServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import java.util.*
import javax.validation.Valid

@Validated
@Controller("/api/v1/clientes")
class RegistraPixChavePixController(

    private val registraChavePixClient: KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceBlockingStub
) {

    @Post("/{clienteId}/pix")
    fun registra(@PathVariable clienteId: UUID, @Valid @Body request: NovaChavePixRequest): HttpResponse<Any> {

        val grpcResponse = registraChavePixClient.registra(request.paraModeloGrpc(clienteId))

        val location = HttpResponse.uri("/api/v1/clientes/$clienteId/pix/${grpcResponse.pixId}")

        return HttpResponse.created(location)
    }
}