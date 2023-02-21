package com.cmc12th.runway.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailVIewModel @Inject constructor(

) : ViewModel() {

    private var loadedFirstData = false

    fun getDetailInfo() {
        if (!loadedFirstData) {
            Log.i("dlgocks1", "loadDAta")
            loadedFirstData = true
        }
    }


}