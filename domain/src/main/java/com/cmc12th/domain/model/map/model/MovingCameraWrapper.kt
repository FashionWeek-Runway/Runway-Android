package com.cmc12th.domain.model.map.model

import android.location.Location

sealed class MovingCameraWrapper {
    object DEFAULT : MovingCameraWrapper()
    class MOVING(val location: Location) : MovingCameraWrapper()
}