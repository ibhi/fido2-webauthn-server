package com.noobish.webauthn.webauthnserver.core.data

data class CollectedClientData(
        val type: String,
        val challenge: ByteArray,
        val origin: String,
        val tokenBinding: TokenBinding?
)