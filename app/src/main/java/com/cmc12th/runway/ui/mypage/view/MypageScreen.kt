package com.cmc12th.runway.ui.mypage.view

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.components.HeightSpacer
import com.cmc12th.runway.ui.components.RunwayIconButton
import com.cmc12th.runway.ui.components.util.bottomBorder
import com.cmc12th.runway.ui.components.util.topBorder
import com.cmc12th.runway.ui.domain.model.ApplicationState
import com.cmc12th.runway.ui.mypage.MypageViewModel
import com.cmc12th.runway.ui.mypage.model.MypageTabRowItem
import com.cmc12th.runway.ui.theme.*
import com.cmc12th.runway.utils.Constants
import com.cmc12th.runway.utils.Constants.BOTTOM_NAVIGATION_HEIGHT
import com.cmc12th.runway.utils.Constants.LOGIN_GRAPH
import com.cmc12th.runway.utils.Constants.MAIN_GRAPH
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldScope
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@Composable
fun MypageScreen(appState: ApplicationState) {

    val mypageViewModel: MypageViewModel = hiltViewModel()
    val logout = {
        mypageViewModel.logout {
            appState.navController.navigate(LOGIN_GRAPH) {
                popUpTo(MAIN_GRAPH) {
                    inclusive = true
                }
            }
        }
    }

    val selectedPage = remember {
        mutableStateOf(1)
    }
    appState.systmeUiController.setStatusBarColor(Gray50)


    val state = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(bottom = BOTTOM_NAVIGATION_HEIGHT)
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Column(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .parallax(1f)
            ) {
                ProfileInfo()
                HeightSpacer(height = 34.dp)
                /** 나의 후기, 저장 로우탭 */
                CustomRowTab(selectedPage)
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
                    .pin(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "MY", style = HeadLine4, color = Color.Black)
                RunwayIconButton(
                    drawable = R.drawable.ic_baseline_setting_24,
                    tint = Black
                ) {

                }
            }
        }
    ) {
        Column {
            MyReviews()
        }
    }
}

@Composable
private fun ColumnScope.MyReviews() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items((0..25).toList()) {
            Box(modifier = Modifier.weight(1f)) {
                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(0.65f)
                        .fillMaxSize(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(R.drawable.img_dummy)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.img_dummy),
                    error = painterResource(id = R.drawable.img_dummy),
                    contentDescription = "IMG_PROFILE",
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
private fun CustomRowTab(selectedPage: MutableState<Int>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .topBorder(1.dp, Gray200)
            .bottomBorder(1.dp, Gray200),
    ) {
        MypageTabRowItem.values().forEach {
            val isSelected = selectedPage.value == it.id
            Column(
                modifier = Modifier
                    .weight(1f)
                    .bottomBorder(if (isSelected) 2.5.dp else 0.dp, Black)
                    .clickable {
                        selectedPage.value = it.id
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                HeightSpacer(height = 5.dp)
                Icon(
                    painter = painterResource(id = if (selectedPage.value == it.id) it.selecteddrawableResId else it.drawableResId),
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

@Composable
private fun ProfileInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(60.dp)) {
            AsyncImage(
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.img_dummy)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.img_dummy),
                error = painterResource(id = R.drawable.img_dummy),
                contentDescription = "IMG_PROFILE",
                contentScale = ContentScale.Crop,
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .background(Color.White)
                    .border(BorderStroke(1.dp, Primary), CircleShape)
                    .size(20.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(12.dp),
                    painter = painterResource(id = R.drawable.ic_filled_pencil_12),
                    contentDescription = "IC_PENCIL",
                    tint = Primary,
                )
            }
        }
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "안녕하세요", style = HeadLine4M, color = Black)
            Text(text = "나패피님", style = HeadLine3, color = Black)
        }

    }
}

@Composable
private fun ColumnScope.EmptyMyReview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 85.dp)
            .weight(1f),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_dummy),
            contentDescription = "IMG_DUMMY",
            modifier = Modifier.size(100.dp)
        )
        HeightSpacer(height = 30.dp)
        Text(text = "내 스타일의 매장에 방문하고", style = Body1, color = Black)
        Text(text = "기록해보세요!", style = Body2, color = Black)
    }
}
