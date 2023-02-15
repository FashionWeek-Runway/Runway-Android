package com.cmc12th.runway.ui.map.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.map.components.SearchTextField
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.CATEGORYS
import com.cmc12th.runway.utils.noRippleClickable


@Composable
fun MapSearchScreen(onBackPrseed: () -> Unit) {
    val searchTextField = remember {
        mutableStateOf("")
    }

    BackHandler {
        onBackPrseed()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {}
            .navigationBarsPadding()
            .background(Color.White)
    ) {
        TopSearchBar(onBackPrseed, searchTextField.value) { searchTextField.value = it }
        WidthSpacerLine(height = 1.dp, color = Gray300)
        WidthSpacer(width = 5.dp)

        /** 검색어를 입력하지 않았을 때 */
        if (searchTextField.value.isBlank()) {
            /** 최근 검색어가 있으면 */
            RecentSearches()

            /** 최근 검색어가 없으면 */
            // TODO 화면제작
        } else {
            /** 검색 중일 때 */
            OnSearching()
        }
    }
}

@Composable
private fun TopSearchBar(
    onBackPrseed: () -> Unit,
    searchTextField: String,
    updateSearchText: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackIcon {
            onBackPrseed()
        }
        SearchTextField(
            value = searchTextField,
            onvalueChanged = { updateSearchText(it) },
            placeholderText = "지역, 매장명 검색",
            focusRequest = focusRequester,
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = { },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_fill_circle),
                        contentDescription = "IC_CLOSE_CIRCLE",
                        tint = Color.Unspecified,
                    )
                }
            }
        )
    }
}

@Composable
fun OnSearching() {

    val items = CATEGORYS

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 30.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(items) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_location),
                        contentDescription = "IC_DUMMY",
                        tint = Color.Unspecified
                    )
                }
                WidthSpacer(width = 4.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = it,
                        style = Body1,
                        color = Black,
                        textAlign = TextAlign.Start
                    )
                    HeightSpacer(height = 4.dp)
                    Text(
                        text = "매장 주소",
                        style = Body2,
                        color = Gray500,
                        textAlign = TextAlign.Start
                    )
                }


            }
        }
    }
}

@Composable
private fun RecentSearches() {

    val items = CATEGORYS

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "최근 검색", style = Body2M, color = Gray600)
            Text(text = "전체 삭제", style = Button2, color = Gray700)
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(items) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search_location),
                            contentDescription = "IC_DUMMY",
                            tint = Color.Unspecified
                        )
                    }

                    WidthSpacer(width = 4.dp)
                    Text(
                        text = it,
                        style = Body1,
                        color = Black,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start
                    )
                    Text(text = "02.12", style = Caption, color = Gray500)
                    WidthSpacer(width = 2.dp)
                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_baseline_small),
                            contentDescription = "IC_DUMMY"
                        )
                    }
                }
            }
        }
    }
}