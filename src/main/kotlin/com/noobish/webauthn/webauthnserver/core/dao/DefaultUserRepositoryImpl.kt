package com.noobish.webauthn.webauthnserver.core.dao

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class DefaultUserRepositoryImpl: UserRespository<String, DefaultUser> {

    private val userStorage: Cache<String, DefaultUser> = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(60, TimeUnit.MINUTES)
            .build()

    override fun save(key: String, user: DefaultUser) {
        userStorage.put(key, user)
    }

    override fun find(key: String): DefaultUser? {
        return userStorage.getIfPresent(key)
    }
}