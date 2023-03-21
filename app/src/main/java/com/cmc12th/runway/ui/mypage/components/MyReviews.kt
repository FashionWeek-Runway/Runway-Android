@file:OptIn(ExperimentalGlideComposeApi::class)

package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.user.MyReviewsItem
import com.cmc12th.runway.ui.theme.Caption
import com.cmc12th.runway.ui.theme.Gray100
import com.cmc12th.runway.ui.theme.Gray200

@Composable
fun ColumnScope.MyReviews(
    navigateToUserReviewDetail: (index: Int) -> Unit,
    myReviews: LazyPagingItems<MyReviewsItem>,
) {
    if (myReviews.loadState.refresh is LoadState.Loading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            items(myReviews.itemCount) { index ->
                Box(modifier = Modifier
                    .weight(1f)
                    .clickable {
                        navigateToUserReviewDetail(myReviews[index]?.reviewId ?: 0)
                    }) {
                    GlideImage(
                        modifier = Modifier
                            .background(Gray200)
                            .aspectRatio(0.65f)
                            .fillMaxSize(),
                        model = myReviews[index]?.imgUrl,
                        contentDescription = "IMG_PROFILE",
                        contentScale = ContentScale.Crop,
                    ) {
                        it.placeholder(R.color.gray200)
                            .signature(ObjectKey(myReviews[index]?.imgUrl ?: -1))
                    }
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filled_review_location_16),
                            contentDescription = "IC_LOCATION",
                            modifier = Modifier.size(14.dp),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = myReviews[index]?.regionInfo ?: "",
                            style = Caption,
                            color = Gray100
                        )
                    }
                }
            }
        }
    }
}

