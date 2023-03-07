package com.cmc12th.runway.ui.signin.model

data class Agreement(
    val title: String,
    var isRequire: Boolean = false,
    var isChecked: Boolean = false,
    var link: String,
)

