package com.kotler.playlist.data.network

import com.kotler.playlist.Storage
import com.kotler.playlist.domain.api.NetworkClient
import com.kotler.playlist.data.dto.TracksSearchRequest
import com.kotler.playlist.data.dto.TracksSearchResponse

class RetrofitNetworkClient(private val storage: Storage) : NetworkClient {

    override fun doRequest(request: Any): TracksSearchResponse {
        val searchList = storage.search((request as TracksSearchRequest).expression)
        return TracksSearchResponse(searchList).apply { resultCode = 200 }
    }
}