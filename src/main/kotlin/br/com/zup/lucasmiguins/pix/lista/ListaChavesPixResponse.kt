package br.com.zup.lucasmiguins.pix.lista

import br.com.zup.lucasmiguins.grpc.ListaChavesPixResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class ListaChavesPixResponse(grpcResponse: ListaChavesPixResponse.ChavePix) {

    val id = grpcResponse.pixId
    val chave = grpcResponse.chave
    val tipo = grpcResponse.tipo
    val tipoDeConta = grpcResponse.tipoDeConta
    val criadaEm = grpcResponse.criadaEm.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
}
