package com.noobish.webauthn.webauthnserver.core.data


data class AuthenticatorAttestationResponse(
        override val clientDataJSON: ByteArray,
        val attestationObject: ByteArray
): AuthenticatorResponse
