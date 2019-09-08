package com.noobish.webauthn.webauthnserver.core.data

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializable
import com.noobish.webauthn.webauthnserver.core.data.json.JsonStringSerializer
import java.util.*

@JsonSerialize(using = JsonStringSerializer::class)
class ByteArray : Comparable<ByteArray>, JsonStringSerializable {

    private val bytes: kotlin.ByteArray

    /**
     * @return the content bytes encoded as Base64Url data.
     */
    val base64Url: String

    val isEmpty: Boolean
        get() = size() == 0

    /**
     * @return the content bytes encoded as classic Base64 data.
     */
//    val base64: String
//        get() = BASE64_ENCODER.encodeToString(bytes)

//    /**
//     * @return the content bytes encoded as hexadecimal data.
//     */
//    val hex: String
//        get() = BinaryUtil.toHex(bytes)

    @JsonCreator
    constructor(base64: String) {
        this.bytes = BASE64URL_DECODER.decode(base64)
        this.base64Url = base64
    }

    /**
     * Create a new instance by copying the contents of `bytes`.
     */
    constructor(bytes: kotlin.ByteArray) {
        this.bytes = copy(bytes)
        this.base64Url = BASE64URL_ENCODER.encodeToString(this.bytes)
    }


    /**
     * @return a new instance containing a copy of this instance followed by a copy of `tail`.
     */
//    fun concat(tail: ByteArray): ByteArray {
//        return ByteArray(Arrays.concatenate(this.bytes, tail.bytes))
//    }

    fun size(): Int {
        return this.bytes.size
    }

    /**
     * @return a copy of the raw byte contents.
     */
    fun getBytes(): kotlin.ByteArray {
        return copy(bytes)
    }

    /**
     * Used by JSON serializer.
     */
    override fun toJsonString(): String {
        return base64Url
    }

    override fun compareTo(other: ByteArray): Int {
        if (bytes.size != other.bytes.size) {
            return bytes.size - other.bytes.size
        }

        for (i in bytes.indices) {
            if (bytes[i] != other.bytes[i]) {
                return bytes[i] - other.bytes[i]
            }
        }

        return 0
    }

    override fun toString(): String {
        return "ByteArray(bytes=${Arrays.toString(bytes)}, base64Url='$base64Url')"
    }


    companion object {

        private val BASE64_ENCODER = Base64.getEncoder()
        private val BASE64_DECODER = Base64.getDecoder()

        private val BASE64URL_ENCODER = Base64.getUrlEncoder().withoutPadding()
        private val BASE64URL_DECODER = Base64.getUrlDecoder()

        /**
         * Create a new instance by decoding `base64` as classic Base64 data.
         */
        fun fromBase64(base64: String): ByteArray {
            return ByteArray(BASE64_DECODER.decode(base64))
        }

        fun copy(bytes: kotlin.ByteArray): kotlin.ByteArray {
            return bytes.copyOf(bytes.size)
        }


        /**
         * Create a new instance by decoding `base64` as Base64Url data.
         *
         * @throws Base64UrlException if `base64` is not valid Base64Url data.
         */
        fun fromBase64Url(base64: String): ByteArray {
            return ByteArray(base64)
        }

        /**
         * Create a new instance by decoding `hex` as hexadecimal data.
         *
         * @throws HexException if `hex` is not valid hexadecimal data.
         */
//        fun fromHex(hex: String): ByteArray {
//            try {
//                return ByteArray(BinaryUtil.fromHex(hex))
//            } catch (e: Exception) {
//                throw HexException("Invalid hexadecimal encoding: $hex", e)
//            }
//
//        }
    }

}