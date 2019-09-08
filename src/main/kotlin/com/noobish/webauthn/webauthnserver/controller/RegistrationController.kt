package com.noobish.webauthn.webauthnserver.controller

import com.noobish.webauthn.webauthnserver.core.data.CredentialCreationOptions
import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredential
import com.noobish.webauthn.webauthnserver.core.data.PublicKeyCredentialCreationOptions
import com.noobish.webauthn.webauthnserver.core.service.WebAuthenticationService
import com.noobish.webauthn.webauthnserver.data.FinishRegistrationRequest
import com.noobish.webauthn.webauthnserver.data.RegistrationRequest
import com.webauthn4j.authenticator.Authenticator
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

}