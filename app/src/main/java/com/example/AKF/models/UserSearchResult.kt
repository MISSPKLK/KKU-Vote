package com.example.project_election.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserSearchResult(
    val idUsers: String,
    val email: String
) : Parcelable
