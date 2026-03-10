package com.example.project_election.models

data class ElectionStats(
    val parties: List<Party>,
    val abstain:Int,
    val voters:Int,
    val students:Int
)

data class Stats(
    val students: Int,
    val parties: List<Party>,
    val abstain: Int
)

