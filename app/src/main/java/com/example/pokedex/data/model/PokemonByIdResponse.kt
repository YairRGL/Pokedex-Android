package com.example.pokedex.data.model

import com.google.gson.annotations.SerializedName

data class PokemonByIdResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("sprites") val sprites: Sprites,
    @SerializedName("height") val height: Int,
    @SerializedName("weight") val weight: Int

)

data class Sprites(
    @SerializedName("other") val other: Other
)

data class Other(
    @SerializedName("home") val dreamWorld: DreamWorld
)

data class DreamWorld(
    @SerializedName("front_default") val frontDefault: String
)
