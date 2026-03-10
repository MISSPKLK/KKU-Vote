package com.example.project_election.network

import com.example.project_election.models.CreateElectionResponse
import com.example.project_election.models.Election
import com.example.project_election.models.ElectionCreateRequest
import com.example.project_election.models.ElectionResponse
import com.example.project_election.models.ElectionStats
import com.example.project_election.models.Member
import com.example.project_election.models.PartyDetailResponse
import com.example.project_election.models.PartyPosition
import com.example.project_election.models.Policy
import com.example.project_election.models.UpdateMemberRequest
import com.example.project_election.models.UserSearchResult
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
import retrofit2.http.Query
import kotlin.jvm.java

interface PartyAPI {
    @GET("stats/{id}")
    suspend fun getStats(
        @Path("id") id:Int
    ): ElectionStats
    @GET("party-members/{partyId}")
    suspend fun getMembersByParty(
        @Path("partyId") partyId: String
    ): List<Member>

    @GET("election/current")
    suspend fun getCurrentElection(): ElectionResponse

    @POST("elections")
    suspend fun createElection(
        @Body election: ElectionCreateRequest
    ): Response<CreateElectionResponse>

    @PUT("elections/{id}")
    suspend fun updateElection(
        @Path("id") id: Int,
        @Body election: ElectionCreateRequest
    ): Response<Unit>

    @GET("elections/{id}")
    suspend fun getElectionById(
        @Path("id") id: Int
    ): Response<Election>

    @DELETE("elections/{id}")
    suspend fun deleteElection(
        @Path("id") id: Int
    ): Response<Map<String, String>>

    @GET("parties/{id}")
    suspend fun getPartyDetail(
        @Path("id") id: String
    ): PartyDetailResponse

    @GET("policies/{partyId}")
    suspend fun getPoliciesByParty(
        @Path("partyId") partyId: String
    ): List<Policy>

    @GET("party-positions")
    suspend fun getPartyPositions(): List<PartyPosition>



    @GET("users/search")
    suspend fun searchUsers(
        @Query("email") email: String
    ): List<UserSearchResult>

    @GET("party-member/{id}")
    suspend fun getMemberById(
        @Path("id") id: Int
    ): Member

    @Multipart
    @POST("party-members")
    suspend fun createMember(
        @Part("Users_idUsers") userId: RequestBody,
        @Part("Parties_idParties") partyId: RequestBody,
        @Part("Party_positions_idParty_positions") positionId: RequestBody,
        @Part("Party_prefix") prefix: RequestBody,
        @Part("Party_Fname") firstName: RequestBody,
        @Part("Party_Lname") lastName: RequestBody,
        @Part("member_number") memberNumber: RequestBody,
        @Part("biography") biography: RequestBody,
        @Part file: MultipartBody.Part?,
        @Part("Admin_idAdmin") adminId: RequestBody
    ): Response<Unit>

    @Multipart
    @PUT("party-members/{id}")
    suspend fun updateMemberMultipart(
        @Path("id") id: Int,
        @Part("Users_idUsers") userId: RequestBody,
        @Part("Parties_idParties") partyId: RequestBody,
        @Part("Party_positions_idParty_positions") positionId: RequestBody,
        @Part("Party_prefix") prefix: RequestBody,
        @Part("Party_Fname") firstName: RequestBody,
        @Part("Party_Lname") lastName: RequestBody,
        @Part("member_number") memberNumber: RequestBody,
        @Part("biography") biography: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<Unit>

    @DELETE("party-members/{id}")
    suspend fun deleteMember(
        @Path("id") id: Int
    ): Response<Unit>

    @PUT("party-members/{id}")
    suspend fun updateMember(
        @Path("id") id: Int,
        @Body request: UpdateMemberRequest
    ): Response<Unit>

}

object PartyClient {

    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    val partyAPI: PartyAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PartyAPI::class.java)
    }
}