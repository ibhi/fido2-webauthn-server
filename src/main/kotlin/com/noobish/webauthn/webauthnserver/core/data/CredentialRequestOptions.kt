package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class CredentialRequestOptions(
        val requestId: ByteArray,
        val publicKey: PublicKeyCredentialRequestOptions
)