package com.noobish.webauthn.webauthnserver.core.dao

import com.webauthn4j.authenticator.Authenticator

interface User {
    val userId: ByteArray
    val userName: String
    val name: String
    val authenticators: Set<Authenticator>
}