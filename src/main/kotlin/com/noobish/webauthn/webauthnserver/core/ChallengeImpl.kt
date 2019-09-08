package com.noobish.webauthn.webauthnserver.core

import com.webauthn4j.data.client.challenge.Challenge

class ChallengeImpl(val bytes: ByteArray): Challenge {
    override fun getValue(): ByteArray {
        return this.bytes
    }
}