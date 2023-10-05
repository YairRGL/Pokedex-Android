package com.example.pokedex.view.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.pokedex.R
import com.example.pokedex.databinding.FragmentHomeBinding
import com.example.pokedex.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnGetPokemon = view.findViewById(R.id.btnGetPokemon) as Button
        val txtViewPokemonName = view.findViewById(R.id.textViewPokemonName) as TextView
        val txtViewPokemonNumber = view.findViewById(R.id.textViewPokemonNumber) as TextView
        val txtViewPokemonHeight = view.findViewById(R.id.textViewPokemonHeight) as TextView
        val txtViewPokemonWeight = view.findViewById(R.id.textViewPokemonWeight) as TextView
        val imageViewPokemon = view.findViewById(R.id.imageViewPokemon) as ImageView
        val cardViewPokemon = view.findViewById(R.id.cardViewPokemon) as CardView

        Glide.with(this)
            .load(R.drawable.ic_default_pokemon)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageViewPokemon)

        if (arguments?.getString("data") == "newPokemon") {
            getRandomPokemon()
        }

        viewModel.pokemonLiveData.observe(viewLifecycleOwner) { pokemonReponse ->
            val pokemonImg = pokemonReponse?.sprites?.other?.dreamWorld?.frontDefault
            if (!pokemonImg.isNullOrEmpty()) {
                txtViewPokemonNumber.text = getString(R.string.string_pokemon_number_home, pokemonReponse.id.toString())
                txtViewPokemonName.text = pokemonReponse.name
                txtViewPokemonHeight.text = getString(R.string.string_pokemon_height_format, pokemonReponse.height.toString())
                txtViewPokemonWeight.text = getString(R.string.string_pokemon_weight_format, pokemonReponse.weight.toString())

                Glide.with(this)
                    .load(pokemonImg).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            //error
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Palette.from(resource!!.toBitmap()).generate() { palette ->
                                palette?.let {
                                    val cardColor = it.lightVibrantSwatch?.rgb?: 0
                                    cardViewPokemon.setCardBackgroundColor(cardColor)
                                }
                            }
                            return false
                        }

                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPokemon)

            } else {
                Toast.makeText(requireContext(), getString(R.string.string_try_again), Toast.LENGTH_SHORT).show()
            }
        }


        btnGetPokemon.setOnClickListener {
            viewModel.getPokemonById(getRandomNumber())
        }
    }

    private fun getRandomPokemon() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getPokemonById(getRandomNumber())
            Toast.makeText(requireContext(), getString(R.string.string_new_pokemon), Toast.LENGTH_SHORT).show()
        }
    }

    fun getRandomNumber(): Int {
        return Random.nextInt(1, 1018)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        arguments = null
    }
}