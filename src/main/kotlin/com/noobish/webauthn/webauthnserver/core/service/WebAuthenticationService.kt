package com.noobish.webauthn.webauthnserver.core.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.noobish.webauthn.webauthnserver.core.ChallengeImpl
import com.noobish.webauthn.webauthnserver.core.data.*
import com.noobish.webauthn.webauthnserver.core.data.ByteArray
import com.noobish.webauthn.webauthnserver.data.RegistrationRequest
import com.webauthn4j.authenticator.Authenticator
import com.webauthn4j.data.client.Origin
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.data.client.challenge.Challenge
import com.webauthn4j.data.WebAuthnRegistrationContext
import java.lang.RuntimeException
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.validator.WebAuthnRegistrationContextValidationResponse
import com.webauthn4j.validator.WebAuthnRegistrationContextValidator




private val logger = KotlinLogging.logger {}
private val random = SecureRandom()

private val BASE64_URL_ENCODER = Base64.getUrlEncoder()
private val BASE64_URL_DECODER = Base64.getUrlDecoder()
private const val RP_ID = "localhost"
private const val ORIGIN_URL = "https://localhost:8080"

@Service
class WebAuthenticationService(private val objectMapper: ObjectMapper) {

    private val relyingParty: PublicKeyCredentialRpEntity = PublicKeyCredentialRpEntity(id = RP_ID, name = "My WebAuthn demo")
    private val registerRequestStorage: Cache<String, CredentialCreationOptions> = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    fun startRegistration(registrationRequest: RegistrationRequest): CredentialCreationOptions {
        logger.trace("startRegistration username: {}, credentialNickname: {}", registrationRequest.userName)
        val requestId: String = createRandomId().base64Url
        val credentialCreationOptions = CredentialCreationOptions(
                requestId = requestId,
                publicKey = buildPublicKeyCredentialCreationOptions(registrationRequest.userName, registrationRequest.displayName)
        )
        registerRequestStorage.put(requestId, credentialCreationOptions)
        return credentialCreationOptions
    }

    fun finishRegistration(publicKeyCredential: PublicKeyCredential, requestId: String): String {

        logger.info { "RequestId from client $requestId" }

        val credentialCreationOptions: CredentialCreationOptions? = registerRequestStorage.getIfPresent(requestId)
        val originalChallenge: kotlin.ByteArray? = credentialCreationOptions?.publicKey?.challenge?.getBytes()
        registerRequestStorage.invalidate(requestId)

        if(originalChallenge == null) {
            throw RuntimeException("Incorrect requestId and hence challenge isnt available")
        }

        // Client properties
        val clientDataJSON: kotlin.ByteArray = publicKeyCredential.response.clientDataJSON.getBytes()/* set clientDataJSON */
        val attestationObject: kotlin.ByteArray = publicKeyCredential.response.attestationObject.getBytes()/* set attestationObject */

        // Server properties
        val origin: Origin = Origin(ORIGIN_URL)/* set origin */
        val rpId: String = RP_ID/* set rpId */
        val challenge: Challenge? = ChallengeImpl(originalChallenge)/* set challenge */
        val tokenBindingId: kotlin.ByteArray? = null /* set tokenBindingId */
        val serverProperty = ServerProperty(origin, rpId, challenge, tokenBindingId)
        val userVerificationRequired = false

        val registrationContext = WebAuthnRegistrationContext(clientDataJSON, attestationObject, serverProperty, userVerificationRequired)

        // WebAuthnRegistrationContextValidator.createNonStrictRegistrationContextValidator() returns a WebAuthnRegistrationContextValidator instance
        // which doesn't validate an attestation statement. It is recommended configuration for most web application.
        // If you are building enterprise web application and need to validate the attestation statement, use the constructor of
        // WebAuthnRegistrationContextValidator and provide validators you like
        val webAuthnRegistrationContextValidator = WebAuthnRegistrationContextValidator.createNonStrictRegistrationContextValidator()


        val response = webAuthnRegistrationContextValidator.validate(registrationContext)

// please persist Authenticator object, which will be used in the authentication process.
        val authenticator = AuthenticatorImpl( // You may create your own Authenticator implementation to save friendly authenticator name
                response.attestationObject.authenticatorData.attestedCredentialData,
                response.attestationObject.attestationStatement,
                response.attestationObject.authenticatorData.signCount
        )

        logger.info { "Authenticator: $authenticator" }
//        return authenticator // please persist authenticator in your manner

//        logger.info { "CLient data JSON: ${String(publicKeyCredential.response.clientDataJSON.getBytes())}" }
//        val clientData = objectMapper.readValue<CollectedClientData>(String(publicKeyCredential.response.clientDataJSON.getBytes()))
//        logger.info { "CollectedCLientData: $clientData" }
//
//        val originalChallenge: ByteArray? = registerRequestStorage.getIfPresent(requestId)?.publicKey?.challenge
//        registerRequestStorage.invalidate(requestId)
//
//        val actualChallenge = clientData.challenge
//        val actualOrigin = clientData.origin
//
//        logger.info { "ClientData: $clientData, actualOrigin: $actualOrigin, originalOrigin: $RP_ID" }
//
//        if(originalChallenge != null && (originalChallenge > actualChallenge)) {
//            throw RuntimeException("Challenge doesn't match")
//        }
//
//        if(actualOrigin != "https://$RP_ID:8080") {
//            throw RuntimeException("Origin doesn't match")
//        }
//
//        if(clientData.type != CollectedClientDataType.WEBAUTHN_CREATE.toJsonString()) {
//            throw RuntimeException("Type doesn't match")
//        }

        return "{ \"key\": \"Success\" }"
    }

    private fun buildPublicKeyCredentialCreationOptions(userName: String, displayName: String): PublicKeyCredentialCreationOptions {
        return PublicKeyCredentialCreationOptions(
                challenge = createChallenge(),
                rp = relyingParty,
                user = PublicKeyCredentialUserEntity(
                        id = createRandomId(),
                        name = userName,
                        displayName = displayName
                ),
                attestation = AttestationConveyancePreference.DIRECT,
                pubKeyCredParams = listOf(PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256))
        )

    }

    private fun createChallenge(): ByteArray {
        val byteArray = ByteArray(32)
        random.nextBytes(byteArray)
        return ByteArray(byteArray)
    }

    private fun createRandomId(): ByteArray {
        return createChallenge()
    }


}