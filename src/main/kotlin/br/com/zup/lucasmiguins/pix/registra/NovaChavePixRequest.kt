package br.com.zup.lucasmiguins.pix.registra

import br.com.zup.lucasmiguins.grpc.EnumTipoDeChave
import br.com.zup.lucasmiguins.grpc.EnumTipoDeConta
import br.com.zup.lucasmiguins.grpc.RegistraChavePixRequest
import br.com.zup.lucasmiguins.pix.enums.TipoDeChaveRequest
import br.com.zup.lucasmiguins.pix.enums.TipoDeContaRequest
import io.micronaut.core.annotation.Introspected
import org.jetbrains.annotations.NotNull
import java.util.*
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
class NovaChavePixRequest(
    @field:NotNull val tipoDeConta: TipoDeContaRequest?,
    @field:NotNull val tipoDeChave: TipoDeChaveRequest?,
    @field:Size(max = 77) val chave: String?
) {

    fun paraModeloGrpc(clienteId: UUID): RegistraChavePixRequest {
        return RegistraChavePixRequest.newBuilder()
            .setClienteId(clienteId.toString())
            .setTipoDeConta(tipoDeConta?.atributoGrpc ?: EnumTipoDeConta.UNKNOWN_TIPO_CONTA)
            .setTipoDeChave(tipoDeChave?.atributoGrpc ?: EnumTipoDeChave.UNKNOWN_TIPO_CHAVE)
            .setChave(chave ?: "")
            .build()
    }
}

