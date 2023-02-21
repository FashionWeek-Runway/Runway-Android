package com.cmc12th.runway.ui.map.model

import android.location.Location

sealed class MovingCameraWrapper {
    object DEFAULT : MovingCameraWrapper()
    class MOVING(val location: Location) : MovingCameraWrapper()
}