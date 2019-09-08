package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class PublicKeyCredentialUserEntity(
        val id: ByteArray,
        val name: String,
        val displayName: String
)