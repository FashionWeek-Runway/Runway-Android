package com.cmc12th.runway.ui.mypage.model

import androidx.annotation.DrawableRes
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.mypage.view.MypageTabInfo

enum class MypageTabRowItem(
    val mypageTabInfo: MypageTabInfo,
    val title: String,
    @DrawableRes val drawableResId: Int,
    @DrawableRes val selecteddrawableResId: Int
) {
    MYREVIEW(
        MypageTabInfo.MY_REVIEW,
        "나의 후기",
        R.drawable.ic_baseline_my_review_24,
        R.drawable.ic_filled_my_review_24
    ),
    STORAGE(
        MypageTabInfo.STORAGE,
        "저장",
        R.drawable.ic_border_bookmark_24,
        R.drawable.ic_filled_bookmark_24
    ),
}