package com.example.project_election.models

data class ElectionCreateRequest(
    val election_name: String,
    val start_time: String,
    val end_time: String
)
