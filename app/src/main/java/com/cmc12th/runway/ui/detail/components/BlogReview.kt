@file:OptIn(ExperimentalAnimationApi::class)

package com.cmc12th.runway.ui.detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.domain.model.response.store.BlogReview
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.Gray900
import com.cmc12th.runway.ui.theme.HeadLine4

@Composable
fun BlogReview(
    blogReview: List<BlogReview>,
    naviagteToWebView: (String, String) -> Unit,
    updateExpandedState: () -> Unit,
    isMoreBtnVisible: Boolean,
    isBlogReviewExapnded: Boolean
) {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "블로그 후기", style = HeadLine4, color = Color.Black)
    }
    WidthSpacerLine(height = 1.dp, color = Gray200)

    blogReview.take(5).map {
        BlogReviewItem(
            blogReview = it,
            onClick = { url, title ->
                naviagteToWebView(url, title)
            }
        )
    }
    AnimatedContent(
        targetState = isBlogReviewExapnded,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
        }
    ) {
        Column {
            blogReview.drop(5).map {
                BlogReviewItem(
                    blogReview = it,
                    onClick = { url, title ->
                        naviagteToWebView(url, title)
                    }
                )
            }
        }
    }
    if (isMoreBtnVisible) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    updateExpandedState()
                }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
        ) {
            Text(text = "더보기", color = Gray900, style = Body2)
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "IC_ARROW",
                tint = Gray900,
                modifier = Modifier
                    .size(16.dp)
                    .rotate(if (isBlogReviewExapnded) 90f else 270f)
            )
        }
    }
}
