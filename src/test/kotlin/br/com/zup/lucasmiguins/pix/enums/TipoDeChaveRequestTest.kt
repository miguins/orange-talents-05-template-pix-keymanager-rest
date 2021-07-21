package br.com.zup.lucasmiguins.pix.enums

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import br.com.zup.lucasmiguins.pix.enums.TipoDeChaveRequest.*


internal class TipoDeChaveRequestTest() {

    @Nested
    inner class CpfTest {
        @Test
        internal fun `deve retornar false para um cpf invalido ou nao informado`() {
            with(CPF) {
                assertFalse(valida(chave = "12345678"))
                assertFalse(valida(chave = ""))
                assertFalse(valida(chave = null))
            }
        }

        @Test
        internal fun `deve retornar true para um cpf valido`() {
            with(CPF) {
                assertTrue(valida(chave = "42812084022"))
                assertTrue(valida(chave = "428.120.840-22"))
            }
        }
    }

    @Nested
    inner class CelularTest {
        @Test
        fun `deve retornar true para um celular valido`() {
            with(CELULAR) {
                assertTrue(valida(chave = "+55 91 912345678".replace(" ", "")))
            }
        }

        @Test
        fun `deve retornar false para um celular invalido ou nao preenchido`() {
            with(CELULAR) {
                assertFalse(valida(chave = "91912345678"))
                assertFalse(valida(chave = "+55abc1234"))
                assertFalse(valida(chave = ""))
                assertFalse(valida(chave = null))
            }
        }
    }

    @Nested
    inner class EmailTest {
        @Test
        fun `deve retornar true para um email valido`() {
            with(EMAIL) {
                assertTrue(valida(chave = "email@valido.com.br"))
            }
        }

        @Test
        fun `deve retornar false para um email valido`() {
            with(EMAIL) {
                assertTrue(valida(chave = "email@valido.com.br"))
            }
        }
    }

    @Nested
    inner class AleatoriaTest {
        @Test
        fun `deve retornar true quando a chave for nula ou vazia`() {
            with(ALEATORIA) {
                assertTrue(valida(chave = ""))
                assertTrue(valida(chave = null))
            }
        }

        @Test
        fun `deve retornar false quando a chave for preenchida`() {
            with(ALEATORIA) {
                assertFalse(valida(chave = "valor"))
            }
        }
    }
}