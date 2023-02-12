package com.cmc12th.runway.ui.map.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.signin.components.StyleCategoryCheckBox
import com.cmc12th.runway.ui.theme.Body1
import com.cmc12th.runway.ui.theme.Gray300
import com.cmc12th.runway.ui.theme.Gray700
import com.cmc12th.runway.ui.theme.White
import com.cmc12th.runway.utils.Constants

@Composable
fun NaverMapSearch(onSearch: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .border(BorderStroke(1.dp, Gray300), RoundedCornerShape(4.dp))
                .clickable {
                    onSearch()
                },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 13.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "지역, 매장명 검색", style = Body1, color = Gray300)
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = "IC_SEARCH",
                    modifier = Modifier.size(24.dp),
                    tint = Gray700
                )
            }
        }
        HeightSpacer(height = 15.dp)
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Constants.CATEGORYS) {
                val surfaceColor: State<Color> = animateColorAsState(
//                    if (categoryTag.isSelected) Primary else
                    White
                )
                StyleCategoryCheckBox(
                    isSelected = false,
                    color = surfaceColor.value,
                    onClicked = { /*TODO*/ },
                    title = it
                )
                WidthSpacer(width = 10.dp)
            }
        }
    }
}
