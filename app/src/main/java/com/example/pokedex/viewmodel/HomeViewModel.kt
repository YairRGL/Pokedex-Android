package com.example.pokedex.viewmodel



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.model.PokemonByIdResponse
import com.example.pokedex.data.repository.PokemonRepository

class HomeViewModel : ViewModel() {

    private val _pokemonLiveData = MutableLiveData<PokemonByIdResponse>()
    val pokemonLiveData: LiveData<PokemonByIdResponse>
        get() = _pokemonLiveData

    private val pokemonRepository = PokemonRepository()

    fun getPokemonById(id: Int) {
        pokemonRepository.getPokemonById(id) { pokemonResponse ->
            if (pokemonResponse != null) {
                _pokemonLiveData.postValue(pokemonResponse)
            } else {
                // error
            }
        }
    }
}