package com.noobish.webauthn.webauthnserver.core.data.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.util.*


private val BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding()

class JsonByteArraySerializer: JsonSerializer<ByteArray>() {
    override fun serialize(data: ByteArray, jsonGenerator: JsonGenerator, provider: SerializerProvider) {
        jsonGenerator.writeString(BASE64_URL_ENCODER.encodeToString(data))
    }
}