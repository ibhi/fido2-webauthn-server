package com.noobish.webauthn.webauthnserver.core.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noobish.webauthn.webauthnserver.core.data.json.JsonLongSerializable
import com.noobish.webauthn.webauthnserver.core.data.json.JsonLongSerializer
import java.util.*
import java.util.stream.Stream

@JsonSerialize(using = JsonLongSerializer::class)
enum class COSEAlgorithmIdentifier private constructor(val id: Long) : JsonLongSerializable {
    EdDSA(-8),
    ES256(-7),
    RS256(-257);

    override fun toJsonNumber(): Long {
        return id
    }

    companion object {

        fun fromId(id: Long): Optional<COSEAlgorithmIdentifier> {
            return Stream.of(*values()).filter { v -> v.id == id }.findAny()
        }

        @JsonCreator
        private fun fromJson(id: Long): COSEAlgorithmIdentifier {
            return fromId(id).orElseThrow { IllegalArgumentException("Unknown COSE algorithm identifier: $id") }
        }
    }

}