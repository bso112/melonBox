package com.seoulventure.melonbox.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seoulventure.melonbox.data.YtMusicOAuthDataSource
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
        authorizationCode: String,
        onSuccess: (String) -> Unit,
    ) {
        viewModelScope.launch {
            kotlin.runCatching {
                dataSource.getOAuthResult(
                    clientId = clientId,
                    clientSecret = clientSecret,
                    authorizationCode = authorizationCode
                )
            }.onSuccess {
                onSuccess(it.accessToken ?: error("accessToken is null"))
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}