package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.broadcast.ComposeFileProvider
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayCheckBox
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body1
import com.cmc12th.runway.ui.theme.Caption2
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray800
import com.cmc12th.runway.ui.theme.HeadLine4
import com.cmc12th.runway.ui.theme.Primary
import com.cmc12th.runway.ui.theme.Primary20
import com.cmc12th.runway.ui.theme.White
import com.cmc12th.runway.utils.Constants
import kotlinx.coroutines.launch


@Composable
fun ShowRoomInfoInCorrect(
    showBottomSheet: (@Composable () -> Unit) -> Unit,
    hideBottomSheet: () -> Unit,
) {

    val inCorrects = listOf<String>(
        "주소가 올바르지 않아요",
        "영업 시간이 올바르지 않아요",
        "전화번호가 올바르지 않아요",
        "인스타그램이 연결되지 않아요",
        "홈페이지가 연결되지 않아요",
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        WidthSpacerLine(height = 1.dp, color = Gray200)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showBottomSheet {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding()
                                .imePadding()
                        ) {
                            HeightSpacer(height = 4.dp)
                            Text(
                                text = "어떤 정보가 올바르지 않나요?",
                                style = HeadLine4,
                                color = Color.Black,
                                modifier = Modifier.padding(20.dp, 20.dp)
                            )
                            inCorrects.forEach {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                        }
                                        .padding(20.dp, 20.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RunwayCheckBox(
                                        onChecked = {},
                                        checkState = true,
                                    )
                                    WidthSpacer(width = 12.dp)
                                    Text(
                                        text = it,
                                        style = Body1,
                                        color = Color.Black
                                    )
                                }
                            }

                            Button(
                                modifier = Modifier
                                    .padding(20.dp, 10.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(4.dp),
                                enabled = true,
                                colors = ButtonDefaults.buttonColors(if (true) Black else Gray300),
                                onClick = {
                                    hideBottomSheet()
                                }) {
                                Text(
                                    text = "완료",
                                    modifier = Modifier.padding(0.dp, 5.dp),
                                    fontSize = 16.sp,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_store_detail_info_18),
                contentDescription = "IC_STORE_DETAIL_INFO_18",
                tint = Gray300,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "정보가 올바르지 않나요? 여기를 눌러서 알려주세요!",
                style = Caption2, color = Gray800
            )
        }
    }

}