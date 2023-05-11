package com.cmc12th.domain.model.signin

import android.net.Uri

sealed class ProfileImageType {
    class LOCAL(val uri: Uri) : ProfileImageType()
    class SOCIAL(val imgUrl: String) : ProfileImageType()
    object DEFAULT : ProfileImageType()
}

