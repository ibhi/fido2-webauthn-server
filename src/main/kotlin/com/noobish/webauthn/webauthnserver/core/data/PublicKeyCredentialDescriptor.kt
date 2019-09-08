package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray


data class PublicKeyCredentialDescriptor(
        val type: PublicKeyCredentialType,
        val id: ByteArray,
        val transports: Set<AuthenticatorTransport>
)