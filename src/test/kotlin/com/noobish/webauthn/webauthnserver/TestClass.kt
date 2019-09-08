//package com.noobish.webauthn.webauthnserver
//
//import com.fasterxml.jackson.core.Base64Variants
//import com.fasterxml.jackson.databind.DeserializationFeature
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.dataformat.cbor.CBORFactory
//import com.fasterxml.jackson.module.kotlin.KotlinModule
//import com.fasterxml.jackson.module.kotlin.readValue
//import com.noobish.webauthn.webauthnserver.core.data.COSEAlgorithmIdentifier
//import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredential
//import org.junit.Test
//
//
//class TestClass {
//
//    private val objectMapper = ObjectMapper().apply {
//        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//        registerModule(KotlinModule())
//    }
//
//    private val f = CBORFactory()
//    private val cborObjectMapper = ObjectMapper(f).apply {
//        registerModule(KotlinModule())
//        setBase64Variant(Base64Variants.MODIFIED_FOR_URL)
//    }
//
//    @Test
//    fun test() {
//        val testJson = "{\"rawId\":\"Kf8VaYvlCZA02UMqXHx5zwzAUOjasGOeRNAGSdDVVkuUqzbgaf9TSOJC2QjflR1ouNEPyZxaGteGcmh00IgHow\",\"response\":{\"attestationObject\":\"o2NmbXRmcGFja2VkZ2F0dFN0bXSjY2FsZyZjc2lnWEYwRAIgYGroOAN_6yhWRnDxuGO-iB2AfxrjG4JUHGzPObeqcfsCIC5KWflf2v-K3RKbHp7VX4DaQ1u1vV9tjoo2UFdvpCn2Y3g1Y4FZAsEwggK9MIIBpaADAgECAgQYrEbAMA0GCSqGSIb3DQEBCwUAMC4xLDAqBgNVBAMTI1l1YmljbyBVMkYgUm9vdCBDQSBTZXJpYWwgNDU3MjAwNjMxMCAXDTE0MDgwMTAwMDAwMFoYDzIwNTAwOTA0MDAwMDAwWjBuMQswCQYDVQQGEwJTRTESMBAGA1UECgwJWXViaWNvIEFCMSIwIAYDVQQLDBlBdXRoZW50aWNhdG9yIEF0dGVzdGF0aW9uMScwJQYDVQQDDB5ZdWJpY28gVTJGIEVFIFNlcmlhbCA0MTM5NDM0ODgwWTATBgcqhkjOPQIBBggqhkjOPQMBBwNCAAR56jssfElwEGIjDNI_62DlKTFx1IPxAL6FnWsPg5cDAbVGzdRuz8rj4_MPgentYr0mjUwevTezvL6SqMKu6046o2wwajAiBgkrBgEEAYLECgIEFTEuMy42LjEuNC4xLjQxNDgyLjEuNzATBgsrBgEEAYLlHAIBAQQEAwIFIDAhBgsrBgEEAYLlHAEBBAQSBBDLaUgej_dAOZPsCicpoVSoMAwGA1UdEwEB_wQCMAAwDQYJKoZIhvcNAQELBQADggEBAJedA5fYYPgu4V0xHHluuvsi-qfghNm6tMYbu1fz5rTBikg3uFw8Ttvkg0P01qXZsc7aiuH-1JEpIXMFjl7hy91r2sB1V8ag6NNoJboVnn-1rYza-ASGjPkOjx-K6hfAFrVcKnrUl8iU-3HXU9ebmkhLbDdtcjuZjS4dQwa_EDO1rvjMpcuyVotpJCJtIqNYq32H5KxfLgkapxV586VpCUl9cvVOBrrBw7RBO7per5TDtk80-eukGstq4oN3bTZGU3hI_uiEvd31sbpXmFTP_c66w0QFlSflbdWY-PVmcVq-QwHdGREw5rnwxkA5ElPiKYA_Ou8nS-2_3j_LvULq1nloYXV0aERhdGFYxEmWDeWIDoxodDQXD2R2YFuP5K65ooYyx5lc87qDHZdjQQAAADXLaUgej_dAOZPsCicpoVSoAEAp_xVpi-UJkDTZQypcfHnPDMBQ6NqwY55E0AZJ0NVWS5SrNuBp_1NI4kLZCN-VHWi40Q_JnFoa14ZyaHTQiAejpQECAyYgASFYIN8uOuKWpwsKnNKItUaZmgYk3VlL3huL6GiNvsKGP8bsIlggTjOQ5450jbZVi9VJ9S6jyGqda-_hvXGUpteTz8etq2Q\",\"getTransports\":{},\"clientDataJSON\":\"eyJjaGFsbGVuZ2UiOiJlSVJfODNOTHBJcGhrZVNROC1YVDFNYURhaXUycmpMVm9GOXZaUFVBTkhBIiwiZXh0cmFfa2V5c19tYXlfYmVfYWRkZWRfaGVyZSI6ImRvIG5vdCBjb21wYXJlIGNsaWVudERhdGFKU09OIGFnYWluc3QgYSB0ZW1wbGF0ZS4gU2VlIGh0dHBzOi8vZ29vLmdsL3lhYlBleCIsIm9yaWdpbiI6Imh0dHBzOi8vbG9jYWxob3N0OjgwODAiLCJ0eXBlIjoid2ViYXV0aG4uY3JlYXRlIn0\"},\"getClientExtensionResults\":{},\"id\":\"Kf8VaYvlCZA02UMqXHx5zwzAUOjasGOeRNAGSdDVVkuUqzbgaf9TSOJC2QjflR1ouNEPyZxaGteGcmh00IgHow\",\"type\":\"public-key\"}"
//
//        val creds = objectMapper.readValue<PublicKeyCredential>(testJson)
//        println(creds)
//        println(cborObjectMapper.readValue<AttestationObject>(creds.response.attestationObject))
//    }
//}
//
//data class AttestationObject(
//        val fmt: String,
//        val attStmt: AttestationStatement,
//        val authData: ByteArray
//)
//
//data class AttestationStatement(
//        val alg: String,
//        val sig: ByteArray,
//        val x5c: List<ByteArray>
//)