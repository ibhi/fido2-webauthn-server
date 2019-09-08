package com.noobish.webauthn.webauthnserver.core.data

import com.webauthn4j.data.PublicKeyCredentialRequestOptions
import kotlin.ByteArray

data class CredentialRequestOptions(
        val requestId: ByteArray,
        val publicKey: PublicKeyCredentialRequestOptions
)