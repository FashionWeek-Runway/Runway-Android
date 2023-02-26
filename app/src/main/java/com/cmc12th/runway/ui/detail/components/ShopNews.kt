package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.theme.*

@Composable
fun ShopNews() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "소식", modifier = Modifier.weight(1f), style = HeadLine4, color = Black)
            Text(text = "더보기", style = Body2M, color = Gray500)
            IconButton(
                modifier = Modifier.size(18.dp),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = "IC_ARROW",
                    modifier = Modifier.rotate(180f),
                    tint = Gray500
                )
            }
        }
        val shopNews = remember {
            mutableStateOf(listOf("사장님글1", "사장님글2", "사장님글3", "사장님글4"))
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { WidthSpacer(width = 8.dp) }
            items(shopNews.value) {
                Column(
                    modifier = Modifier
                        .widthIn(max = 200.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(BorderStroke(1.dp, Gray200), RoundedCornerShape(4.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "SHOP_NEW_MAIN",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.6f)
                    )
                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .heightIn(max = 80.dp)
                    ) {
                        Text(
                            text = "사장님 글 사장님 글 타이틀사장님 글 타이틀사장님 글 타이글 타이틀사장님 글 타이틀",
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(text = "MM.DD", style = Caption, color = Gray300)
                    }
                }
            }
            item { WidthSpacer(width = 20.dp) }
        }
    }
}
