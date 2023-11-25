package com.seoulventure.melonbox.feature.login.web

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seoulventure.melonbox.BuildConfig
import com.seoulventure.melonbox.data.YtMusicOAuthDataSource
import com.seoulventure.melonbox.domain.YtMusicOAuthData
import com.seoulventure.melonbox.domain.YtMusicOAuthResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface YtMusicOAuthState {
    class Progressing(val data: YtMusicOAuthData) : YtMusicOAuthState
    class Success(val data: YtMusicOAuthResult) : YtMusicOAuthState
    object Loading : YtMusicOAuthState
    class Error(val e: Throwable) : YtMusicOAuthState
}

@HiltViewModel
class YtMusicLoginViewModel @Inject constructor(
    private val oAuthDataSource: YtMusicOAuthDataSource
) : ViewModel() {

    private val _oAuthState: MutableStateFlow<YtMusicOAuthState> =
        MutableStateFlow(YtMusicOAuthState.Loading)
    val oAuthResponse: StateFlow<YtMusicOAuthState> = _oAuthState.asStateFlow()

    fun requestAuth() {
        viewModelScope.launch {
            try {
                val authData =
                    oAuthDataSource.requestOAuth(BuildConfig.GOOGLE_OAUTH_CLIENT_ID).toDomain()
                if (authData == null) {
                    _oAuthState.update { YtMusicOAuthState.Error(IllegalStateException("oauthData must not be null")) }
                } else {
                    _oAuthState.update { YtMusicOAuthState.Progressing(authData) }
                }
            } catch (e: Exception) {
                _oAuthState.update { YtMusicOAuthState.Error(e) }
            }
        }
    }

    fun getOAuthResult(
        clientId: String,
        clientSecret: String,
        deviceCode: String
    ) {
//        viewModelScope.launch {
//            try {
//                val result = oAuthDataSource.getOAuthResult(
//                    clientId = clientId,
//                    clientSecret = clientSecret,
//                    deviceCode = deviceCode
//                ).toDomain()
//
//                if (result == null) {
//                    _oAuthState.update { YtMusicOAuthState.Error(IllegalStateException("oauthResult must not be null")) }
//                } else {
//                    _oAuthState.update { YtMusicOAuthState.Success(result) }
//                }
//            } catch (e: Exception) {
//                _oAuthState.update { YtMusicOAuthState.Error(e) }
//            }
//        }
    }
}