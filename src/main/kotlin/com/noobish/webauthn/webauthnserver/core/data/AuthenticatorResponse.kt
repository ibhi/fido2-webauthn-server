package com.noobish.webauthn.webauthnserver.core.data

data class AuthenticatorResponse(
        val attestationObject: ByteArray,
        val clientDataJSON: ByteArray
)
