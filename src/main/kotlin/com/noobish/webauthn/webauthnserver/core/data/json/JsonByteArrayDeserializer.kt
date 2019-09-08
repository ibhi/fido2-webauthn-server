package com.noobish.webauthn.webauthnserver.core.data.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.util.*


private val BASE64_URL_DECODER = Base64.getUrlDecoder()

class JsonByteArrayDeserializer: JsonDeserializer<ByteArray>() {

    override fun deserialize(jsonParser: JsonParser, context: DeserializationContext?): ByteArray {
        val data: String = jsonParser.text
        return BASE64_URL_DECODER.decode(data)
    }

}