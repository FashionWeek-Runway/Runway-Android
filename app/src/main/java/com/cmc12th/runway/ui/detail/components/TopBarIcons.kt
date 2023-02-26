package com.cmc12th.runway.ui.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.detail.photoreview.model.EditUiStatus
import com.cmc12th.runway.ui.theme.Body1B
import com.cmc12th.runway.ui.theme.Body2M

@Composable
fun BoxScope.TopBarIcons(
    isEdit: Boolean,
    updateEditMode: (isEdit: Boolean) -> Unit,
    addUserReviewText: () -> Unit,
    updateColorPickerVisiblity: (Boolean) -> Unit,
    editStatus: EditUiStatus,
    popBackStack: () -> Unit,
    updateTextAlign: (TextAlign) -> Unit,
) {

    /** 에디트 모드일 때 */
    AnimatedVisibility(visible = isEdit, enter = fadeIn(), exit = fadeOut()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(78.dp)
                .padding(20.dp, 14.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "취소", style = Body1B, color = Color.White, modifier = Modifier.clickable {
                updateEditMode(false)
            })
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {

                when (editStatus.textAlign) {
                    TextAlign.Start -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_left_align_24),
                            contentDescription = "IMG_COLOR_PICKER",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    updateTextAlign(TextAlign.Center)
                                },
                            tint = Color.White
                        )
                    }
                    TextAlign.Center -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_center_align_24),
                            contentDescription = "IMG_COLOR_PICKER",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    updateTextAlign(TextAlign.Right)
                                },
                            tint = Color.White
                        )
                    }
                    else -> {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_right_align_24),
                            contentDescription = "IMG_COLOR_PICKER",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    updateTextAlign(TextAlign.Start)
                                },
                            tint = Color.White
                        )
                    }
                }

                if (!editStatus.isColorPickerVisibility) {
                    Image(
                        painter = painterResource(id = R.drawable.img_color_picker_able_24),
                        contentDescription = "IMG_COLOR_PICKER_ABLE",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                updateColorPickerVisiblity(true)
                            }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.img_color_picker_disable_24),
                        contentDescription = "IMG_COLOR_PICKER_DISABLE",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                updateColorPickerVisiblity(false)
                            }
                    )
                }

            }
            Text(text = "완료", style = Body1B, color = Color.White, modifier = Modifier.clickable {
                addUserReviewText()
            })
        }
    }

    /** 에디트 모드가 아닐 때 */
    if (!isEdit) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .padding(20.dp, 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {
                    popBackStack()
                },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_left_runway),
                    contentDescription = "IC_ARROW",
                    tint = Color.White
                )
            }
            IconButton(onClick = {
                updateEditMode(true)
            }) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(31.dp))
                        .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(31.dp))
                        .padding(10.dp, 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "IC_PLUS",
                        modifier = Modifier.size(16.dp),
                        tint = Color.White
                    )
                    Text(text = "Aa", style = Body2M, color = Color.White)
                }
            }
        }
    }
}
