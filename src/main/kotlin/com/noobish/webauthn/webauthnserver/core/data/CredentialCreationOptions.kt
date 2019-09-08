package com.noobish.webauthn.webauthnserver.core.data

data class CredentialCreationOptions(
//       This field is for our own tracking purpose
        val requestId: String,
        val publicKey: PublicKeyCredentialCreationOptions
)