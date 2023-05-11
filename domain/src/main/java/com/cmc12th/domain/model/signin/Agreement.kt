package com.cmc12th.domain.model.signin

data class Agreement(
    val title: String,
    var isRequire: Boolean = false,
    var isChecked: Boolean = false,
    var link: String,
)

