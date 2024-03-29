@file:OptIn(ExperimentalGlideComposeApi::class, ExperimentalGlideComposeApi::class)

package com.cmc12th.runway.ui.detail.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.cmc12th.domain.model.response.store.UserReview
import com.cmc12th.runway.R
import com.cmc12th.runway.broadcast.ComposeFileProvider
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.domain.model.BottomSheetContentItem
import com.cmc12th.runway.ui.theme.*
import kotlinx.coroutines.flow.Flow

@Composable
fun UserReview(
    userReviews: Flow<PagingData<UserReview>>,
    showBottomSheet: (contents: @Composable () -> Unit) -> Unit,
    navigateToUserReviewDetail: (UserReview) -> Unit,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    updateImageUri: (Uri?) -> Unit,
    hideBottomSheet: () -> Unit,
) {
    val userReviewsPaging = userReviews.collectAsLazyPagingItems()
    val context = LocalContext.current

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
                    showBottomSheet {
                        WriteReviewBottomContents(
                            context,
                            updateImageUri,
                            cameraLauncher,
                            galleryLauncher,
                            hideBottomSheet
                        )
                    }
                },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RunwayIconButton(drawable = R.drawable.ic_filled_camera_24, size = 24.dp) {
                    showBottomSheet {
                        WriteReviewBottomContents(
                            context,
                            updateImageUri,
                            cameraLauncher,
                            galleryLauncher,
                            hideBottomSheet
                        )
                    }
                }
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
                    painter = painterResource(id = R.mipmap.img_empty_myreview),
                    contentDescription = "IMG_DUMMY",
                    modifier = Modifier.size(128.dp, 115.dp)
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
                    GlideImage(
                        modifier = Modifier
                            .size(132.dp, 200.dp)
                            .background(Gray100)
                            .clickable {
                                navigateToUserReviewDetail(it)
                            },
                        model = it.imgUrl,
                        contentDescription = "IMG_SELECTED_IMG",
                        contentScale = ContentScale.Crop,
                    ) { requestBuilder ->
                        requestBuilder.signature(ObjectKey(it.imgUrl))
                    }
                }
            }
            item { WidthSpacer(width = 15.dp) }
        }
    }
}

@Composable
private fun WriteReviewBottomContents(
    context: Context,
    updateImageUri: (Uri?) -> Unit,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    galleryLauncher: ManagedActivityResultLauncher<String, Uri?>,
    hideBottomSheet: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        HeightSpacer(height = 10.dp)
        listOf(
            BottomSheetContentItem(
                itemName = "사진 찍기",
                onItemClick = {
                    val uri = ComposeFileProvider.getImageUri(context)
                    updateImageUri(uri)
                    cameraLauncher.launch(uri)
                },
            ),
            BottomSheetContentItem(
                itemName = "사진 앨범",
                onItemClick = {
                    galleryLauncher.launch("image/*")
                },
            )
        ).forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (it.isSeleceted) Primary20 else White)
                    .clickable {
                        it.onItemClick()
                        hideBottomSheet()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(20.dp, 20.dp),
                    text = it.itemName,
                    style = Body1,
                    textAlign = TextAlign.Start,
                    color = it.itemTextColor
                )
            }
        }
    }
}