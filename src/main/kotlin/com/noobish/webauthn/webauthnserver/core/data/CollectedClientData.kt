package com.noobish.webauthn.webauthnserver.core.data

import kotlin.ByteArray

data class CollectedClientData(
        val type: String,
        val challenge: ByteArray,
        val origin: String,
        val tokenBinding: TokenBinding?
)