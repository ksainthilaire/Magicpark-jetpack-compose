package com.magicpark.domain.model

enum class MediaTypeEnum(val value: String){
    // Include all movies, TV shows and people in the results as a global trending list.
    ALL("all"),
    // Show the trending movies in the results.
    MOVIE("movie"),
    // Show the trending TV shows in the results.
    TV("tv"),
    // Show the trending people in the results.
    PERSON("person")
}