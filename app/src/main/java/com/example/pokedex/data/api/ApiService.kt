package com.example.pokedex.data.api

import com.example.pokedex.data.model.PokemonByIdResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {
    @GET("pokemon/{id}")
    fun getPokemonById(
        @Path("id") id: Int
    ): Call<PokemonByIdResponse>

}