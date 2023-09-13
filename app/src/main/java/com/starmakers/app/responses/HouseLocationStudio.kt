package com.starmakers.app.responses


data class HouseLocationDataResponse(
    val message: String,
    val data: List<HouseLocationStudio>
)

data class HouseLocationStudio(
    val id: Int,
    val studio_picture: List<HouseLocationStudioPicture>,
    val studio_movie: List<HouseLocationStudioMovie>,
    val studio_booking: List<HouseLocationStudioBooking>,
    val studio_name: String,
    val location: String,
    val date_of_start: String,
    val budget: Int,
    val owner: String,
    val owner_number: String,
    val total_no_of_movies: Int,
    val write_about_studio: String,
    val select_studio_type: String,
    val created_at: String,
    val updated_at: String
)

data class HouseLocationStudioPicture(
    val id: Int,
    val studio_picture: String,
    val studio: Int
)

data class HouseLocationStudioMovie(
    val id: Int,
    val studio_movie: String,
    val studio: Int
)

data class HouseLocationStudioBooking(
    val id: Int,
    val studio_booking_date: String,
    val from_date: String,
    val to_date: String,
    val no_of_days: Int,
    val booking_studio: String,
    val studio: Int,
    val studio_bookiee: Int
)
