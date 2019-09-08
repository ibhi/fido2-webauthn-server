package com.noobish.webauthn.webauthnserver.core.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializable
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializer
import java.util.*
import java.util.stream.Stream

@JsonSerialize(using = JsonStringSerializer::class)
enum class PublicKeyCredentialType(val id: String): JsonStringSerializable {

    PUBLIC_KEY("public-key");

    private fun fromString(id: String): Optional<PublicKeyCredentialType> {
        return Stream.of(*values()).filter { v -> v.id == id }.findAny()
    }

    @JsonCreator
    private fun fromJsonString(id: String): PublicKeyCredentialType {
        return fromString(id).orElseThrow {
            IllegalArgumentException(String.format(
                    "Unknown %s value: %s", PublicKeyCredentialType::class.java.simpleName, id
            ))
        }
    }

    override fun toJsonString(): String {
        return id
    }

}
