package com.example.project_election.network

import com.example.project_election.models.Party
import com.example.project_election.models.Candidate
import com.example.project_election.models.ElectionResult
import com.example.project_election.models.ElectionStats
import com.example.project_election.models.ElectionStatus
import com.example.project_election.models.HasVotedResponse
import com.example.project_election.models.PartyMem
import com.example.project_election.models.Voted
import com.example.project_election.models.Winner
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ElectionApi {
    @GET("stats/{id}")
    suspend fun getStats(
        @Path("id") id:Int
    ): ElectionStats
    @GET("parties")
    suspend fun getParties(): List<Party>

    // ✅ ต้องอยู่ก่อน route ที่มี {id}
    @GET("parties/members")
    suspend fun getPartyMembers(): List<PartyMem>

    // ✅ อยู่หลัง static route
    @GET("parties/{id}/candidates")
    suspend fun getCandidatesByParty(@Path("id") partyId: Int): List<Candidate>
    /////aom add
    // เปลี่ยนจาก @POST + @Body เป็น Multipart
    @Multipart
    @POST("parties")
    suspend fun addParty(
        @Part("Party_name") partyName: RequestBody,
        @Part("Party_number") partyNumber: RequestBody,
        @Part("Party_PrefixName") partyPrefixName: RequestBody,
        @Part("Party_description") description: RequestBody,
        @Part logo: MultipartBody.Part?,
        @Part image_poster: MultipartBody.Part?    // ชื่อ parameter คือ image_poster
    ): Party

    @DELETE("parties/{id}")
    suspend fun deleteParty(
        @Path("id") id: Int
    )
    ///aom add
    @GET("parties/{id}")
    suspend fun getPartyById(@Path("id") id: Int): Party

    @Multipart
    @PUT("parties/{id}")
    suspend fun updateParty(
        @Path("id") id: Int,
        @Part("Party_name") partyName: RequestBody,
        @Part("Party_number") partyNumber: RequestBody,
        @Part("Party_PrefixName") partyPrefixName: RequestBody,
        @Part("Party_description") description: RequestBody,
        @Part logo: MultipartBody.Part?,
        @Part image_poster: MultipartBody.Part?
    ): Party

    //user
    @GET("election/result")
    suspend fun getElectionResult(): ElectionResult

    @GET("election/winner")
    suspend fun getWinner(): Winner

    @GET("election/status")
    suspend fun getElectionStatus(): ElectionStatus

    @POST("vote")
    suspend fun submitVote(@Body request: Voted): Response<Unit>

    @GET("vote/check/{userId}/{electionId}")
    suspend fun checkHasVoted(
        @Path("userId") userId: Int,
        @Path("electionId") electionId: Int
    ): HasVotedResponse


}

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val api: ElectionApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ElectionApi::class.java)
    }
}


