package com.cmc12th.runway.ui.domain.model

import androidx.annotation.DrawableRes
import com.cmc12th.runway.R
import com.cmc12th.runway.ui.signin.model.CategoryTag

enum class RunwayCategory(
    val visibleName: String,
    @DrawableRes val iconId: Int,
    val idx: Int,
) {
    MINIMAL("미니멀", R.drawable.ic_minimal, 1), CASUAL("캐주얼", R.drawable.ic_casual, 2),
    CITYBOY("시티보이", R.drawable.ic_cityboy, 3), STREET("스트릿", R.drawable.ic_street, 4),
    VINTAGE("빈티지", R.drawable.ic_vintage, 5), FEMININE("페미닌", R.drawable.ic_feminine, 6);

    companion object {
        fun generateCategoryTags(): MutableList<CategoryTag> =
            values().map { category ->
                CategoryTag(id = category.idx,
                    name = category.visibleName,
                    iconId = category.iconId)
            }.toMutableList()
    }
}
