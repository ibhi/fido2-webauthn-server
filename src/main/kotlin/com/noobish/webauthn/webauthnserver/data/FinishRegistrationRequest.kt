package com.noobish.webauthn.webauthnserver.data

import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredential

data class FinishRegistrationRequest(
        val requestId: String,
        val publicKeyCredential: PublicKeyCredential
)