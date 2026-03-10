package com.example.project_election.models

data class PartyDetailResponse(
    val idParties: Int,
    val Party_number: Int,
    val Party_PrefixName: String?,
    val Party_name: String,
    val Party_description: String?,
    val logo: String?,
    val image_poster: String?,
    val member_count: Int,
    val position_name: String?
)