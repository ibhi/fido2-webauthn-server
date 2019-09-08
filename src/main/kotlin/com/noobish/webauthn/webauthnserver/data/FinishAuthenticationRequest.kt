package com.noobish.webauthn.webauthnserver.data

import com.noobish.webauthn.webauthnserver.core.data.AuthenticatorAssertionResponse
import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredential


data class FinishAuthenticationRequest(
        val requestId: ByteArray,
        val userName: String,
        val publicKeyCredential: PublicKeyCredential<AuthenticatorAssertionResponse>
)