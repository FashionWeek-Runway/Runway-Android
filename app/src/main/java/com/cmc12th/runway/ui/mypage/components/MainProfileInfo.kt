package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.HeadLine3
import com.cmc12th.runway.ui.theme.HeadLine4M
import com.cmc12th.runway.ui.theme.Primary

@Composable
fun MainProfileInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(60.dp)) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.img_dummy)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "IMG_PROFILE",
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Primary), CircleShape)
                    .size(20.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(12.dp),
                    painter = painterResource(id = R.drawable.ic_filled_pencil_12),
                    contentDescription = "IC_PENCIL",
                    tint = Primary,
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "안녕하세요", style = HeadLine4M, color = Black)
            Text(text = "나패피님", style = HeadLine3, color = Black)
        }

    }
}
