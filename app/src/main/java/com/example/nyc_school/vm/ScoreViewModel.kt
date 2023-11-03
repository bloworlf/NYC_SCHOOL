package com.example.nyc_school.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nyc_school.data.model.SchoolModel
import com.example.nyc_school.data.model.ScoreModel
import com.example.nyc_school.data.network.response.ApiResponse
import com.example.nyc_school.data.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val liveData: MutableLiveData<ApiResponse<List<ScoreModel>>> by lazy{
        MutableLiveData<ApiResponse<List<ScoreModel>>>()
    }

    fun getScore(dbn: String) {
        viewModelScope.launch {
            val value = repository.getScore(dbn)

            liveData.postValue(value)
        }
    }
}