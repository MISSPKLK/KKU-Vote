package com.example.project_election.models

data class Voted(
    val Users_idUsers: Int,
    val Elections_idElections: Int,
    val Parties_idParties: Int?,
    val is_abstain: Boolean = false
)