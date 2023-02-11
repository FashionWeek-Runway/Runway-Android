package com.cmc12th.runway.ui.signin.model

import android.net.Uri

sealed class ProfileImageType {
    class LOCAL(val uri: Uri) : ProfileImageType()
    class SOCIAL(val imgUrl: String) : ProfileImageType()
    object DEFAULT : ProfileImageType()
}

