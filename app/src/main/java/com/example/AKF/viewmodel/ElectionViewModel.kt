package com.example.projectmode


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_election.models.ElectionCreateRequest
import com.example.project_election.network.PartyClient
import kotlinx.coroutines.launch
import com.example.project_election.models.Election
import com.example.project_election.models.ElectionResult
import com.example.project_election.models.ElectionStatus
import com.example.project_election.models.Voted
import com.example.project_election.models.Winner
import com.example.project_election.network.RetrofitInstance
import com.example.project_election.network.RetrofitInstance.api
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ElectionViewModel : ViewModel() {

    private val _hasVoted = MutableStateFlow(false)
    val hasVoted: StateFlow<Boolean> = _hasVoted

    private val _status = MutableStateFlow<ElectionStatus?>(null)
    val status: StateFlow<ElectionStatus?> = _status

    private val _result = MutableStateFlow<ElectionResult?>(null)
    val result: StateFlow<ElectionResult?> = _result

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _winner = MutableStateFlow<Winner?>(null)
    val winner: StateFlow<Winner?> = _winner

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _userId = MutableStateFlow(1)
    val userId: StateFlow<Int> = _userId


    var currentElection by mutableStateOf<Election?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var selectedElection by mutableStateOf<Election?>(null)
        private set

    fun loadCurrentElection() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = PartyClient.partyAPI.getCurrentElection()
                currentElection = response.election
            } catch (e: Exception) {
                Log.e("ElectionVM", e.message ?: "error")
            } finally {
                isLoading = false
            }
        }
    }

    fun createElection(
        name: String,
        start: String,
        end: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true

                val request = ElectionCreateRequest(
                    election_name = name,
                    start_time = start,
                    end_time = end
                )

                val response = PartyClient.partyAPI.createElection(request)

                if (response.isSuccessful) {
                    onSuccess()
                }

            } catch (e: Exception) {
                Log.e("ElectionVM", e.message ?: "error")
            } finally {
                isLoading = false
            }
        }
    }

    fun loadElectionById(id: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = PartyClient.partyAPI.getElectionById(id)

                if (response.isSuccessful) {
                    selectedElection = response.body()
                }

            } catch (e: Exception) {
                Log.e("ElectionVM", e.message ?: "error")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateElection(
        id: Int,
        name: String,
        start: String,
        end: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {

                val request = ElectionCreateRequest(
                    election_name = name,
                    start_time = start,
                    end_time = end
                )

                val response = PartyClient.partyAPI.updateElection(id, request)

                if (response.isSuccessful) {
                    onSuccess()
                }

            } catch (e: Exception) {
                Log.e("ElectionVM", e.message ?: "error")
            }
        }
    }

    fun deleteElection(
        id: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                isLoading = true

                val response = PartyClient.partyAPI.deleteElection(id)

                Log.d("DELETE_DEBUG", "code = ${response.code()}")
                Log.d("DELETE_DEBUG", "body = ${response.body()}")

                if (response.isSuccessful) {
                    onSuccess()
                }

            } catch (e: Exception) {
                Log.e("ElectionVM", "ERROR = ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
    init {
        fetchStatus()
    }

    fun fetchStatus() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getElectionStatus()
                _status.value = response
            } catch (e: Exception) {

                android.util.Log.e("VOTE_ERROR", "โหลดหน้าโหวตพังเพราะ: ${e.message}")


                _status.value = ElectionStatus(status = "NOT_STARTED")
            }
        }
    }

    fun fetchResult() {
        viewModelScope.launch {
            try {
                _result.value = api.getElectionResult()
            } catch (e: Exception) {
                Log.e("Election", e.message ?: "Result error")
            }
        }
    }
    fun fetchWinner() {
        viewModelScope.launch {
            try {
                _winner.value = api.getWinner()
            } catch (e: Exception) {
                Log.e("WINNER", "Error: ${e.message}")
            }
        }
    }

    fun checkHasVoted(userId: Int, electionId: Int) {
        viewModelScope.launch {
            try {
                val response = api.checkHasVoted(userId, electionId)
                _hasVoted.value = response.hasVoted
            } catch (e: Exception) {
                Log.e("VOTE", "checkHasVoted error: ${e.message}")
            }
        }
    }
    fun submitVote(electionId: Int, partyId: Int?, isAbstain: Boolean = false) {
        viewModelScope.launch {
            try {
                val body = Voted(
                    Users_idUsers = _userId.value,
                    Elections_idElections = electionId,
                    Parties_idParties = if (isAbstain) null else partyId,
                    is_abstain = isAbstain
                )

                val response = api.submitVote(body)

                if (response.isSuccessful) {
                    _statusMessage.value = "ลงคะแนนสำเร็จ!"
                    _isSuccess.value = true
                    _hasVoted.value = true
                } else {
                    _statusMessage.value = "คุณได้ใช้สิทธิ์ลงคะแนนในการเลือกตั้งนี้ไปแล้ว"
                    _isSuccess.value = false

                }

            } catch (e: Exception) {
                Log.e("VOTE", "Exception: ${e.message}")
                _statusMessage.value = "Error: ${e.message}"
                _isSuccess.value = false
            }
        }
    }


    fun checkUserVotedStatus(electionId: Int) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email ?: ""

        if (email.isEmpty()) return

        viewModelScope.launch {
            try {
                val response = api.checkHasVoted(userId = 1, electionId = electionId)
                _hasVoted.value = response.hasVoted
            } catch (e: Exception) {
                _hasVoted.value = false
            }
        }
    }

}