package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.mypage.model.MypageBookmarkTabInfo
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Body2B

@Preview
@Composable
fun MypageBookmarkRowTab(
    selectedPage: MypageBookmarkTabInfo = MypageBookmarkTabInfo.STORE,
    updateSelectedPage: (MypageBookmarkTabInfo) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(70.dp, 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0x50DBDBE2)),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.dp, top = 2.dp, bottom = 2.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .clickable {
                        updateSelectedPage(MypageBookmarkTabInfo.STORE)
                    }
                    .background(if (selectedPage == MypageBookmarkTabInfo.STORE) Color.White else Color.Transparent)
            ) {
                Text(
                    text = "매장",
                    style = if (selectedPage == MypageBookmarkTabInfo.STORE) Body2B else Body2,
                    modifier = Modifier
                        .padding(0.dp, 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Black
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(4.dp))
                    .padding(end = 2.dp, top = 2.dp, bottom = 2.dp)
                    .clickable {
                        updateSelectedPage(MypageBookmarkTabInfo.REVIEW)
                    }
                    .background(if (selectedPage == MypageBookmarkTabInfo.REVIEW) Color.White else Color.Transparent)
            ) {
                Text(
                    text = "사용자 후기",
                    style = if (selectedPage == MypageBookmarkTabInfo.REVIEW) Body2B else Body2,
                    modifier = Modifier
                        .padding(0.dp, 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Black
                )
            }
        }
    }
}
