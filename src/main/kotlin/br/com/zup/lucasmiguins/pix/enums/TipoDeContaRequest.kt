package br.com.zup.lucasmiguins.pix.enums

import br.com.zup.lucasmiguins.grpc.EnumTipoDeConta

enum class TipoDeContaRequest(val atributoGrpc: EnumTipoDeConta) {

    CONTA_CORRENTE(EnumTipoDeConta.CONTA_CORRENTE),

    CONTA_POUPANCA(EnumTipoDeConta.CONTA_POUPANCA)
}