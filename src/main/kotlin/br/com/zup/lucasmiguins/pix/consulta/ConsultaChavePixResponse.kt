package br.com.zup.lucasmiguins.pix.consulta

import br.com.zup.lucasmiguins.grpc.ConsultaChavePixResponse
import br.com.zup.lucasmiguins.grpc.EnumTipoDeConta
import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Introspected
class ConsultaChavePixResponse(grpcResponse: ConsultaChavePixResponse) {

    val pixId = grpcResponse.pixId
    val tipo = grpcResponse.chave.tipo
    val chave = grpcResponse.chave.chave

    val criadaEm = grpcResponse.chave.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

    val tipoConta = when (grpcResponse.chave.conta.tipo) {
        EnumTipoDeConta.CONTA_CORRENTE -> "CONTA_CORRENTE"
        EnumTipoDeConta.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_RECONHECIDA"
    }

    val conta = mapOf(
        Pair("tipo", tipoConta),
        Pair("instituicao", grpcResponse.chave.conta.instituicao),
        Pair("nomeDoTitular", grpcResponse.chave.conta.nomeDoTitular),
        Pair("cpfDoTitular", grpcResponse.chave.conta.cpfDoTitular),
        Pair("agencia", grpcResponse.chave.conta.agencia),
        Pair("numero", grpcResponse.chave.conta.numeroDaConta)
    )
}