package com.example.zoomexample

import android.content.Context
import android.os.Build

object VersionedGestureDetector {
    fun newInstance(
        context: Context?,
        listener: OnGestureListener?
    ): GestureDetector {
        val sdkVersion = Build.VERSION.SDK_INT
        val detector: GestureDetector
        detector = if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            CupcakeGestureDetector(context)
        } else if (sdkVersion < Build.VERSION_CODES.FROYO) {
            EclairGestureDetector(context)
        } else {
            FroyoGestureDetector(context)
        }
        detector.setOnGestureListener(listener)
        return detector
    }
}