package com.seoulventure.melonbox.feature.servicedesk

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.rememberMelonBoxState
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.util.ClickableStringTag
import com.seoulventure.melonbox.util.clickable
import com.seoulventure.melonbox.util.clickableStringHandler
import com.seoulventure.melonbox.util.openEmailApp
import com.seoulventure.melonbox.util.setTextCompat
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun ServiceDeskScreen(
    appState: MelonBoxAppState = rememberMelonBoxState()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .background(MelonBoxTheme.colors.background)
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 10.dp)
            ) {
                IconButton(
                    modifier = Modifier.size(30.dp),
                    onClick = { appState.navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.action_return)
                    )
                }
            }
        }
    ) { padding ->
        ServiceDeskContent(
            modifier = Modifier.padding(padding),
            serviceDeskItems = serviceDeskItems,
            onFailToOpenEmail = { error, email ->
                clipboardManager.setTextCompat(context, AnnotatedString(email))
                error.printStackTrace()
            }
        )
    }
}

@Composable
fun ServiceDeskContent(
    modifier: Modifier = Modifier,
    serviceDeskItems: ImmutableList<ServiceDeskItemData>,
    onFailToOpenEmail: (Throwable, email: String) -> Unit,
) {
    LazyColumn(
        modifier
            .fillMaxSize()
            .background(MelonBoxTheme.colors.background),
    ) {
        items(serviceDeskItems) {
            ServiceDeskItem(it, onFailToOpenEmail)
        }
    }
}

@Composable
fun ServiceDeskItem(
    item: ServiceDeskItemData,
    onFailToOpenEmail: (Throwable, email: String) -> Unit,
) {
    val context = LocalContext.current

    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(MelonBoxTheme.colors.iconGray)
                .padding(10.dp),
            text = "Q. ${item.question}",
            fontWeight = FontWeight.Bold
        )
        ClickableText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            text = item.answer,
            onClick = item.answer.clickableStringHandler { tag, annotation ->
                when (tag) {
                    ClickableStringTag.Email -> {
                        context.openEmailApp(
                            recipients = arrayOf(annotation),
                            titleOfEmail = "멜론박스 앱 문의사항이 있습니다."
                        ).onFailure {
                            onFailToOpenEmail(it, annotation)
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun ServiceDeskScreenPreview() {
    MelonBoxTheme {
        ServiceDeskScreen()
    }
}

private val serviceDeskItems = persistentListOf(
    ServiceDeskItemData(
        question = "한번도 사용하지 않았는데 사용량이 초과되었다고 해요",
        answer = buildAnnotatedString {
            append("멜론박스는 모든 유저가 할당량을 공유해요. 현재는 기술적인 문제로 대략 하루에 ")
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append("최대 100곡")
            }
            append(" 정도를 변환할 수 있어요")
        }
    ),
    ServiceDeskItemData(
        question = "그 외 문의사항",
        answer = buildAnnotatedString {
            append("문의사항은 ")
            clickable(ClickableStringTag.Email, "bso11246@gmail.com") { annotation ->
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(annotation)
                }
            }
            append(" 으로 메일을 보내주세요! 최대한 빠른 시일내에 답변드릴게요.")
        }
    )
)