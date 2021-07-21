package br.com.zup.lucasmiguins.pix.consulta

import br.com.zup.lucasmiguins.grpc.ConsultaChavePixResponse
import br.com.zup.lucasmiguins.grpc.EnumTipoDeChave
import br.com.zup.lucasmiguins.grpc.EnumTipoDeConta
import br.com.zup.lucasmiguins.grpc.KeymanagerConsultaGrpcServiceGrpc
import br.com.zup.lucasmiguins.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.any
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ConsultaChavePixControllerTest {

    @field:Inject
    lateinit var consultaChaveStub: KeymanagerConsultaGrpcServiceGrpc.KeymanagerConsultaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    // happy path
    @Test
    internal fun `deve carregar uma chave pix`() {

        val clienteId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(consultaChaveStub.consulta(any())).willReturn(consultaChavePixResponse(clienteId, pixId))

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/$pixId")
        val response = client.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
    }

    @Test
    internal fun `deve retornar erro para clienteId invalido`() {
        val thrown  = assertThrows<HttpClientResponseException> {
            val request = HttpRequest.GET<Any>("/api/v1/clientes/INVALIDO/pix/${UUID.randomUUID()}")
            client.toBlocking().exchange(request, Any::class.java)
        }

        with(thrown) {
            assertEquals(HttpStatus.BAD_REQUEST.code, status.code)
        }
    }

    private fun consultaChavePixResponse(clienteId: String, pixId: String): ConsultaChavePixResponse {
       return ConsultaChavePixResponse.newBuilder()
            .setClienteId(clienteId)
            .setPixId(pixId)
            .setChave(ConsultaChavePixResponse.ChavePix.newBuilder()
                .setTipo(EnumTipoDeChave.EMAIL)
                .setChave("ponte@email.com")
                .setConta(ConsultaChavePixResponse.ChavePix.ContaInfo.newBuilder()
                    .setTipo(EnumTipoDeConta.CONTA_CORRENTE)
                    .setInstituicao("Itau")
                    .setNomeDoTitular("Rafael Ponte")
                    .setCpfDoTitular("52564396003")
                    .setAgencia("0001")
                    .setNumeroDaConta("0000-1")
                    .build())
                .setCriadaEm(timeStampNow())
                .build()
            ).build()
    }

    private fun timeStampNow(): Timestamp {
        return LocalDateTime.now().let {
            val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
            Timestamp.newBuilder()
                .setSeconds(createdAt.epochSecond)
                .setNanos(createdAt.nano)
                .build()
        }
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubConsultaMock() = Mockito.mock(KeymanagerConsultaGrpcServiceGrpc.KeymanagerConsultaGrpcServiceBlockingStub::class.java)
    }
}