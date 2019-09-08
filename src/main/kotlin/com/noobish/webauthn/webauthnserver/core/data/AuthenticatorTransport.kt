package com.noobish.webauthn.webauthnserver.core.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializable
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializer
import java.util.*
import java.util.stream.Stream

@JsonSerialize(using =JsonStringSerializer::class)
enum class AuthenticatorTransport(private val value: String): JsonStringSerializable {

    USB("usb"),
    NFC("nfc"),
    BLE("ble"),
    INTERNAL("internal"),
    LIGHTNING("lightning");

    private fun fromString(value: String): Optional<AuthenticatorTransport> {
        return Stream.of(*AuthenticatorTransport.values()).filter { v -> v.value == value }.findAny()
    }

    @JsonCreator
    private fun fromJsonString(value: String): AuthenticatorTransport {
        return fromString(value).orElseThrow {
            IllegalArgumentException(String.format(
                    "Unknown %s value: %s", AuthenticatorTransport::class.java.simpleName, value
            ))
        }
    }

    override fun toJsonString(): String {
        return value
    }
}