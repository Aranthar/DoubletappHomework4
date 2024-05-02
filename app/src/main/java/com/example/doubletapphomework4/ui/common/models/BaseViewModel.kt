package com.example.doubletapphomework4.ui.common.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<State, Event>(
    initialState: State,
) : ViewModel() {
    protected val viewState = MutableStateFlow(initialState)

    fun getViewState(): StateFlow<State> = viewState

    open fun obtainEvent(viewEvent: Event) {}
}