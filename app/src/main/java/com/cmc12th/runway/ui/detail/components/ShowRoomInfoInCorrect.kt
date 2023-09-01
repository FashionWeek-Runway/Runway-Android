package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayCheckBox
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.detail.model.InCorrectInfo
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body1
import com.cmc12th.runway.ui.theme.Caption2
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray800
import com.cmc12th.runway.ui.theme.HeadLine4


@Composable
fun ShowRoomInfoInCorrect(
    showBottomSheet: (@Composable () -> Unit) -> Unit,
    hideBottomSheet: () -> Unit,
    reportStore: (List<Int>) -> Unit,
) {
    var inCorrectInfos by remember {
        mutableStateOf<Map<InCorrectInfo, Boolean>>(
            InCorrectInfo.default.map {
                it to false
            }.toMap()
        )
    }

    val updateInCorrectInfo: (InCorrectInfo, Boolean) -> Unit = { name, checked ->
        inCorrectInfos = inCorrectInfos.toMutableMap().apply {
            this[name] = checked
        }
    }

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
                        InCorrectBottomSheetContents(
                            inCorrectInfos = inCorrectInfos,
                            updateInCorrectInfo = updateInCorrectInfo,
                            hideBottomSheet = hideBottomSheet,
                            reportStore = reportStore
                        )
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

@Composable
private fun InCorrectBottomSheetContents(
    inCorrectInfos: Map<InCorrectInfo, Boolean>,
    updateInCorrectInfo: (InCorrectInfo, Boolean) -> Unit,
    hideBottomSheet: () -> Unit,
    reportStore: (List<Int>) -> Unit,
) {
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
        inCorrectInfos.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        updateInCorrectInfo(it.key, !it.value)
                    }
                    .padding(20.dp, 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RunwayCheckBox(
                    onChecked = {
                        updateInCorrectInfo(it.key, !it.value)
                    },
                    checkState = it.value,
                )
                WidthSpacer(width = 12.dp)
                Text(
                    text = it.key.name,
                    style = Body1,
                    color = Color.Black
                )
            }
        }

        val enabled = inCorrectInfos.count { it.value } >= 1
        Button(
            modifier = Modifier
                .padding(20.dp, 10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(if (enabled) Black else Gray300),
            onClick = {
                reportStore(inCorrectInfos.filter { it.value }.map { it.key.id })
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