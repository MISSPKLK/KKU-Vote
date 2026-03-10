package com.example.project_election.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_election.models.Member
import com.example.project_election.models.Party
import com.example.project_election.models.PartyDetailResponse
import com.example.project_election.models.PartyMem
import com.example.project_election.models.PartyPosition
import com.example.project_election.models.Policy
import com.example.project_election.models.UpdateMemberRequest
import com.example.project_election.models.UserSearchResult
import com.example.project_election.network.PartyClient
import com.example.project_election.network.RetrofitInstance
import com.example.project_election.network.RetrofitInstance.api
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class PartyViewModel : ViewModel() {

    var partyList = mutableStateOf<List<Party>>(emptyList())
        private set

    var isLoading = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf("")
        private set

    var memberList = mutableStateListOf<Member>()
        private set

    var selectedParty by mutableStateOf<PartyDetailResponse?>(null)
        private set

    var policyList by mutableStateOf<List<Policy>>(emptyList())
        private set

    var positionList by mutableStateOf<List<PartyPosition>>(emptyList())

    var editingMember by mutableStateOf<Member?>(null)

    init {
        fetchParties()
        fetchMembers()
    }

    private fun fetchParties() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitInstance.api.getParties()
                partyList.value = response.filter { it.isDeleted == false }
            } catch (e: Exception) {
                errorMessage.value = "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้: ${e.message}"
                Log.e("API_ERROR", "Error fetching parties", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun loadMembersByParty(partyId: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = PartyClient.partyAPI.getMembersByParty(partyId)
                memberList.clear()
                memberList.addAll(response)
            } catch (e: Exception) {
                Log.e("PartyVM", e.message ?: "error")
            } finally {
                isLoading.value = true
            }
        }
    }

    fun loadPartyDetail(partyId: String) {
        viewModelScope.launch {
            try {
                isLoading.value = true
                selectedParty = PartyClient.partyAPI.getPartyDetail(partyId)
            } catch (e: Exception) {
                Log.e("PartyVM", e.message ?: "error")
            } finally {
                isLoading.value = true
            }
        }
    }

    fun loadPoliciesByParty(partyId: String) {
        viewModelScope.launch {
            try {
                val response = PartyClient.partyAPI.getPoliciesByParty(partyId)
                policyList = response
            } catch (e: Exception) {
                Log.e("API", "Load policy error: ${e.message}")
            }
        }
    }

    fun loadPositions() {
        viewModelScope.launch {
            try {
                positionList = PartyClient.partyAPI.getPartyPositions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createMemberMultipart(
        context: Context,
        imageUri: Uri?,
        userId: String,
        partyId: String,
        positionId: Int,
        prefix: String,
        firstName: String,
        lastName: String,
        memberNumber: String,
        biography: String,
        adminId: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val filePart = imageUri?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream!!.readBytes()
                    val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("file", "profile.jpg", requestFile)
                }

                val response = PartyClient.partyAPI.createMember(
                    userId.toRequestBody("text/plain".toMediaTypeOrNull()),
                    partyId.toRequestBody("text/plain".toMediaTypeOrNull()),
                    positionId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    prefix.toRequestBody("text/plain".toMediaTypeOrNull()),
                    firstName.toRequestBody("text/plain".toMediaTypeOrNull()),
                    lastName.toRequestBody("text/plain".toMediaTypeOrNull()),
                    memberNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                    biography.toRequestBody("text/plain".toMediaTypeOrNull()),
                    filePart,
                    adminId.toRequestBody("text/plain".toMediaTypeOrNull())
                )

                onResult(response.isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    var userSearchList by mutableStateOf<List<UserSearchResult>>(emptyList())

    fun searchUser(email: String) {
        viewModelScope.launch {
            try {
                userSearchList = PartyClient.partyAPI.searchUsers(email)
            } catch (e: Exception) {
                userSearchList = emptyList()
            }
        }
    }

    fun loadMemberById(memberId: Int) {
        viewModelScope.launch {
            try {
                val response = PartyClient.partyAPI.getMemberById(memberId)
                Log.d("DEBUG_API", "FULL OBJECT = $response")
                Log.d("DEBUG_API", "userId = ${response.userId}")
                Log.d("DEBUG_API", "email = ${response.email}")
                editingMember = response
            } catch (e: Exception) {
                Log.e("PartyVM", "Error: ${e.message}")
            }
        }
    }

    fun updateMemberMultipart(
        context: Context,
        memberId: Int,
        imageUri: Uri?,
        userId: String,
        partyId: String,
        positionId: Int,
        prefix: String,
        firstName: String,
        lastName: String,
        memberNumber: String,
        biography: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val isNewLocalImage = imageUri != null &&
                        imageUri.scheme != "http" &&
                        imageUri.scheme != "https"

                if (!isNewLocalImage) {
                    val request = UpdateMemberRequest(
                        Users_idUsers = userId,
                        Parties_idParties = partyId,
                        Party_positions_idParty_positions = positionId,
                        Party_prefix = prefix,
                        Party_Fname = firstName,
                        Party_Lname = lastName,
                        member_number = memberNumber,
                        biography = biography
                    )
                    val response = PartyClient.partyAPI.updateMember(memberId, request)
                    onResult(response.isSuccessful)
                } else {
                    val inputStream = context.contentResolver.openInputStream(imageUri!!)
                    val bytes = inputStream?.use { it.readBytes() }

                    if (bytes != null) {
                        val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                        val filePart = MultipartBody.Part.createFormData("file", "profile.jpg", requestFile)

                        val response = PartyClient.partyAPI.updateMemberMultipart(
                            memberId,
                            userId.toRequestBody("text/plain".toMediaTypeOrNull()),
                            partyId.toRequestBody("text/plain".toMediaTypeOrNull()),
                            positionId.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                            prefix.toRequestBody("text/plain".toMediaTypeOrNull()),
                            firstName.toRequestBody("text/plain".toMediaTypeOrNull()),
                            lastName.toRequestBody("text/plain".toMediaTypeOrNull()),
                            memberNumber.toRequestBody("text/plain".toMediaTypeOrNull()),
                            biography.toRequestBody("text/plain".toMediaTypeOrNull()),
                            filePart
                        )
                        onResult(response.isSuccessful)
                    } else {
                        onResult(false)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onResult(false)
            }
        }
    }

    fun deleteMember(memberId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                PartyClient.partyAPI.deleteMember(memberId)
                memberList.removeAll { it.id == memberId }
                onResult(true)
            } catch (e: Exception) {
                onResult(false)
            }
        }
    }

    //////////// aom add

    var partyName = mutableStateOf("")
    var partyNumber = mutableStateOf("")
    var partyPrefixName = mutableStateOf("")
    var description = mutableStateOf("")
    var logoUri = mutableStateOf<Uri?>(null)
    var posterUri = mutableStateOf<Uri?>(null)
    var duplicateError = mutableStateOf("")

    var partyMemList = mutableStateListOf<PartyMem>()
        private set

    fun getMemberCount(partyId: Int?): Int {
        if (partyId == null) return 0
        return partyMemList.count { it.Parties_idParties == partyId }
    }

    fun fetchMembers() {
        viewModelScope.launch {
            try {
                val response = api.getPartyMembers()
                partyMemList.clear()
                partyMemList.addAll(response)
            } catch (e: Exception) {
                Log.e("API", "Error fetching members", e)
            }
        }
    }

    fun addParty(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val name = partyName.value.toRequestBody("text/plain".toMediaType())
                val prefix = partyPrefixName.value.toRequestBody("text/plain".toMediaType())
                val number = partyNumber.value.toRequestBody("text/plain".toMediaType())
                val desc = description.value.toRequestBody("text/plain".toMediaType())

                val logoPart = logoUri.value?.let { uriToMultipart(context, it, "logo") }
                val posterPart = posterUri.value?.let { uriToMultipart(context, it, "image_poster") }

                RetrofitInstance.api.addParty(
                    partyName = name,
                    partyNumber = number,
                    partyPrefixName = prefix,
                    description = desc,
                    logo = logoPart,
                    image_poster = posterPart
                )
                fetchParties()

                partyName.value = ""
                partyNumber.value = ""
                description.value = ""
                logoUri.value = null
                posterUri.value = null

                onSuccess()
            } catch (e: Exception) {
                errorMessage.value = "เพิ่มพรรคไม่สำเร็จ: ${e.message}"
                Log.e("API_ERROR", "Error adding party", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun deleteParty(id: Int) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                RetrofitInstance.api.deleteParty(id)
                fetchParties()
            } catch (e: Exception) {
                errorMessage.value = "ลบพรรคไม่สำเร็จ: ${e.message}"
                Log.e("API_ERROR", "Error deleting party", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun uriToMultipart(context: Context, uri: Uri, fieldName: String): MultipartBody.Part {
        val stream = context.contentResolver.openInputStream(uri)!!
        val bytes = stream.readBytes()
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val requestBody = bytes.toRequestBody(mimeType.toMediaType())
        val ext = when (mimeType) {
            "image/png" -> "png"
            "image/webp" -> "webp"
            else -> "jpg"
        }
        val filename = "${fieldName}_${System.currentTimeMillis()}.$ext"
        return MultipartBody.Part.createFormData(fieldName, filename, requestBody)
    }
    var editingParty = mutableStateOf<Party?>(null)

    fun loadPartyById(id: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPartyById(id)
                editingParty.value = response
                // โหลดค่าเข้า field
                partyName.value = response.Party_name
                partyNumber.value = response.Party_number.toString()
                partyPrefixName.value = response.Party_PrefixName ?: ""
                description.value = response.Party_description ?: ""
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error loading party: ${e.message}")
            }
        }
    }

    fun updateParty(id: Int, context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val name = partyName.value.toRequestBody("text/plain".toMediaType())
                val prefix = partyPrefixName.value.toRequestBody("text/plain".toMediaType())
                val number = partyNumber.value.toRequestBody("text/plain".toMediaType())
                val desc = description.value.toRequestBody("text/plain".toMediaType())

                val logoPart = logoUri.value?.let { uri ->
                    if (uri.scheme != "http" && uri.scheme != "https")
                        uriToMultipart(context, uri, "logo") else null
                }
                val posterPart = posterUri.value?.let { uri ->
                    if (uri.scheme != "http" && uri.scheme != "https")
                        uriToMultipart(context, uri, "image_poster") else null
                }

                RetrofitInstance.api.updateParty(id, name, number, prefix, desc, logoPart, posterPart)
                fetchParties()
                onSuccess()
            } catch (e: Exception) {
                errorMessage.value = "อัปเดตพรรคไม่สำเร็จ: ${e.message}"
                Log.e("API_ERROR", "Error updating party", e)
            } finally {
                isLoading.value = false
            }
        }
    }
}