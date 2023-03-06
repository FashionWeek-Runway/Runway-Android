package com.cmc12th.runway.ui.detail.photoreview.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.detail.photoreview.ReviewViewModel
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.signin.components.OnBoardHeadLine
import com.cmc12th.runway.ui.theme.*

@Composable
fun ReviewReportScreen(appState: ApplicationState, reivewId: Int) {

    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val uiState by reviewViewModel.reportUiState.collectAsStateWithLifecycle()

    val reportReview: () -> Unit = {
        reviewViewModel.reporteReview(reviewId = reivewId) {
            appState.showSnackbar("신고가 완료됐습니다.\n더 나은 서비스를 위해 노력하겠습니다.")
            appState.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding(),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackIcon {
            }
            Text(text = "사용자 후기 신고", style = Body1B, color = Color.Black)
        }
        Column(
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Column {
                HeightSpacer(height = 20.dp)
                Text(text = "신고 사유를 골라주세요", style = HeadLine3)
                HeightSpacer(height = 8.dp)
                uiState.reports.forEach {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            reviewViewModel.updateSelectedId(it.id)
                        }) {
                        Row(
                            modifier = Modifier
                                .padding(0.dp, 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .border(
                                        if (uiState.selectedReportId == it.id) BorderStroke(
                                            5.dp,
                                            Primary
                                        ) else BorderStroke(1.dp, Gray300), CircleShape
                                    )
                            )
                            Text(text = it.contents, style = Body1, color = Black)
                        }
                    }

                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OnBoardHeadLine("추가 의견이 있다면 적어주세요", sub = "")

                HeightSpacer(height = 16.dp)
                BasicTextField(
                    value = uiState.reportContents,
                    onValueChange = {
                        if (it.length <= 100) reviewViewModel.updateReportContents(it)
                    },
                    textStyle = Body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(4.dp)),
                    decorationBox = { innerTextField ->
                        Box(
                            Modifier
                                .weight(1f)
                                .padding(14.dp)
                        ) {
                            if (uiState.reportContents.isEmpty()) {
                                Text(
                                    "100자 이내로 작성해주세요.",
                                    style = Body1,
                                    color = Gray300
                                )
                            }
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        reportReview()
                    }),
                )
                HeightSpacer(height = 8.dp)
                Text(
                    text = "신고자 정보는 익명으로 처리되며, 신고한 사용자 후기는 검토 후 임시조치 될 예정입니다.",
                    style = Body2,
                    color = Gray500
                )
            }

        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 10.dp),
            shape = RoundedCornerShape(4.dp),
            enabled = uiState.selectedReportId != -1,
            colors = ButtonDefaults.buttonColors(if (uiState.selectedReportId != -1) Color.Black else Gray300),
            onClick = {
                reportReview()
            }) {
            Text(
                modifier = Modifier.padding(0.dp, 5.dp),
                text = "신고 하기",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

