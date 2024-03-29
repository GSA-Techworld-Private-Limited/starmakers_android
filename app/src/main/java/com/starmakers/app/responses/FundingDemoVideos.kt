package com.starmakers.app.responses

import com.google.gson.annotations.SerializedName

data class FundingDemoVideos(
    @SerializedName("id"         ) var id        : Int?    = null,
    @SerializedName("video"      ) var video     : String? = null,
    @SerializedName("created_at" ) var createdAt : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null
)
