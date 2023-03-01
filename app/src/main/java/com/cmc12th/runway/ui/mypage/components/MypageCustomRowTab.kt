package com.cmc12th.runway.ui.mypage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.components.util.topBorder
import com.cmc12th.runway.ui.mypage.model.MypageTabRowItem
import com.cmc12th.runway.ui.mypage.view.MypageTabInfo
import com.cmc12th.runway.ui.theme.Black
import com.cmc12th.runway.ui.theme.Body2
import com.cmc12th.runway.ui.theme.Gray200
import com.cmc12th.runway.ui.theme.Gray300

@Composable
fun MypageCustomRowTab(selectedPage: MypageTabInfo, updateSelectedPage: (MypageTabInfo) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .topBorder(1.dp, Gray200)
            .bottomBorder(1.dp, Gray200),
    ) {
        MypageTabRowItem.values().forEach {
            val isSelected = selectedPage == it.mypageTabInfo
            Column(
                modifier = Modifier
                    .weight(1f)
                    .bottomBorder(if (isSelected) 2.5.dp else 0.dp, Black)
                    .clickable {
                        updateSelectedPage(it.mypageTabInfo)
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                HeightSpacer(height = 5.dp)
                Icon(
                    painter = painterResource(id = if (selectedPage == it.mypageTabInfo) it.selecteddrawableResId else it.drawableResId),
                    contentDescription = "IC_TAB_ROW",
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Black else Gray300
                )
                Text(
                    text = it.title,
                    style = Body2,
                    color = if (isSelected) Black else Gray300
                )
                HeightSpacer(height = 9.dp)
            }
        }
    }
}
