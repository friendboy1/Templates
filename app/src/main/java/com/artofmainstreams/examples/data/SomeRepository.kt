package com.artofmainstreams.examples.data

import android.content.Context
import javax.inject.Inject

interface SomeRepository {
    suspend fun getSomething(someArgument: String): List<GitHubRepo>
}

class SomeRepositoryImpl @Inject constructor(
    private val someService: SomeService,
    private val analytics: Analytics,
) : SomeRepository {
    override suspend fun getSomething(someArgument: String): List<GitHubRepo> {
        analytics.trackNewsRequest(someArgument)
        return someService.getSomething(someArgument)
    }
}

enum class Status { LOADING, DONE, ERROR }

class ResourceManager @Inject constructor(private val context: Context)