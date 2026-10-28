package com.mvpmodular.feature.auth.api.domain

data class Session(val userId: String, val email: String, val token: String)
