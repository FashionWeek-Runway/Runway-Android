package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.store.BlogReview
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.theme.*

@Composable
fun BlogReviewItem(blogReview: BlogReview) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(136.dp)
                .padding(20.dp, 14.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = blogReview.title,
                    style = Body1M,
                    color = Gray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = blogReview.content,
                    style = Body2,
                    color = Gray700,
                    overflow = TextOverflow.Ellipsis
                )
            }
            WidthSpacer(width = 20.dp)
            AsyncImage(
                modifier = Modifier
                    .size(108.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(blogReview.imgUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "BLOG_IMG",
                contentScale = ContentScale.Crop,
            )
        }
        WidthSpacerLine(height = 1.dp, color = Gray200)
    }
}

