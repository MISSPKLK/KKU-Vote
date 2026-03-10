package com.example.project_election

import com.example.project_election.models.ElectionStats
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // ดึง stats ตาม electionId (เดิม)
    @GET("api/stats/{id}")
    suspend fun getStats(
        @Path("id") id: Int
    ): ElectionStats

    // ดึง stats ตามปี ค.ศ. (ใหม่) — ต้องตรงกับ Backend route /api/stats/year/:year
    @GET("api/stats/year/{year}")
    suspend fun getStatsByYear(
        @Path("year") year: Int
    ): ElectionStats
}