package com.cmc12th.runway.ui.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus
import com.cmc12th.runway.ui.theme.Body2B
import com.cmc12th.runway.ui.theme.Gray700
import com.cmc12th.runway.ui.theme.Point

@Composable
fun ColumnScope.ComfirmButton(
    editStatus: EditUiStatus,
    event: Boolean,
    updateUploadEvent: (Boolean) -> Unit,
    updateBottomBarHeight: (Dp) -> Unit,
) {
    val localDensity = LocalDensity.current
    AnimatedVisibility(visible = !editStatus.isEdit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .onGloballyPositioned { coordinates ->
                    updateBottomBarHeight(with(localDensity) { coordinates.size.height.toDp() })
                },
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { updateUploadEvent(!event) },
                modifier = Modifier
                    .padding(12.dp, 20.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Gray700),
                colors = ButtonDefaults.buttonColors(Color(0x50242528))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "등록", style = Body2B, color = Point)
                    WidthSpacer(width = 4.dp)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_right_arrow_16),
                        contentDescription = "IC_RIGHT_ARROW",
                        modifier = Modifier.size(16.dp),
                        tint = Point
                    )
                }
            }

        }
    }
}
