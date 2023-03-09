package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R


@Composable
fun BoxScope.DetailTopBar(
    topbarColor: Color,
    topbarIconAnimateColor: Color,
    onBackPress: () -> Unit,
    isBookmarked: Boolean,
    updateBookmark: (Boolean) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(54.dp)
            .background(topbarColor)
            .align(Alignment.TopCenter),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onBackPress() }, modifier = Modifier
                .padding(start = 20.dp)
                .size(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_left_runway),
                contentDescription = "IC_LEFT_RUNWAY",
                tint = topbarIconAnimateColor
            )
        }
        Row(
            modifier = Modifier.padding(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (isBookmarked) {
                IconButton(onClick = {
                    updateBookmark(false)
                }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filled_bookmark_24),
                        contentDescription = "IC_BOOKMARKED",
                        tint = topbarIconAnimateColor
                    )
                }
            } else {
                IconButton(onClick = {
                    updateBookmark(true)
                }, modifier = Modifier.size(28.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_border_bookmark_24),
                        contentDescription = "IC_BOOKMARK",
                        tint = topbarIconAnimateColor
                    )
                }
            }

//            IconButton(onClick = {}, modifier = Modifier.size(28.dp)) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_border_share_24),
//                    contentDescription = "IC_SHARE",
//                    tint = topbarIconAnimateColor
//                )
//            }
        }
    }
}
