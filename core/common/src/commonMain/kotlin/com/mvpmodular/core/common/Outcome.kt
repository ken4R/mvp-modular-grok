package com.mvpmodular.core.common

sealed interface Outcome<out T, out E> {
    data class Success<T>(val value: T) : Outcome<T, Nothing>

    data class Failure<E>(val error: E) : Outcome<Nothing, E>
}
