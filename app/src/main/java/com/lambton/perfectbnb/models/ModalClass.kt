package com.lambton.perfectbnb.models

import com.google.gson.annotations.SerializedName
import com.lambton.perfectbnb.models.Main

data class ModalClass(
    @SerializedName("main")
    val main: Main
)
