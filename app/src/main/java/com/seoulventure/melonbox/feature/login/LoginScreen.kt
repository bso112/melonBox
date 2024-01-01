package com.seoulventure.melonbox.feature.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.navOptions
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.BuildConfig
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.data.OAuthManager
import com.seoulventure.melonbox.feature.login.google.registerForGoogleLoginResult
import com.seoulventure.melonbox.feature.main.navigateMain
import com.seoulventure.melonbox.logD
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.color_background


@Composable
fun LoginScreen(
    appState: MelonBoxAppState,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val googleLoginLauncher = registerForGoogleLoginResult(
        onSuccess = { authCode ->
            viewModel.getAccessToken(
                clientId = BuildConfig.GOOGLE_OAUTH_CLIENT_ID,
                clientSecret = BuildConfig.GOOGLE_OAUTH_CLIENT_SECRET,
                authorizationCode = authCode
            ) { accessToken ->
                OAuthManager.accessToken = accessToken
                navigateMain(appState)
            }
        },
        onFailure = {
            logD(it.message.orEmpty())
            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
        }
    )
    LoginContent(
        onClickGoogleLogin = {
            googleLoginLauncher.launch(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
        },
        onClickAppleLogin = {

        }
    )
}

private fun navigateMain(appState: MelonBoxAppState) {
    appState.navController.navigateMain(navOptions { popUpTo(appState.navController.graph.id) })
}

@Composable
fun LoginContent(
    onClickGoogleLogin: Action,
    onClickAppleLogin: Action,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color_background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Image(painter = painterResource(id = R.drawable.melon), contentDescription = null)
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = stringResource(id = R.string.msg_welcome),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                )
            )
        }
        Spacer(modifier = Modifier.size(76.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
        ) {
            Button(
                contentPadding = PaddingValues(vertical = 10.dp),
                onClick = onClickGoogleLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Image(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(40.dp)
                            .align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.action_login_google),
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.size(26.dp))
            Button(
                contentPadding = PaddingValues(vertical = 10.dp),
                onClick = onClickAppleLogin,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                ),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Image(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .size(40.dp)
                            .padding(10.dp)
                            .align(Alignment.CenterStart),
                        painter = painterResource(id = R.drawable.apple),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.action_login_apple),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MelonBoxTheme {
        LoginContent(
            onClickGoogleLogin = {},
            onClickAppleLogin = {}
        )
    }
}