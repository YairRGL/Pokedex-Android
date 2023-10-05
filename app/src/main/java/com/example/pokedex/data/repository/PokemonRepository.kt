package com.example.pokedex.data.repository

import com.example.pokedex.data.api.ApiService
import com.example.pokedex.data.api.RetrofitClient
import com.example.pokedex.data.model.PokemonByIdResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository {
    private val retrofitClient = RetrofitClient().getClient()
    private val apiService = retrofitClient.create(ApiService::class.java)

    fun getPokemonById(pokemonId: Int, callback: (PokemonByIdResponse?) -> Unit) {
        val call = apiService.getPokemonById(pokemonId)

        call.enqueue(object : Callback<PokemonByIdResponse> {
            override fun onResponse(call: Call<PokemonByIdResponse>, response: Response<PokemonByIdResponse>) {
                if (response.isSuccessful) {
                    val pokemon = response.body()
                    callback(pokemon)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<PokemonByIdResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
