package com.example.nyc_school.data.repo

import com.example.nyc_school.data.network.response.ApiResponse

interface Repository {

    suspend fun getSchoolInfo(): ApiResponse

    suspend fun getByDBN(dbn: String): ApiResponse

    suspend fun getScore(dbn: String): ApiResponse

    suspend fun getByName(schoolName: String): ApiResponse
}