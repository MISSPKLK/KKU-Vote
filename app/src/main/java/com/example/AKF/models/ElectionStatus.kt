package com.example.project_election.models

data class ElectionStatus(
    val id: Int? = null,
    val status: String,
    val startTime: String? = null,
    val endTime: String? = null,
    val message: String? = null
)