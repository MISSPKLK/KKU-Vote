package com.example.project_election.models


data class Election(
    val idElections: Int,
    val election_name: String,
    val start_time: String,
    val end_time: String,
    val created_at: String,
    val update_at: String,
    val delete_at: String?
)
