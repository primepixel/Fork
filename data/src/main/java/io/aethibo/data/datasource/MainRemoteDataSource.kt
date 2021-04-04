package io.aethibo.data.datasource

import io.aethibo.data.utils.Resource
import io.aethibo.domain.AccessTokenResponse
import io.aethibo.domain.User

interface MainRemoteDataSource {

    /**
     * Authorization
     */
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        code: String
    ): Resource<AccessTokenResponse>

    // Current logged in user
    suspend fun getUserInfo(): Resource<User>
}