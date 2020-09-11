package com.androidarchitecture.data.repository

import com.androidarchitecture.data.repository.base.BaseRestApiRepository
import com.androidarchitecture.data.retrofit.ApiService
import com.androidarchitecture.domain.models.Result
import com.androidarchitecture.domain.models.User
import com.androidarchitecture.domain.repository.UserRepository
import javax.inject.Inject

class RetrofitUsersRepository @Inject constructor(
    private val api: ApiService
) : UserRepository,
    BaseRestApiRepository() {

    override suspend fun getUsers(): Result<List<User>> {
        return parseResult(api.getUsers()) { response -> response.users.map { it.toUser() } }
    }

    override suspend fun getUserDetail(id: Int): Result<User> {
        return parseResult(api.getUserDetail(id)) { it.data.toUser() }
    }
}
