package com.noobish.webauthn.webauthnserver.controller

import com.noobish.webauthn.webauthnserver.core.data.CredentialCreationOptions
import com.noobish.webauthn.webauthnserver.core.data.CredentialRequestOptions
import com.noobish.webauthn.webauthnserver.core.service.WebAuthenticationService
import com.noobish.webauthn.webauthnserver.data.AuthenticationRequest
import com.noobish.webauthn.webauthnserver.data.FinishAuthenticationRequest
import com.noobish.webauthn.webauthnserver.data.FinishRegistrationRequest
import com.noobish.webauthn.webauthnserver.data.RegistrationRequest
import mu.KotlinLogging
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("v1")
class RegistrationController(private val webAuthenticationService: WebAuthenticationService) {

    @PostMapping("/registrations/start")
    fun startRegistration(@RequestBody registrationRequest: RegistrationRequest): CredentialCreationOptions {
        val credentialCreationOptions = webAuthenticationService.startRegistration(registrationRequest)
        logger.info { "CredentialCreationOptions $credentialCreationOptions" }
        return credentialCreationOptions
    }

    @PostMapping("registrations/finish")
    fun finishRegistration(@RequestBody finishRegistrationRequest: FinishRegistrationRequest): String {
        return webAuthenticationService.finishRegistration(finishRegistrationRequest.publicKeyCredential, finishRegistrationRequest.requestId)
    }

    @PostMapping("authentication/start")
    fun startAuthentication(@RequestBody authenticationRequest: AuthenticationRequest): CredentialRequestOptions {
        return webAuthenticationService.startAuthentication(authenticationRequest.userName)
    }

    @PostMapping("authentication/finish")
    fun finishAuthentication(@RequestBody finishAuthenticationRequest: FinishAuthenticationRequest): String {
        return webAuthenticationService.finishAuthentication(
                finishAuthenticationRequest.publicKeyCredential,
                finishAuthenticationRequest.requestId,
                finishAuthenticationRequest.userName
        )
    }

}