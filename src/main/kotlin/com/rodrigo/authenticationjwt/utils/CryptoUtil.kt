package com.rodrigo.authenticationjwt.utils

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object CryptUtil {
    private fun encriptar(text: String?): String {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.encode(text)
    }

    /**
     * Encripta un string solo si no tiene 60 caracteres
     * Se asume que si tiene 60 caracteres es un hash bCrypt
     * Aplicable a contraseñas o Strings de los cuales se tenga la certeza que no
     * tienen 60 caracteres.
     *
     * @param text el texto que se desea encriptar
     * @return una cadena encriptada
     */
    fun encriptarSiNoEstaEncriptado(text: String): String {
        return if (text.length == 60) {
            text
        } else {
            encriptar(text)
        }
    }

    fun verificar(noHashedText: String?, hashedText: String?): Boolean {
        val passwordEncoder = BCryptPasswordEncoder()
        return passwordEncoder.matches(noHashedText, hashedText)
    }

    fun verificarComplejidadPass(cadena: String): Boolean {
        // Verificar la longitud mínima
        if (cadena.length < 6) {
            return false
        }

        // Verificar al menos una letra, un número y un carácter especial
        var tieneLetra = false
        var tieneNumero = false
        var tieneCaracterEspecial = false

        for (caracter in cadena.toCharArray()) {
            if (Character.isLetter(caracter)) {
                tieneLetra = true
            } else if (Character.isDigit(caracter)) {
                tieneNumero = true
            } else if (!Character.isLetterOrDigit(caracter)) {
                tieneCaracterEspecial = true
            }

            // Salir del bucle si todos los requisitos se cumplen
            if (tieneLetra && tieneNumero && tieneCaracterEspecial) {
                break
            }
        }

        return tieneLetra && tieneNumero && tieneCaracterEspecial
    }
}