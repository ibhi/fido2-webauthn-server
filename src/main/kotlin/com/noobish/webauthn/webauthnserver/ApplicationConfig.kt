package com.noobish.webauthn.webauthnserver

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.noobish.webauthn.webauthnserver.core.data.json.JsonByteArrayDeserializer
import com.noobish.webauthn.webauthnserver.core.data.json.JsonByteArraySerializer
import com.webauthn4j.converter.jackson.deserializer.ChallengeDeserializer
import com.webauthn4j.converter.jackson.serializer.ChallengeSerializer
import com.webauthn4j.data.client.challenge.Challenge
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class ApplicationConfig: Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(jacksonObjectMapperBuilder: Jackson2ObjectMapperBuilder) {
        val jsonByteArrayDeserializer: JsonByteArrayDeserializer = JsonByteArrayDeserializer()
        val jsonByteArraySerializer: JsonByteArraySerializer = JsonByteArraySerializer()

        jacksonObjectMapperBuilder
                .deserializerByType(ByteArray::class.java, jsonByteArrayDeserializer)
                .serializerByType(ByteArray::class.java, jsonByteArraySerializer)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .deserializerByType(Challenge::class.java, ChallengeDeserializer())
                .serializerByType(Challenge::class.java, ChallengeSerializer())
    }

}