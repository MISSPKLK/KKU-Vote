package com.example.project_election.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.project_election.RetrofitClient
import com.example.project_election.models.ElectionStats
import java.util.Calendar

class StatsViewModel : ViewModel() {


    private val api = RetrofitClient.api

    private val _stats = mutableStateOf<ElectionStats?>(null)
    val stats: State<ElectionStats?> = _stats

    // รับปี พ.ศ. แปลงเป็น ค.ศ. แล้วส่ง API
    fun loadStats(yearBE: Int) {
        val yearCE = yearBE - 543

        viewModelScope.launch {
            try {
                val result = api.getStatsByYear(yearCE)
                _stats.value = result
            } catch (e: Exception) {
                _stats.value = null
                e.printStackTrace()
            }
        }
    }

    fun getCurrentYearBE(): Int {
        return Calendar.getInstance().get(Calendar.YEAR) + 543
    }


}