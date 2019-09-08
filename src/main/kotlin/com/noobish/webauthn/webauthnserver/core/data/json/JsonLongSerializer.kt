package com.noobish.webauthn.webauthnserver.core.data.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException

class JsonLongSerializer<T : JsonLongSerializable> : JsonSerializer<T>() {

    @Throws(IOException::class)
    override fun serialize(t: T, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
        jsonGenerator.writeNumber(t.toJsonNumber())
    }

}
