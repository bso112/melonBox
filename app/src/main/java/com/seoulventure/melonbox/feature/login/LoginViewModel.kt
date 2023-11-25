package com.seoulventure.melonbox.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seoulventure.melonbox.data.YtMusicOAuthDataSource
import com.seoulventure.melonbox.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataSource: YtMusicOAuthDataSource
) : ViewModel() {


    fun getAccessToken(
        clientId: String,
        clientSecret: String,
        authorizationCode: String
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                dataSource.getOAuthResult(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    authorizationCode = authorizationCode
                )
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}