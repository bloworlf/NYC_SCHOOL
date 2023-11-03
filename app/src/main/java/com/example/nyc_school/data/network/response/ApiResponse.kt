package com.example.nyc_school.data.network.response

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

data class ApiResponse<T>(
    val status: ResponseStatus = ResponseStatus(),
    val content: T? = null
)

