package com.noobish.webauthn.webauthnserver.core.data

data class PublicKeyCredentialUserEntity(
        val id: ByteArray,
        val name: String,
        val displayName: String
)