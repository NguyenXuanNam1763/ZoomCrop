package com.example.zoomexample

import android.net.Uri
import java.io.File

object Utils {
    fun getImageUri(path: String?): Uri {
        return Uri.fromFile(File(path))
    }
}
