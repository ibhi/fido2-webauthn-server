package com.noobish.webauthn.webauthnserver.data

import com.noobish.webauthn.webauthnserver.core.data.AuthenticatorAttestationResponse
import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredential

data class FinishRegistrationRequest(
        val requestId: ByteArray,
        val publicKeyCredential: PublicKeyCredential<AuthenticatorAttestationResponse>
)