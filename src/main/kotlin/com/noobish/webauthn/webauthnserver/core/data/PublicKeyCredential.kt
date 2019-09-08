package com.noobish.webauthn.webauthnserver.core.data

data class PublicKeyCredential(
        val rawId: ByteArray,
        val id: ByteArray,
        val response: AuthenticatorResponse
)