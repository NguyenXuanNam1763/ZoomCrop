package com.example.zoomexample

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.View

object ImageViewUtil {

    fun getBitmapRectCenterInside(bitmap: Bitmap, view: View): Rect {
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val viewWidth = view.width
        val viewHeight = view.height
        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight)
    }


    fun getBitmapRectCenterInside(
        bitmapWidth: Int,
        bitmapHeight: Int,
        viewWidth: Int,
        viewHeight: Int
    ): Rect {
        return getBitmapRectCenterInsideHelper(bitmapWidth, bitmapHeight, viewWidth, viewHeight)
    }

    private fun getBitmapRectCenterInsideHelper(
        bitmapWidth: Int,
        bitmapHeight: Int,
        viewWidth: Int,
        viewHeight: Int
    ): Rect {
        val resultWidth: Double
        val resultHeight: Double
        val resultX: Int
        val resultY: Int
        var viewToBitmapWidthRatio = Double.POSITIVE_INFINITY
        var viewToBitmapHeightRatio = Double.POSITIVE_INFINITY

        // Checks if either width or height needs to be fixed
        if (viewWidth < bitmapWidth) {
            viewToBitmapWidthRatio = viewWidth.toDouble() / bitmapWidth.toDouble()
        }
        if (viewHeight < bitmapHeight) {
            viewToBitmapHeightRatio = viewHeight.toDouble() / bitmapHeight.toDouble()
        }

        // If either needs to be fixed, choose smallest ratio and calculate from
        // there
        if (viewToBitmapWidthRatio != Double.POSITIVE_INFINITY || viewToBitmapHeightRatio != Double.POSITIVE_INFINITY) {
            if (viewToBitmapWidthRatio <= viewToBitmapHeightRatio) {
                resultWidth = viewWidth.toDouble()
                resultHeight = bitmapHeight * resultWidth / bitmapWidth
            } else {
                resultHeight = viewHeight.toDouble()
                resultWidth = bitmapWidth * resultHeight / bitmapHeight
            }
        } else {
            resultHeight = bitmapHeight.toDouble()
            resultWidth = bitmapWidth.toDouble()
        }

        // Calculate the position of the bitmap inside the ImageView.
        if (resultWidth == viewWidth.toDouble()) {
            resultX = 0
            resultY = Math.round((viewHeight - resultHeight) / 2).toInt()
        } else if (resultHeight == viewHeight.toDouble()) {
            resultX = Math.round((viewWidth - resultWidth) / 2).toInt()
            resultY = 0
        } else {
            resultX = Math.round((viewWidth - resultWidth) / 2).toInt()
            resultY = Math.round((viewHeight - resultHeight) / 2).toInt()
        }
        return Rect(
            resultX,
            resultY,
            resultX + Math.ceil(resultWidth).toInt(),
            resultY + Math.ceil(resultHeight).toInt()
        )
    }
}