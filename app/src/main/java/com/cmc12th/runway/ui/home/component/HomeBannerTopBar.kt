package com.cmc12th.runway.ui.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.theme.Body2M
import com.cmc12th.runway.ui.theme.HeadLine4
import com.cmc12th.runway.ui.theme.White


@Composable
fun BoxScope.HomeBannerTopBar(nickname: String) {
    Box(
        Modifier
            .statusBarsPadding()
            .align(Alignment.TopCenter)
            .padding(20.dp, top = 10.dp, 20.dp, 20.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
        ) {
            RunwayIconButton(
                drawable = R.drawable.ic_baseline_filter_24,
                tint = Color.White
            ) {
                // TODO 필터 설정 뷰로 넘기기
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(text = "${nickname}님의\n취향을 가득 담은 매장", style = HeadLine4, color = White)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "전체보기", style = Body2M, color = White)
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "IC_ARROW",
                    modifier = Modifier
                        .rotate(180f)
                        .size(12.dp),
                    tint = Color.White
                )
            }
        }
    }
}