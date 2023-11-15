package com.example.project9

import com.google.firebase.database.Exclude

data class Image(
    @get:Exclude
    var imageId: String = "",
    var url: String = ""
)
