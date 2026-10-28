package com.mvpmodular.core.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class OutcomeTest {
    @Test
    fun successCarriesTheValue() {
        val outcome: Outcome<String, DomainError> = Outcome.Success("ok")
        val success = assertIs<Outcome.Success<String>>(outcome)
        assertEquals("ok", success.value)
    }

    @Test
    fun failureCarriesTheError() {
        val outcome: Outcome<String, DomainError> = Outcome.Failure(DomainError.Network("hors ligne"))
        val failure = assertIs<Outcome.Failure<DomainError>>(outcome)
        assertEquals("hors ligne", failure.error.message)
    }
}
