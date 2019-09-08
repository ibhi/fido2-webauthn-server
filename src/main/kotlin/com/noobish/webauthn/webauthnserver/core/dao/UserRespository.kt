package com.noobish.webauthn.webauthnserver.core.dao

interface UserRespository<K, V: User> {
    fun save(key: K, user: V)
    fun find(key: K): V?
}