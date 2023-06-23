package com.artofmainstreams.examples.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path

interface SomeService {
    @GET("users/{user}/repos")
    suspend fun getSomething(@Path("user") id: String): List<GitHubRepo>
}

data class GitHubRepo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)