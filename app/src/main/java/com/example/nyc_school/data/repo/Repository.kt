package com.example.nyc_school.data.repo

import com.example.nyc_school.data.model.SchoolModel
import com.example.nyc_school.data.model.ScoreModel
import com.example.nyc_school.data.network.response.ApiResponse

interface Repository {

    suspend fun getSchoolInfo(): ApiResponse<List<SchoolModel>>

    suspend fun getByDBN(dbn: String): ApiResponse<List<SchoolModel>>

    suspend fun getScore(dbn: String): ApiResponse<List<ScoreModel>>

    suspend fun getByName(schoolName: String): ApiResponse<List<SchoolModel>>
}