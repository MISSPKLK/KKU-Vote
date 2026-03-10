package com.example.project_election.models

data class UpdateMemberRequest(
    val Users_idUsers: String,
    val Parties_idParties: String,
    val Party_positions_idParty_positions: Int,
    val Party_prefix: String,
    val Party_Fname: String,
    val Party_Lname: String,
    val member_number: String,
    val biography: String
)
