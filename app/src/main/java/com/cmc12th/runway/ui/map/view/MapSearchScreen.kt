package com.cmc12th.runway.ui.map.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmc12th.runway.R
import com.cmc12th.runway.data.response.map.RegionSearch
import com.cmc12th.runway.data.response.map.StoreSearch
import com.cmc12th.runway.ui.components.BackIcon
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.WidthSpacer
import com.cmc12th.runway.ui.components.WidthSpacerLine
import com.cmc12th.runway.ui.map.MapViewModel
import com.cmc12th.runway.ui.map.components.SearchTextField
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants.CATEGORYS
import com.cmc12th.runway.utils.noRippleClickable


@Composable
fun MapSearchScreen(
    onBackPrseed: () -> Unit,
    onShopSearch: () -> Unit,
    onLocationSearch: () -> Unit,
    mapViewModel: MapViewModel,
) {
    val searchUiState by mapViewModel.searchUiState.collectAsStateWithLifecycle()
    BackHandler {
        onBackPrseed()
    }
    LaunchedEffect(key1 = searchUiState.searchText) {
        mapViewModel.mapSearch()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .noRippleClickable {}
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Color.White)
    ) {
        TopSearchBar(
            onBackPrseed,
            searchUiState.searchText
        ) { mapViewModel.updateSearchText(it) }
        WidthSpacerLine(height = 1.dp, color = Gray300)
        WidthSpacer(width = 5.dp)

        /** 검색어를 입력하지 않았을 때 */
        if (searchUiState.searchText.text.isBlank()) {
            /** 최근 검색어가 있으면 */
            RecentSearches()
            // TODO 화면제작
        } else {
            /** 검색 중일 때 */
            OnSearching(
                onShopSearch = onShopSearch,
                onLocationSearch = onLocationSearch,
                regionSearchs = searchUiState.regionSearchs,
                storeSearchs = searchUiState.storeSearchs,
                searchText = searchUiState.searchText
            )
        }
    }
}

@Composable
private fun TopSearchBar(
    onBackPrseed: () -> Unit,
    searchTextField: TextFieldValue,
    updateSearchText: (TextFieldValue) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        updateSearchText(
            searchTextField.copy(
                selection = TextRange(searchTextField.text.length),
            )
        )
        focusRequester.requestFocus()
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackIcon {
            onBackPrseed()
        }
        SearchTextField(
            value = searchTextField,
            onvalueChanged = {
                updateSearchText(it)
            },
            placeholderText = "지역, 매장명 검색",
            focusRequest = focusRequester,
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = {
                        updateSearchText(TextFieldValue(""))
                    },
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
fun OnSearching(
    onShopSearch: () -> Unit,
    onLocationSearch: () -> Unit,
    regionSearchs: List<RegionSearch>,
    storeSearchs: List<StoreSearch>,
    searchText: TextFieldValue
) {

    if (storeSearchs.isEmpty() && regionSearchs.isEmpty()) {
        EmptyResult(searchText)
    } else {
        ResultItems(regionSearchs, searchText, onShopSearch, onLocationSearch, storeSearchs)
    }
}

@Composable
private fun EmptyResult(searchText: TextFieldValue) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "IC_DUMMY",
            modifier = Modifier.size(100.dp)
        )
        HeightSpacer(height = 30.dp)
        Text(text = searchText.text, style = HeadLine4, color = Primary)
        Text(text = "에 대한 검색결과가 없습니다.", style = Body1, color = Black)
        HeightSpacer(height = 8.dp)
        Text(text = "다른 검색어를 입력해보세요.", style = Body2, color = Gray400)
    }
}

@Composable
private fun ResultItems(
    regionSearchs: List<RegionSearch>,
    searchText: TextFieldValue,
    onShopSearch: () -> Unit,
    onLocationSearch: () -> Unit,
    storeSearchs: List<StoreSearch>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { HeightSpacer(height = 20.dp) }
        if (regionSearchs.isNotEmpty()) {
            items(regionSearchs) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                        onClick = { },
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
                        Text(buildAnnotatedString {
                            it.region.forEach { storeChar ->
                                if (searchText.text.contains(storeChar)) {
                                    withStyle(
                                        style = SpanStyle(
                                            color = Primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(storeChar)
                                    }
                                } else {
                                    withStyle(style = SpanStyle(color = Black)) {
                                        append(storeChar)
                                    }
                                }
                            }
                        },
                            style = Body1,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable {
                                // TODO 매장 클릭 구현
                                onShopSearch()
                            }
                        )
                        HeightSpacer(height = 4.dp)
                        Text(
                            text = it.address,
                            style = Body2,
                            color = Gray500,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable {
                                // TODO 지역 클릭 구현
                                onLocationSearch()
                            }
                        )
                    }


                }
            }
        }
        if (storeSearchs.isNotEmpty()) {
            items(storeSearchs) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(
                        onClick = { },
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
                        Text(buildAnnotatedString {
                            it.storeName.forEach { storeChar ->
                                if (searchText.text.contains(storeChar)) {
                                    withStyle(
                                        style = SpanStyle(
                                            color = Primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append(storeChar)
                                    }
                                } else {
                                    withStyle(style = SpanStyle(color = Black)) {
                                        append(storeChar)
                                    }
                                }
                            }
                        },
                            style = Body1,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable {
                                // TODO 매장 클릭 구현
                                onShopSearch()
                            }
                        )
                        HeightSpacer(height = 4.dp)
                        Text(
                            text = it.address,
                            style = Body2,
                            color = Gray500,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.clickable {
                                // TODO 지역 클릭 구현
                                onLocationSearch()
                            }
                        )
                    }


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