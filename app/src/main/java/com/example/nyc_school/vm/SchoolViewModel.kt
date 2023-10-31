package com.example.nyc_school.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyc_school.data.network.response.ApiResponse
import com.example.nyc_school.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val liveData: MutableLiveData<ApiResponse> by lazy {
        MutableLiveData<ApiResponse>()
    }

    val isRefreshing: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun getSchoolInfo() {
        viewModelScope.launch {
            isRefreshing.value = true
            val value = repository.getSchoolInfo()

            liveData.postValue(value)
            isRefreshing.value = false
        }
    }

    fun getByDBN(dbn: String) {
        viewModelScope.launch {
            isRefreshing.value = true
            val value = repository.getByDBN(dbn)

            liveData.postValue(value)
            isRefreshing.value = false
        }
    }

    fun getByName(name: String) {
        viewModelScope.launch {
            isRefreshing.value = true
            val value = repository.getByName(name)

            liveData.postValue(value)
            isRefreshing.value = false
        }
    }
}