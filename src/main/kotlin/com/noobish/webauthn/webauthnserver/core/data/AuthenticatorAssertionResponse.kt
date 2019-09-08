package com.noobish.webauthn.webauthnserver.core.data

data class AuthenticatorAssertionResponse(
        override val clientDataJSON: ByteArray,
        val authenticatorData: ByteArray,
        val signature: ByteArray,
        val userHandle: ByteArray?
): AuthenticatorResponse