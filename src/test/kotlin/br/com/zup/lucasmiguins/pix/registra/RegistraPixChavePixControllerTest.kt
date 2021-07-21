package br.com.zup.lucasmiguins.pix.registra

import br.com.zup.lucasmiguins.grpc.KeymanagerRegistraGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.RegistraChavePixResponse
import br.com.zup.lucasmiguins.pix.enums.TipoDeChaveRequest
import br.com.zup.lucasmiguins.pix.enums.TipoDeContaRequest
import br.com.zup.lucasmiguins.shared.grpc.KeyManagerGrpcFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegistraPixChavePixControllerTest {

    @field:Inject
    lateinit var registraStub: KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient


    // happy path
    @Test
    internal fun `deve registrar uma nova chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val respostaGrpc = RegistraChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .build()

        given(registraStub.registra(Mockito.any())).willReturn(respostaGrpc)


        val novaChavePix = NovaChavePixRequest(
            tipoDeConta = TipoDeContaRequest.CONTA_CORRENTE,
            chave = "ponte@email2.com",
            tipoDeChave = TipoDeChaveRequest.EMAIL
        )

        val request = HttpRequest.POST("/api/v1/clientes/$clienteId/pix", novaChavePix)
        val response = client.toBlocking().exchange(request, NovaChavePixRequest::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertTrue(response.header("Location")!!.contains(pixId))
    }


    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(KeymanagerRegistraGrpcServiceGrpc.KeymanagerRegistraGrpcServiceBlockingStub::class.java)
    }
}