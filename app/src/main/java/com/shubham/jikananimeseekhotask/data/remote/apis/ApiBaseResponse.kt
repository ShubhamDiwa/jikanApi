package com.shubham.jikananimeseekhotask.data.remote.apis
data class ApiBaseResponse<T>(
    val status: String,
    val message: String,
    val response: T
)
