package com.mvpmodular.core.common

sealed interface DomainError {
    val message: String

    data class Validation(override val message: String) : DomainError

    data class Unauthorized(override val message: String) : DomainError

    data class NotFound(override val message: String) : DomainError

    data class Network(override val message: String) : DomainError

    data class Unknown(override val message: String) : DomainError
}
