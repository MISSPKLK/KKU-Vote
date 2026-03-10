package com.example.project_election.models

import com.google.gson.annotations.SerializedName

data class ElectionResult(
    val totalEligible: Int,
    val totalVoted: Int,
    val abstain: Int,
    val winner: String,
    val winnerImage: String,
    val timeStats: TimeStats?,
    val partyVotes: List<PartyVote>
)
