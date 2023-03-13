package com.cmc12th.runway.ui.detail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.store.StoreDetail
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.theme.Blue900
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Button2
import com.cmc12th.runway.utils.skeletonUI

@Composable
fun ShowRoomDetail(
    storeDetail: StoreDetail,
    navigateToWeb: (String) -> Unit,
    navigateToInstgram: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(20.dp, 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            storeDetail.address.skeletonUI(size = 230.dp to 18.dp) {
                RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_map_18)
                Text(text = storeDetail.address, style = Body2, color = Color.Black)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RunwayIconButton(drawable = R.drawable.ic_border_copy_14, size = 14.dp)
                    Text(text = "복사", style = Button2, color = Blue900)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            storeDetail.address.skeletonUI(size = 155.dp to 18.dp) {
                RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_time_18)
                Text(text = storeDetail.storeTime, style = Body2, color = Color.Black)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            storeDetail.storePhone.skeletonUI(size = 142.dp to 18.dp) {
                RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_call_18)
                Text(text = storeDetail.storePhone, style = Body2, color = Color.Black)
            }
        }
        if (!storeDetail.instagram.isNullOrBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToInstgram(storeDetail.instagram)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_instagram_18)
                Text(
                    text = storeDetail.instagram,
                    textDecoration = TextDecoration.Underline,
                    style = Body2,
                    color = Color.Black
                )
            }
        }
        if (!storeDetail.webSite.isNullOrBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToWeb(storeDetail.webSite)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                storeDetail.webSite.skeletonUI(size = 118.dp to 18.dp) {
                    RunwayIconButton(size = 18.dp, drawable = R.drawable.ic_border_web_18)
                    Text(
                        text = storeDetail.webSite,
                        textDecoration = TextDecoration.Underline,
                        style = Body2,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
