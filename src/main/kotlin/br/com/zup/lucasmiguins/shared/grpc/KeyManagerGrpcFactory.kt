package br.com.zup.lucasmiguins.shared.grpc

import br.com.zup.lucasmiguins.grpc.KeymanagerConsultaGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.KeymanagerListaGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.KeymanagerRegistraGrpcServiceGrpc
import br.com.zup.lucasmiguins.grpc.KeymanagerRemoveGrpcServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registraChave() = KeymanagerRegistraGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeChave() = KeymanagerRemoveGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun consultaChave() = KeymanagerConsultaGrpcServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun listaChaves() = KeymanagerListaGrpcServiceGrpc.newBlockingStub(channel)
}