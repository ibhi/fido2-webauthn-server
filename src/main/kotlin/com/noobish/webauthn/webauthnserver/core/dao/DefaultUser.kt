package com.noobish.webauthn.webauthnserver.core.dao

import com.webauthn4j.authenticator.Authenticator


data class DefaultUser(
        override val userId: ByteArray,
        override val userName: String,
        override val name: String,
        override val authenticators: Set<Authenticator>
): User {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DefaultUser

        if (!userId.contentEquals(other.userId)) return false
        if (userName != other.userName) return false
        if (name != other.name) return false
        if (authenticators != other.authenticators) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId.contentHashCode()
        result = 31 * result + userName.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + authenticators.hashCode()
        return result
    }
}