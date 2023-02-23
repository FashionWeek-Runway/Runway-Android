package com.cmc12th.runway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cmc12th.runway.ui.domain.model.DialogButtonContent
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Button1
import com.cmc12th.runway.ui.theme.HeadLine4

/**
 * @param title 타이틀이 필요 없다면 공백으로 보낼 것
 * 확인버튼과 아니요 버튼이 무조건 있음
 */
@Composable
fun RunwayHorizontalDialog(
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit,
    title: String = "",
    descrption: String,
    positiveButton: DialogButtonContent,
    negativeButton: DialogButtonContent,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeightSpacer(height = 10.dp)

            if (title.isNotBlank()) {
                Text(
                    text = title, style = HeadLine4, color = Color.Black,
                    modifier = Modifier.padding(top = 14.dp, bottom = 10.dp)
                )
            }
            Text(
                modifier = Modifier.padding(20.dp, 0.dp),
                text = descrption,
                textAlign = TextAlign.Center,
                style = Body2,
                color = Color.Black
            )
            HeightSpacer(height = 8.dp)

            Row(
                modifier = Modifier
                    .padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { negativeButton.onClick() },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = negativeButton.title,
                        color = Color.Black,
                        style = Button1,
                        modifier = Modifier.padding(0.dp, 5.dp)
                    )
                }

                /** 동의 버튼 */
                Button(
                    modifier = Modifier
                        .weight(1f),
                    onClick = { positiveButton.onClick() },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text(
                        text = positiveButton.title,
                        color = Color.White,
                        style = Button1,
                        modifier = Modifier.padding(0.dp, 5.dp)
                    )
                }

            }

        }
    }
}

