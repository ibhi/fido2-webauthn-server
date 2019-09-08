package com.noobish.webauthn.webauthnserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebauthnServerApplication

fun main(args: Array<String>) {
	runApplication<WebauthnServerApplication>(*args)
}
