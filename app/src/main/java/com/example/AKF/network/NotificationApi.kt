package com.example.project_election.network

import retrofit2.http.GET

interface NotificationApi {
    @GET("api/notifications")
    suspend fun getNotifications(): List<NotificationResponse>
}

data class NotificationResponse(
    val title: String,
    val message: String,
    val created_at: String
)