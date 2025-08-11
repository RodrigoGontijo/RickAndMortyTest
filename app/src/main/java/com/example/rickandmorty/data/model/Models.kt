package com.example.rickandmorty.data.model

import com.squareup.moshi.Json

data class CharactersResponse(
    @Json(name = "info") val info: Info,
    @Json(name = "results") val results: List<Character>
)

data class Info(
    @Json(name = "count") val count: Int,
    @Json(name = "pages") val pages: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "prev") val prev: String?
)

data class Character(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "status") val status: String,
    @Json(name = "species") val species: String,
    @Json(name = "type") val type: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "image") val image: String,
    @Json(name = "origin") val origin: LocationRef,
    @Json(name = "location") val location: LocationRef
)

data class LocationRef(
    @Json(name = "name") val name: String,
    @Json(name = "url") val url: String
)
