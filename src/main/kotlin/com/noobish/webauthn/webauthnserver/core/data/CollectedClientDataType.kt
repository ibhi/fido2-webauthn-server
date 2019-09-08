package com.noobish.webauthn.webauthnserver.core.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializable
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializer
import java.util.*
import java.util.stream.Stream

@JsonSerialize(using = JsonStringSerializer::class)
enum class CollectedClientDataType private constructor(val id: String): JsonStringSerializable {
    WEBAUTHN_CREATE("webauthn.create"),
    WEBAUTHN_GET("webauthn.get");

    @JsonCreator
    fun fromJsonString(id: String): CollectedClientDataType {
        return fromString(id).orElseThrow {
            IllegalArgumentException(String.format(
                    "Unknown %s value: %s", CollectedClientDataType::class.java.simpleName, id
            ))
        }
    }

    private fun fromString(id: String): Optional<CollectedClientDataType> {
        return Stream.of(*CollectedClientDataType.values()).filter { v -> v.id == id }.findAny()
    }

    override fun toJsonString(): String {
        return id
    }
}