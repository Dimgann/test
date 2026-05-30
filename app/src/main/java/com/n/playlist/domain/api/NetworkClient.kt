package com.kotler.playlist.domain.api

import com.kotler.playlist.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}