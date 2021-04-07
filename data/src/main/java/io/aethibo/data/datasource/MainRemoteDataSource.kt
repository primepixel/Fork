package io.aethibo.data.datasource

import io.aethibo.data.utils.Resource
import io.aethibo.domain.User
import io.aethibo.domain.response.AccessTokenResponse

interface MainRemoteDataSource {

    /**
     * Authorization
     */
    suspend fun getAccessToken(params: Map<String, String>): Resource<AccessTokenResponse>

    // Current logged in user
    suspend fun getUserInfo(token: String): Resource<User>
}