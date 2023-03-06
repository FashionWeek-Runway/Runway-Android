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
import com.cmc12th.runway.ui.signin.model.CategoryTag
import com.cmc12th.runway.ui.theme.*

@Composable
fun SearchBoxAndTagCategory(
    isBookmarked: Boolean,
    categoryItems: List<CategoryTag>,
    updateCategoryTags: (CategoryTag) -> Unit,
    updateIsBookmarked: (Boolean) -> Unit,
    onSearch: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        /** 상단 검색 바 */
        Box(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 4.dp)
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
        HeightSpacer(height = 10.dp)

        /** 카테고리 리스트 */
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                WidthSpacer(width = 10.dp)
            }

            item {
                val surfaceColor: State<Color> = animateColorAsState(
                    if (isBookmarked) Primary else Gray50
                )
                /** 즐겨찾기 아이콘 */
                BookmarkIcon(
                    isBookmarked = isBookmarked,
                    surfaceColor = surfaceColor,
                    updateIsBookmarked = updateIsBookmarked
                )
            }

            items(categoryItems) {
                val surfaceColor: State<Color> = animateColorAsState(
                    if (it.isSelected) Primary else Gray50
                )
                StyleCategoryCheckBoxInMapView(
                    isSelected = it.isSelected,
                    color = surfaceColor.value,
                    onClicked = {
                        updateCategoryTags(it)
                    },
                    title = it.name,
                )
            }

            item {
                WidthSpacer(width = 10.dp)
            }
        }
        HeightSpacer(height = 10.dp)

    }
}

@Composable
private fun BookmarkIcon(
    isBookmarked: Boolean,
    surfaceColor: State<Color>,
    updateIsBookmarked: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .border(
                1.dp,
                if (isBookmarked) Primary else Gray200,
                RoundedCornerShape(5.dp)
            )
            .background(surfaceColor.value)
            .clickable {
                updateIsBookmarked(!isBookmarked)
            }
    ) {
        Icon(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.ic_baseline_bookmark_24),
            contentDescription = "IC_BASELINE_BOOKMAR",
            tint = if (isBookmarked) Point else Gray400
        )
    }
}

