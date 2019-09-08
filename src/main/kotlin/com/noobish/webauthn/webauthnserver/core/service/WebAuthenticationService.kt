package com.noobish.webauthn.webauthnserver.core.service

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.noobish.webauthn.webauthnserver.core.dao.DefaultUser
import com.noobish.webauthn.webauthnserver.core.dao.UserRespository
import com.noobish.webauthn.webauthnserver.core.data.*
import com.noobish.webauthn.webauthnserver.core.data.AuthenticatorAssertionResponse
import com.noobish.webauthn.webauthnserver.core.data.AuthenticatorAttestationResponse
import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredential
import com.noobish.webauthn.webauthnserver.data.RegistrationRequest
import com.webauthn4j.authenticator.Authenticator
import com.webauthn4j.authenticator.AuthenticatorImpl
import com.webauthn4j.data.*
import com.webauthn4j.data.attestation.statement.COSEAlgorithmIdentifier
import com.webauthn4j.data.client.Origin
import com.webauthn4j.data.client.challenge.Challenge
import com.webauthn4j.data.client.challenge.DefaultChallenge
import com.webauthn4j.server.ServerProperty
import com.webauthn4j.validator.WebAuthnAuthenticationContextValidator
import com.webauthn4j.validator.WebAuthnRegistrationContextValidator
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit


private val logger = KotlinLogging.logger {}
private val random = SecureRandom()

private val BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding()
private const val RP_ID = "localhost"
private const val ORIGIN_URL = "https://localhost:8080"

@Service
class WebAuthenticationService(private val userRespository: UserRespository<String, DefaultUser>) {

    private val relyingParty: PublicKeyCredentialRpEntity = PublicKeyCredentialRpEntity(RP_ID, "My WebAuthn demo")
    private val registerRequestStorage: Cache<String, PublicKeyCredentialCreationOptions> = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build()

    private val authenticateRequestStorage: Cache<String, PublicKeyCredentialRequestOptions> = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build()

    fun startRegistration(registrationRequest: RegistrationRequest): CredentialCreationOptions {
        logger.trace("startRegistration username: {}, credentialNickname: {}", registrationRequest.userName)
        val requestId: ByteArray = createRandomId()
        val publicKeyCredentialCreationOptions = buildPublicKeyCredentialCreationOptions(registrationRequest.userName, registrationRequest.displayName)

        registerRequestStorage.put(stringifyRequestId(requestId), publicKeyCredentialCreationOptions)
        return CredentialCreationOptions(
                requestId = requestId,
                publicKey = publicKeyCredentialCreationOptions
        )
    }

    fun finishRegistration(publicKeyCredential: PublicKeyCredential<AuthenticatorAttestationResponse>, requestId: ByteArray): String {

        val requestIdString = stringifyRequestId(requestId)
        logger.info { "RequestId from client $requestIdString" }


        val publicKeyCredentialCreationOptions: PublicKeyCredentialCreationOptions =  Optional.ofNullable(registerRequestStorage.getIfPresent(requestIdString))
                .orElseThrow { RuntimeException("Invalid request Id") }

        registerRequestStorage.invalidate(requestIdString)


        // Client properties
        val clientDataJSON: ByteArray = publicKeyCredential.response.clientDataJSON /* set clientDataJSON */
        val attestationObject: ByteArray = publicKeyCredential.response.attestationObject /* set attestationObject */

        // Server properties
        val origin: Origin = Origin(ORIGIN_URL) /* set origin */
        val rpId: String = RP_ID/* set rpId */
        val challenge: Challenge = publicKeyCredentialCreationOptions.challenge /* set challenge */
        val tokenBindingId: ByteArray? = null /* set tokenBindingId */
        val serverProperty = ServerProperty(origin, rpId, challenge, tokenBindingId)
        val userVerificationRequired = false

        val registrationContext = WebAuthnRegistrationContext(clientDataJSON, attestationObject, null, serverProperty, userVerificationRequired, true, Collections.emptyList())

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

        val user = publicKeyCredentialCreationOptions.user

        val userDao = DefaultUser(userId = user.id, userName = user.name, name = user.displayName, authenticators =  setOf(authenticator))

        userRespository.save(user.name, userDao)

        logger.info { "Authenticator: $authenticator" }

        logger.debug { "Persisted user data: ${userRespository.find(user.name)}" }

        return "{ \"status\": \"ok\" }"
    }

