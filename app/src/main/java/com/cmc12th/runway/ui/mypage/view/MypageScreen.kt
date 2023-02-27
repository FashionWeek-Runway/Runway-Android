package com.cmc12th.runway.ui.mypage.view

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.mypage.MypageViewModel
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH

@Composable
fun MypageScreen(appState: ApplicationState) {

    val mypageViewModel: MypageViewModel = hiltViewModel()
    val logout = {
        mypageViewModel.logout {
            appState.navController.navigate(LOGIN_GRAPH) {
                popUpTo(MAIN_GRAPH) {
                    inclusive = true
                }
            }
        }
    }
    appState.systmeUiController.setStatusBarColor(Gray50)

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(Gray50)
            .fillMaxSize()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "MY", style = HeadLine4, color = Color.Black)
            RunwayIconButton(drawable = R.drawable.ic_baseline_setting_24, tint = Black) {

            }
        }
        HeightSpacer(height = 12.dp)
        val localDensity = LocalDensity.current

        Box(modifier = Modifier.padding(20.dp, 0.dp)) {
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                with(localDensity) {
                    drawPath(
                        path = Path().apply {
                            moveTo(25.dp.toPx(), 0f)
                            lineTo(size.width - 25.dp.toPx(), 0f)
                            lineTo(size.width, 25.dp.toPx())
                            lineTo(size.width, 95.dp.toPx())
                            lineTo(size.width - 18.dp.toPx(), 120.dp.toPx())
                            lineTo(size.width, 138.dp.toPx())
                            lineTo(size.width, size.height)
                            lineTo(0f, size.height)
                            lineTo(0f, 138.dp.toPx())
                            lineTo(18.dp.toPx(), 120.dp.toPx())
                            lineTo(0f, 95.dp.toPx())
                            lineTo(0f, 25.dp.toPx())
                            close()
                        },
                        color = Color.White)
                    drawLine(
                        color = Gray200,
                        start = Offset(25.dp.toPx(), 0f),
                        end = Offset(size.width - 25.dp.toPx(), 0f),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width - 25.dp.toPx(), 0f),
                        end = Offset(size.width, 25.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width, 25.dp.toPx()),
                        end = Offset(size.width, 95.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width, 95.dp.toPx()),
                        end = Offset(size.width - 18.dp.toPx(), 120.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width - 18.dp.toPx(), 120.dp.toPx()),
                        end = Offset(size.width, 138.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width, 138.dp.toPx()),
                        end = Offset(size.width, size.height),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width, size.height),
                        end = Offset(0f, size.height),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(0f, size.height),
                        end = Offset(0f, 138.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(0f, 138.dp.toPx()),
                        end = Offset(18.dp.toPx(), 120.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(18.dp.toPx(), 120.dp.toPx()),
                        end = Offset(0f, 95.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(0f, 95.dp.toPx()),
                        end = Offset(0f, 25.dp.toPx()),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(0f, 25.dp.toPx()),
                        end = Offset(25.dp.toPx(), 0f),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(0f, 25.dp.toPx()),
                        end = Offset(25.dp.toPx(), 0f),
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(18.dp.toPx(), 120.dp.toPx()),
                        end = Offset(size.width - 18.dp.toPx(), 120.dp.toPx()),
                        strokeWidth = 0.5.dp.toPx()
                    )
                    drawLine(
                        color = Gray200,
                        start = Offset(size.width - 70.dp.toPx(), 120.dp.toPx()),
                        end = Offset(size.width - 70.dp.toPx(), size.height),
                        strokeWidth = 0.5.dp.toPx()
                    )
                }
            }
            Text(text = "나패피",
                style = HeadLine4,
                color = Black,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp, 21.dp))
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = -12.dp, y = -12.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_filled_edit_pencil_24),
                    contentDescription = "IC_PENCIL",
                    tint = Gray500,
                    modifier = Modifier
                )
            }

            Box(modifier = Modifier
                .offset(y = 12.dp)
                .align(Alignment.TopCenter)) {
                Image(painter = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_PROFILE",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(BorderStroke(1.dp, Gray200), CircleShape),
                    contentScale = ContentScale.Crop)
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "포토 리뷰")
            Text(text = "저장한 목록")
        }
        LazyColumn() {
        }

    }
}

fun Dp.add(mdp: Dp) = (this.value + mdp.value).dp
fun Float.addDptoDp(mdp: Dp) = (this.dp.value + mdp.value).dp
fun Float.addDptoFloat(mdp: Dp) = (this.dp.value + mdp.value).dp.value
fun Float.minusDptoFloat(mdp: Dp) = (this.dp.value - mdp.value).dp.value

