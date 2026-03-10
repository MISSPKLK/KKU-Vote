package com.example.project_election.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.project_election.screens.AboutAppScreen
import com.example.project_election.screens.AddMemberScreen
import com.example.project_election.screens.AddPartyScreen
import com.example.project_election.screens.CandidateDetailScreen
import com.example.project_election.screens.CountdownScreen
import com.example.project_election.screens.EditPartyScreen
import com.example.project_election.screens.ElectionScreen
import com.example.project_election.screens.ElectionStatsScreen
import com.example.project_election.screens.HelpSupportScreen
import com.example.project_election.screens.HomeScreen
import com.example.project_election.screens.LoginScreen
import com.example.project_election.screens.ManagePartyScreen
import com.example.project_election.screens.NewsScreen
import com.example.project_election.screens.NotificationSettingsScreen
import com.example.project_election.screens.PartyListScreen
import com.example.project_election.screens.PartyMembersScreen
import com.example.project_election.screens.ResultScreen
import com.example.project_election.screens.SearchUserScreen
import com.example.project_election.screens.SendNotificationScreen
import com.example.project_election.screens.SettingsScreen
import com.example.project_election.screens.TrendingEventsScreen
import com.example.project_election.screens.VotingScreen
import com.example.project_election.screens.VotingUsageScreen
import com.example.project_election.screens.WorkExperienceScreen
import com.example.project_election.viewmodel.StatsViewModel
import com.example.projectmode.AdminHomeScreen
import com.example.projectmode.ElectionViewModel
import com.example.projectmode.ManageElectionScreen
import com.example.projectmode.PartyDetailScreen
import com.example.projectmode.PartyInfoScreen
import java.net.URLDecoder

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.LOGIN,
    onGoogleSignInClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val sharedViewModel: ElectionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ข้าวสวยแก้
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { email ->
                    if (email == "admin@kku.ac.th") {
                        navController.navigate("AdminHomeScreen") {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                        }
                    }
                },
                onGoogleSignInClick = onGoogleSignInClick
            )
        }

        // หน้า Home แบบใหม่ ไม่ต้องรับ email ผ่าน parameter แล้ว
        composable(Routes.HOME) {
            HomeScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(Routes.PARTY_LIST) {
            PartyListScreen(
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(
            route = Routes.CANDIDATE_DETAIL,
            arguments = listOf(
                navArgument("partyId") { type = NavType.IntType },
                navArgument("partyName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getInt("partyId") ?: 0
            val partyName = backStackEntry.arguments?.getString("partyName") ?: "ไม่ระบุชื่อพรรค"
            CandidateDetailScreen(
                partyId = partyId,
                partyName = partyName,
                navController = navController
            )
        }

        composable(
            route = Routes.PARTY_MEMBERS,
            arguments = listOf(
                navArgument("partyId") { type = NavType.IntType },
                navArgument("partyName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getInt("partyId") ?: 0
            val partyName = backStackEntry.arguments?.getString("partyName") ?: ""
            PartyMembersScreen(partyId, partyName, navController)
        }

        composable(
            route = Routes.WORK_EXPERIENCE,
            arguments = listOf(navArgument("partyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getInt("partyId") ?: 0
            WorkExperienceScreen(partyId, navController)
        }

        composable(Routes.NEWS_LIST) {
            NewsScreen(navController = navController)
        }

        //aom add
        composable(Routes.ADD_PARTY) {
            AddPartyScreen(navController = navController)
        }

        composable(Routes.MANAGE_PARTY) {
            ManagePartyScreen(navController)
        }

        composable(
            route = "editParty/{partyId}",
            arguments = listOf(navArgument("partyId") { type = NavType.IntType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getInt("partyId") ?: 0
            EditPartyScreen(navController = navController, partyId = partyId)
        }//end aom add

        // --------------------------------------------------------------------------

        composable("AdminHomeScreen") {
            AdminHomeScreen(navController = navController)
        }

        composable("manage_election") {
            ManageElectionScreen(navController)
        }

        composable(
            route = "partyDetail/{partyId}",
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: ""
            PartyDetailScreen(
                navController = navController,
                partyId = partyId
            )
        }

        composable(
            route = "addCandidate/{partyId}",
            arguments = listOf(navArgument("partyId") { type = NavType.StringType })
        ) { backStackEntry ->
            val partyId = backStackEntry.arguments?.getString("partyId") ?: ""
            AddMemberScreen(navController, partyId)
        }

        composable(
            route = "editElection/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            ManageElectionScreen(
                navController = navController,
                electionId = id
            )
        }

        composable("partyInfo/{partyId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("partyId") ?: ""
            PartyInfoScreen(navController, id)
        }

        composable("search_user") {
            SearchUserScreen(navController)
        }

        composable(
            route = "editCandidate/{partyId}/{memberId}",
            arguments = listOf(
                navArgument("partyId") { type = NavType.StringType },
                navArgument("memberId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            AddMemberScreen(
                navController = navController,
                partyId = backStackEntry.arguments?.getString("partyId") ?: "",
                memberId = backStackEntry.arguments?.getInt("memberId")
            )
        }

        composable("stats"){
            val viewModel: StatsViewModel = viewModel()
            val stats = viewModel.stats.value

            LaunchedEffect(Unit){
                viewModel.loadStats(1)
            }

            ElectionStatsScreen(
                stats = stats ?: return@composable,
                onOpenVotingUsage = {
                    navController.navigate(route = "voting_usage")
                },
                navController = navController
            )
        }

        composable("voting_usage"){
            val statsEntry = remember {
                navController.getBackStackEntry("stats")
            }
            val viewModel: StatsViewModel = viewModel(statsEntry)

            VotingUsageScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        //เพิ่มจากฝั่งuser
        composable(Routes.ELECTION) {
            ElectionScreen(
                viewModel = sharedViewModel,
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(
            route = "countdown/{endTime}",
            arguments = listOf(navArgument("endTime") { type = NavType.StringType })
        ) { backStackEntry ->
            val rawEndTime = backStackEntry.arguments?.getString("endTime") ?: ""
            val endTime = URLDecoder.decode(rawEndTime, "UTF-8")
            CountdownScreen(
                endTime = endTime,
                viewModel = sharedViewModel,
                navController = navController,
                currentRoute = currentRoute,
                onTimeUp = {
                    navController.navigate(Routes.RESULT)
                }
            )
        }

        composable(Routes.RESULT) {
            ResultScreen(
                viewModel = sharedViewModel,
                navController = navController,
                currentRoute = currentRoute
            )
        }

        composable(
            route = "voting/{endTime}",
            arguments = listOf(
                navArgument("endTime") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rawEndTime = backStackEntry.arguments?.getString("endTime") ?: ""
            val endTime = URLDecoder.decode(rawEndTime, "UTF-8")
            VotingScreen(
                endTime = endTime,
                viewModel = sharedViewModel,
                navController = navController,
                currentRoute = currentRoute
            )
        }

        // เมนูตั้งค่าและการแจ้งเตือนต่างๆ
        composable(Routes.SETTINGS) {
            SettingsScreen(navController = navController, currentRoute = currentRoute)
        }

        composable(Routes.TRENDING_EVENTS) {
            TrendingEventsScreen(navController = navController)
        }

        composable(Routes.NOTIFICATIONS_SETTING) {
            NotificationSettingsScreen(navController = navController)
        }

        composable(Routes.HELP_SUPPORT) {
            HelpSupportScreen(navController = navController)
        }

        composable(Routes.ABOUT_APP) {
            AboutAppScreen(navController = navController)
        }

        composable("send_notification") {
            SendNotificationScreen(navController) }
    }
}