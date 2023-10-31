package com.example.nyc_school.data.network.response

import com.example.nyc_school.data.model.school.SchoolModel

data class ResponseStatus(
    val code: ErrorCode = ErrorCode.UNDEFINED,
    val message: String? = null
)

enum class ErrorCode{
    SUCCESS,
    FAILED,
    ERROR,
    UNDEFINED
}

data class ApiResponse(
    val status: ResponseStatus = ResponseStatus(),
    val content: List<SchoolModel>? = null
)

