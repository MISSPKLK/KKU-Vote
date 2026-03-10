package com.example.project_election.models

data class PartyMem(
    val idParty_members: Int,
    val Users_idUsers: String,
    val Parties_idParties: Int,
    val Party_positions_idParty_positions: Int?,
    val profile_image: String?,
    val biography: String?,
    val Party_Fname: String?,
    val Party_Lname: String?,
    val member_number: String?
)