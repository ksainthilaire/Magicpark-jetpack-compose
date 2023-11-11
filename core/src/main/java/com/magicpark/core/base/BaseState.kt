package com.magicpark.core.base

import kotlinx.coroutines.flow.MutableStateFlow

interface BaseState
interface BaseViewModel<M> {
    val state: MutableStateFlow<M>
}
