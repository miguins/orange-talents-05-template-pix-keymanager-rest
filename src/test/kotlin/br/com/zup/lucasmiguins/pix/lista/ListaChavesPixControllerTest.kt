package br.com.zup.lucasmiguins.pix.lista

import br.com.zup.lucasmiguins.grpc.EnumTipoDeChave
import br.com.zup.lucasmiguins.grpc.EnumTipoDeConta
import br.com.zup.lucasmiguins.grpc.KeymanagerListaGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.ListaChavesPixResponse
import br.com.zup.lucasmiguins.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListaChavesPixControllerTest {

    @field:Inject
    lateinit var listaChavesStub: KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var client: HttpClient

    // happy path
    @Test
    internal fun `deve listar todas as chaves de um cliente`() {

        val clienteId = UUID.randomUUID().toString()

        val respostaGrpc = listaChavePixResponse(clienteId)

        given(listaChavesStub.lista(Mockito.any())).willReturn(respostaGrpc)

        val request = HttpRequest.GET<Any>("/api/v1/clientes/$clienteId/pix/")
        val response = client.toBlocking().exchange(request, List::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertNotNull(response.body())
        assertEquals(response.body()!!.size, 3)
    }

    private fun listaChavePixResponse(clienteId: String): ListaChavesPixResponse {
        return ListaChavesPixResponse.newBuilder()
            .setClienteId(clienteId)
            .addAllChaves(listOf(chavePixEmail(), chavePixCelular(), chavePixCpf()))
            .build()
    }

    private fun chavePixEmail(): ListaChavesPixResponse.ChavePix {
       return ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(EnumTipoDeChave.EMAIL)
            .setChave("ponte@email.com")
            .setTipoDeConta(EnumTipoDeConta.CONTA_CORRENTE)
            .setCriadaEm(timeStampNow())
            .build()
    }

    private fun chavePixCelular(): ListaChavesPixResponse.ChavePix {
        return ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(EnumTipoDeChave.CELULAR)
            .setChave("+5591912345678")
            .setTipoDeConta(EnumTipoDeConta.CONTA_CORRENTE)
            .setCriadaEm(timeStampNow())
            .build()
    }

    private fun chavePixCpf(): ListaChavesPixResponse.ChavePix {
        return ListaChavesPixResponse.ChavePix.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setTipo(EnumTipoDeChave.CPF)
            .setChave("+42812084022")
            .setTipoDeConta(EnumTipoDeConta.CONTA_CORRENTE)
            .setCriadaEm(timeStampNow())
            .build()
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
        fun stubListaMock() = Mockito.mock(KeymanagerListaGrpcServiceGrpc.KeymanagerListaGrpcServiceBlockingStub::class.java)
    }
}