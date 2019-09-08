package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class PublicKeyCredentialCreationOptions(
        val challenge: ByteArray,
        val rp: PublicKeyCredentialRpEntity,
        val user: PublicKeyCredentialUserEntity,
        val attestation: AttestationConveyancePreference,
        val pubKeyCredParams: List<PublicKeyCredentialParameters>
)