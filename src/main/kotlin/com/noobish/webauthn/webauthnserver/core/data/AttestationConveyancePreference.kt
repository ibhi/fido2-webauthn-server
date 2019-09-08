package com.noobish.webauthn.webauthnserver.core.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializable
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializer
import java.util.*
import java.util.stream.Stream

@JsonSerialize(using = JsonStringSerializer::class)
enum class AttestationConveyancePreference private constructor(val id: String): JsonStringSerializable {
    NONE("none"),
    INDIRECT("indirect"),
    DIRECT("direct");

    private fun fromString(id: String): Optional<AttestationConveyancePreference> {
        return Stream.of(*values()).filter { v -> v.id == id }.findAny()
    }

    @JsonCreator
    private fun fromJsonString(id: String): AttestationConveyancePreference {
        return fromString(id).orElseThrow {
            IllegalArgumentException(String.format(
                    "Unknown %s value: %s", AttestationConveyancePreference::class.java.simpleName, id
            ))
        }
    }

    override fun toJsonString(): String {
        return id
    }

}