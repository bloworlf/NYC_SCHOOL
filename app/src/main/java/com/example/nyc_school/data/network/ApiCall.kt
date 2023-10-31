package com.example.nyc_school.data.network

import com.example.nyc_school.data.model.ScoreModel
import com.example.nyc_school.data.model.SchoolModel
import com.example.nyc_school.utils.Common
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCall {

    @GET(Common.API.SCHOOL_INFO)
    suspend fun getSchoolInfo(): List<SchoolModel>

    @GET(Common.API.SCHOOL_INFO)
    suspend fun getByDBN(
        @Query("dbn") dbn: String
    ): List<SchoolModel>

    @GET(Common.API.SAT_SCORE)
    suspend fun getScore(
        @Query("dbn") dbn: String
    ): List<ScoreModel>

    @GET(Common.API.SCHOOL_INFO)
    suspend fun getByName(
        @Query("school_name") schoolName: String
    ): List<SchoolModel>
}