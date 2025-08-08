package com.example.grind.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscoverItemClass(
    var image: Int,
    var name: String,
    var msg: String,
    var time: String,
    var phoneno: String,
    var country: String
) : Parcelable
