package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class PublicKeyCredential<A: AuthenticatorResponse>(
        val rawId: ByteArray,
        val id: ByteArray,
        val response: A,
        val type: String
)