package com.noobish.webauthn.webauthnserver.data

import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredentialCreationOptions


data class RegistrationResponse(
        val userName: String,
        val credentialNickName: String?,
        val id: ByteArray,
        val publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions
)