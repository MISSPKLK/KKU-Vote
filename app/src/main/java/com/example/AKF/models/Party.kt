package com.example.project_election.models

data class Party(
    val idParties: Int,
    val Party_number: Int,
    val Party_PrefixName: String?,
    val Party_name: String,
    val Party_description: String,
    val logo: String,
    val image_poster: String?,
    val isDeleted: Boolean = false,
    val votes: Int = 0
)
