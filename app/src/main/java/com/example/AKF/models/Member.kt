package com.example.project_election.models

import com.google.gson.annotations.SerializedName
import kotlin.collections.joinToString
import kotlin.text.trim

data class Member(
    @SerializedName("idParty_members") val id: Int? = null,
    @SerializedName("Users_idUsers") val userId: String?,
    @SerializedName("Parties_idParties") val partyId: Int,
    @SerializedName("Party_positions_idParty_positions") val positionId: Int,
    @SerializedName("Admin_idAdmin") val adminId: String?,
    @SerializedName("Party_prefix") val prefix: String?,
    @SerializedName("Party_Fname") val firstName: String?,
    @SerializedName("Party_Lname") val lastName: String?,
    @SerializedName("member_number") val memberNumber: String?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("delete_at") val deleteAt: String?,

    // ⭐ เพิ่มตรงนี้
    @SerializedName("position_name") val positionName: String?
) {
    val fullName: String
        get() = listOf(prefix ?: "", firstName ?: "", lastName ?: "")
            .joinToString(" ")
            .trim()
}