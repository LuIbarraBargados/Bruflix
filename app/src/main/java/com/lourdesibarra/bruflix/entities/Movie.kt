package com.lourdesibarra.bruflix.entities


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Movie(
    val id: Int,
    val title: String? = null,
    val genre: String? = null,
    val backdropPath: String? = null,
    val posterPath: String? = null,
    val overview: String? = null,
    val releaseDate: String? = null,
    var isSubscribed: Boolean = false
) : Parcelable