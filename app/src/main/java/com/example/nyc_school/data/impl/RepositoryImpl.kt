package com.example.nyc_school.data.impl

import com.example.nyc_school.data.network.ApiCall
import com.example.nyc_school.data.network.response.ApiResponse
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.data.network.response.ResponseStatus
import com.example.nyc_school.data.repo.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiCall: ApiCall
) : Repository {

    override suspend fun getSchoolInfo(): ApiResponse {
        return try {
            val value = apiCall.getSchoolInfo().sortedBy { it.schoolName }
            ApiResponse(
                status = ResponseStatus(code = ErrorCode.SUCCESS),
                content = value
            )

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(
                status = ResponseStatus(code = ErrorCode.ERROR, message = e.message)
            )
        }
    }

    override suspend fun getByDBN(dbn: String): ApiResponse {
        return try {
            val value = apiCall.getByDBN(dbn)/*.sortedBy { it.schoolName }*/
            ApiResponse(
                status = ResponseStatus(code = ErrorCode.SUCCESS),
                content = value
            )

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(
                status = ResponseStatus(code = ErrorCode.ERROR, message = e.message)
            )
        }
    }

    override suspend fun getByName(schoolName: String): ApiResponse {
        return try {
            val value = apiCall.getByName(schoolName).sortedBy { it.schoolName }
            ApiResponse(
                status = ResponseStatus(code = ErrorCode.SUCCESS),
                content = value
            )

        } catch (e: Exception) {
            e.printStackTrace()
            ApiResponse(
                status = ResponseStatus(code = ErrorCode.ERROR, message = e.message)
            )
        }
    }
}