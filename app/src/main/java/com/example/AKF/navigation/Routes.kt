package com.example.project_election.navigation

import java.net.URLEncoder

object Routes {
    const val LOGIN = "login"
    const val HOME = "home"
    const val PARTY_LIST = "party_list"
    const val CANDIDATE_DETAIL = "candidate_detail/{partyId}/{partyName}"
    const val PARTY_MEMBERS = "party_members/{partyId}/{partyName}"
    const val WORK_EXPERIENCE = "work_experience/{partyId}"
    const val NEWS_LIST = "news_list"
    const val ELECTION = "election"
    const val ADD_PARTY = "add_party"
    const val MANAGE_PARTY = "manage_party"
    const val SETTINGS = "settings"
    const val TRENDING_EVENTS = "trending_events"
    const val RESULT = "result"
    const val VOTING = "voting/{endTime}"

    const val NOTIFICATIONS_SETTING = "notifications_setting"
    const val HELP_SUPPORT = "help_support"
    const val ABOUT_APP = "about_app"

    fun candidateDetailRoute(partyId: Int, partyName: String): String {
        return "candidate_detail/$partyId/$partyName"
    }

    fun partyMembersRoute(partyId: Int, partyName: String): String {
        return "party_members/$partyId/$partyName"
    }

    fun workExperienceRoute(partyId: Int): String {
        return "work_experience/$partyId"
    }

    fun votingRoute(endTime: String): String {
        return "voting/${URLEncoder.encode(endTime, "UTF-8")}"
    }
}