    fun startAuthentication(userName: String): CredentialRequestOptions {
        val user = userRespository.find(userName) ?: throw RuntimeException("Invalid username")

        val publicKeyCredentialRequestOptions = buildPublicKeyCredentialRequestOptions(user.authenticators)
        val requestId: ByteArray = createRandomId()

        authenticateRequestStorage.put(stringifyRequestId(requestId), publicKeyCredentialRequestOptions)
        return CredentialRequestOptions(requestId, publicKeyCredentialRequestOptions)
    }

    fun finishAuthentication(publicKeyCredential: PublicKeyCredential<AuthenticatorAssertionResponse>, requestId: ByteArray, userName: String): String {

        val requestIdString = stringifyRequestId(requestId)

        logger.info { "RequestId from client $requestIdString" }

        val publicKeyCredentialRequestOptions: PublicKeyCredentialRequestOptions =  Optional.ofNullable(authenticateRequestStorage.getIfPresent(requestIdString))
                .orElseThrow { RuntimeException("Invalid request Id") }

        registerRequestStorage.invalidate(requestIdString)

        // Client properties
        val credentialId: ByteArray = publicKeyCredential.rawId/* set credentialId */
        val clientDataJSON: ByteArray = publicKeyCredential.response.clientDataJSON/* set clientDataJSON */
        val authenticatorData: ByteArray = publicKeyCredential.response.authenticatorData/* set authenticatorData */
        val signature: ByteArray = publicKeyCredential.response.signature/* set signature */

        // Server properties
        val origin: Origin = Origin(ORIGIN_URL) /* set origin */
        val rpId: String = RP_ID /* set rpId */
        val challenge: Challenge = publicKeyCredentialRequestOptions.challenge /* set challenge */
        val tokenBindingId: ByteArray? = null /* set tokenBindingId */
        val serverProperty = ServerProperty(origin, rpId, challenge, tokenBindingId)
        val userVerificationRequired = false

        val authenticationContext = WebAuthnAuthenticationContext(
                credentialId,
                clientDataJSON,
                authenticatorData,
                signature,
                serverProperty,
                userVerificationRequired
        )
        val authenticator: Authenticator = findAuthenticator(credentialId, userName) // please load authenticator object persisted in the registration process in your manner

        val webAuthnAuthenticationContextValidator = WebAuthnAuthenticationContextValidator()

        val response = webAuthnAuthenticationContextValidator.validate(authenticationContext, authenticator)

        // please update the counter of the authenticator record
//        updateCounter(
//                response.authenticatorData.attestedCredentialData.credentialId,
//                response.authenticatorData.signCount
//        )

        logger.info { "Response from finish authentication $response" }

        return "{ \"status\": \"ok\" }"

    }

    private fun findAuthenticator(credentialId: ByteArray, userName: String): Authenticator {
        val user: DefaultUser =  Optional.ofNullable(userRespository.find(userName))
                .orElseThrow { RuntimeException("User not found") }
        return user.authenticators
                .first { authenticator -> authenticator.attestedCredentialData.credentialId!!.contentEquals(credentialId) }

    }

    private fun buildPublicKeyCredentialRequestOptions(authenticators: Set<Authenticator>): PublicKeyCredentialRequestOptions  {

         val publicKeyCredentialDescriptors :List<PublicKeyCredentialDescriptor> = authenticators.map { authenticator ->  PublicKeyCredentialDescriptor(
                PublicKeyCredentialType.PUBLIC_KEY,
                authenticator.attestedCredentialData.credentialId,
                setOf(AuthenticatorTransport.USB, AuthenticatorTransport.NFC, AuthenticatorTransport.BLE)
        )}

        return PublicKeyCredentialRequestOptions(
                createChallenge(),
                null,
                RP_ID,
                publicKeyCredentialDescriptors,
                UserVerificationRequirement.PREFERRED,
                null
        )
    }

    private fun buildPublicKeyCredentialCreationOptions(userName: String, displayName: String): PublicKeyCredentialCreationOptions {
        return PublicKeyCredentialCreationOptions(
                relyingParty,
                PublicKeyCredentialUserEntity(
                        createRandomId(),
                        userName,
                        displayName
                ),
                createChallenge(),
                listOf(PublicKeyCredentialParameters(PublicKeyCredentialType.PUBLIC_KEY, COSEAlgorithmIdentifier.ES256))
        )
    }

    private fun createChallenge(): Challenge {
        val byteArray = ByteArray(32)
        random.nextBytes(byteArray)
        return DefaultChallenge(byteArray)
    }

    private fun createRandomId(): ByteArray {
        val byteArray = ByteArray(32)
        random.nextBytes(byteArray)
        return byteArray
    }

    private fun stringifyRequestId(requestId: ByteArray) = BASE64_URL_ENCODER.encodeToString(requestId)


}