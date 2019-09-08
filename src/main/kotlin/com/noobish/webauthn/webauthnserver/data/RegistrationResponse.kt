package com.noobish.webauthn.webauthnserver.data

import com.webauthn4j.data.PublicKeyCredentialCreationOptions

data class RegistrationResponse(
        val userName: String,
        val credentialNickName: String?,
        val id: ByteArray,
        val publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions
)