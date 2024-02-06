package com.example.proyectoyoutube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoyoutube.DetailSuperheroActivity.Companion.EXTRA_ID
import com.example.proyectoyoutube.databinding.ActivitySuperHeroListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SuperHeroListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuperHeroListBinding
    private lateinit var retrofit: Retrofit

    private lateinit var adapter : SuperheroAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperHeroListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrofit = getRetrofit()

        instancias()

    }



    private fun instancias() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {

                searchByName(query.orEmpty())

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        adapter = SuperheroAdapter{navigateToDetail(it)}
        binding.recyclerViewSuperHero.setHasFixedSize(true)
        binding.recyclerViewSuperHero.layoutManager  = LinearLayoutManager(this)

        binding.recyclerViewSuperHero.adapter = adapter

    }

    private fun searchByName(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val myResponse: Response<SuperHeroDataResponse> = retrofit.create(ApiService::class.java).getSuperheroes(query)
            if (myResponse.isSuccessful) {
                val response: SuperHeroDataResponse? = myResponse.body()
                if (response != null) {
                    // Filtrar la lista según el requisito deseado (solo "Marvel Comics")
                    val filteredList = response.superheroes.filter { superhero ->
                        superhero.biography.publisher.equals("DC Comics", ignoreCase = true)
                    }

                    runOnUiThread {
                        adapter.updateList(filteredList)
                    }
                }
                Log.i("aristidevs", "Funciona")
            } else {
                Log.i("aristidevs", "No funciona")
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://www.superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    private fun navigateToDetail(id:String){

        val intent = Intent(this, DetailSuperheroActivity::class.java)

        intent.putExtra(EXTRA_ID, id)

        startActivity(intent)

    }

}