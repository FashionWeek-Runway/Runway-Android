package com.cmc12th.runway.ui.detail.components

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.store.UserReview
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.BottomSheetContent
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.theme.*
import kotlinx.coroutines.flow.Flow

@Composable
fun UserReview(
    userReviews: Flow<PagingData<UserReview>>,
    showBottomSheet: (BottomSheetContent) -> Unit,
    cameraLauncher: ManagedActivityResultLauncher<Void?, Bitmap?>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
) {
    val userReviewsPaging = userReviews.collectAsLazyPagingItems()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "사용자 후기", style = HeadLine4, color = Color.Black)
            Row(
                modifier = Modifier.clickable {
                    showBottomSheet(
                        BottomSheetContent(
                            title = "",
                            itemList = listOf(
                                BottomSheetContentItem(
                                    itemName = "사진 찍기",
                                    onItemClick = {
                                        cameraLauncher.launch()
                                    },
                                ),
                                BottomSheetContentItem(
                                    itemName = "사진 앨범",
                                    onItemClick = {
                                        galleryLauncher.launch("image/*")
                                    },
                                )
                            )
                        )
                    )
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RunwayIconButton(drawable = R.drawable.ic_filled_camera_24, size = 24.dp)
                Text(
                    text = "후기 작성",
                    style = Body1M,
                    color = Primary
                )
            }
        }
        if (userReviewsPaging.itemCount == 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_DUMMY",
                    modifier = Modifier.size(100.dp)
                )
                HeightSpacer(height = 30.dp)
                Text(text = "아직 등록된 후기가 없습니다.", style = Body1, color = Color.Black)
                HeightSpacer(height = 5.dp)
                Text(text = "매장에 방문했다면 후기를 남겨보세요 :)", style = Body2, color = Gray500)
            }
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item { WidthSpacer(width = 15.dp) }

            items(userReviewsPaging) {
                it?.let {
                    AsyncImage(
                        modifier = Modifier.size(132.dp, 200.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.imgUrl)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.img_dummy),
                        error = painterResource(id = R.drawable.img_dummy),
                        contentDescription = "ASDas",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            item { WidthSpacer(width = 15.dp) }
        }
    }
}