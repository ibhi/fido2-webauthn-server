package com.noobish.webauthn.webauthnserver.core.data

interface AuthenticatorResponse {
    val clientDataJSON: ByteArray
}