package com.noobish.webauthn.webauthnserver.core.data

import com.webauthn4j.data.PublicKeyCredentialCreationOptions
import kotlin.ByteArray

data class CredentialCreationOptions(
//       This field is for our own tracking purpose
        val requestId: ByteArray,
        val publicKey: PublicKeyCredentialCreationOptions
)