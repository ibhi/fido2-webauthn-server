package com.noobish.webauthn.webauthnserver.core.data

enum class TokenBindingStatus private constructor(val status: String){
    PRESENT("present"),
    SUPPORTED("supported");
}
