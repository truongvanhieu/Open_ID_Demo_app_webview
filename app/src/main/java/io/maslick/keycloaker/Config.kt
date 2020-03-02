package io.maslick.keycloaker

object Config {
    const val clientId = "demo"
    const val baseUrl = "https://accounts.smartcodes.vn"
    const val authenticationCodeUrl = "$baseUrl/auth/realms/smartcodes/protocol/openid-connect/auth"
    const val redirectUri = "maslick://oauthresponse"
    const val clientsecret = "66dce544-1619-4fe5-bf59-27a57c399880"
}