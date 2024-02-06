package com.example.proyectoyoutube

import com.google.gson.annotations.SerializedName

data class SuperHeroDataResponse (
    @SerializedName("response") val response: String,
    @SerializedName("results") val superheroes: List<SuperHeroItemResponse>
)

data class SuperHeroItemResponse(
    @SerializedName("id") val superheroId  : String,
    @SerializedName("name") val name : String,
    @SerializedName("image") val superheroImage : SuperHeroImageResponse,
    @SerializedName("biography") val biography: SuperHeroBiographyResponse
)

data class SuperHeroImageResponse(
    @SerializedName("url") val url : String
)

data class SuperHeroBiographyResponse(
    @SerializedName("publisher") val publisher: String
)

