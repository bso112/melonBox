package com.seoulventure.melonbox.feature.main

import androidx.lifecycle.ViewModel
import com.seoulventure.melonbox.domain.GetTutorialUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTutorialVideoUrlUseCase: GetTutorialUrlUseCase
) : ViewModel() {

    fun getTutorialUrl() = getTutorialVideoUrlUseCase.invoke()
}