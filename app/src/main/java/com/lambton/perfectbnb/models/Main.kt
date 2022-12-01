package com.lambton.perfectbnb.models

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp")
    val temp: Double
)
