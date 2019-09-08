package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class PublicKeyCredential<R: AuthenticatorResponse>(
        val rawId: ByteArray,
        val id: ByteArray,
        val response: R,
        val type: String
)