package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray


data class PublicKeyCredentialRequestOptions(
        val rpId: String,
        val challenge: ByteArray,
        val allowCredentials: List<PublicKeyCredentialDescriptor>
)