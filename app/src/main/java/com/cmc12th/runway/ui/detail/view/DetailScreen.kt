package com.cmc12th.runway.ui.detail

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.DotLine
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.map.components.BottomGradient
import com.cmc12th.runway.ui.map.components.TopGradient
import com.cmc12th.runway.ui.theme.*

@Composable
fun DetailScreen() {

    LazyColumn(modifier = Modifier
        .fillMaxSize()
    ) {
        item {
            ShowRoomBanner()
            ShowRoomDetail()
            DotLine()
            UserReview()
            HeightSpacer(height = 10.dp)
            DotLine()
            HeightSpacer(height = 10.dp)
            BlogReview()
        }
        items((0..3).toList()) {
            BlogReviewItem()
        }
    }
}

@Composable
fun BlogReview() {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(text = "블로그 후기", style = Body1M, color = Color.Black)
    }
}

@Composable
fun BlogReviewItem() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp, 10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Image(painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "IMG_DUMMY",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2.32f),
            contentScale = ContentScale.Crop)
        Text(text = "무신사 쇼핑 나들이 (무신사 스튜디오 성수  / 무신사 스탠다드 홍대)", style = Body2M)
    }
}

@Composable
fun UserReview() {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "사용자 후기", style = Body1M, color = Color.Black)
            Text(text = "나도 참여", style = Body2, textDecoration = TextDecoration.Underline)
        }

        LazyRow(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            item { WidthSpacer(width = 15.dp) }
            items((0..9).toList()) {
                Image(
                    painter = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_USER_REVIEW",
                    modifier = Modifier.size(132.dp, 200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            item { WidthSpacer(width = 15.dp) }
        }
    }
}


@Composable
private fun ShowRoomDetail() {
    Box(modifier = Modifier.padding(20.dp)) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                RunwayIconButton(drawable = R.drawable.ic_search_location) {

                }
                WidthSpacer(width = 10.dp)
                Text(text = "서울 특별시 겅동구 아찬사로 13길 122")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                RunwayIconButton(drawable = R.drawable.ic_search_location) {

                }
                WidthSpacer(width = 10.dp)
                Text(text = "서울 특별시 겅동구 아찬사로 13길 122")
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                RunwayIconButton(drawable = R.drawable.ic_search_location) {

                }
                WidthSpacer(width = 10.dp)
                Text(text = "서울 특별시 겅동구 아찬사로 13길 122")
            }
        }
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Text(text = "북마크")
        }
    }
}

@Composable
private fun ShowRoomBanner() {
    Box(modifier = Modifier.fillMaxWidth()) {
        Icon(
            painter = painterResource(id = R.drawable.ic_close_fill_circle),
            contentDescription = "IC_CLOSE_FILL_CIRCLE",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp)
                .size(32.dp),
            tint = Color.Unspecified
        )
        Image(painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "SHOP_IMAGE",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
        Column(modifier = Modifier
            .padding(20.dp)
            .align(Alignment.BottomStart),
            verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // TODO 글꼴 미적용
            Box(modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .background(Color.White)) {
                Text(text = "미니멀", modifier = Modifier
                    .padding(10.dp, 6.dp),
                    style = Body2)
            }
            Text(text = "무신사 스텐다드", color = Color.White, fontSize = 26.sp)
        }
        TopGradient(modifier = Modifier.align(Alignment.BottomCenter), height = 30.dp, alpha = 0.8f)
        BottomGradient(modifier = Modifier.align(Alignment.TopCenter),
            height = 30.dp,
            alpha = 0.8f)
    }
}


@Preview
@Composable
fun DetailScreenPreview() {
    DetailScreen()
}