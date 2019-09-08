package com.noobish.webauthn.webauthnserver.core.data

data class PublicKeyCredentialParameters(
        val type: PublicKeyCredentialType,
        val alg: COSEAlgorithmIdentifier
)
