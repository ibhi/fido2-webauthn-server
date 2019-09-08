package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class CredentialCreationOptions(
//       This field is for our own tracking purpose
        val requestId: ByteArray,
        val publicKey: PublicKeyCredentialCreationOptions
)