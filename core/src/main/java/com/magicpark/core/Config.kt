package com.magicpark.core

object Config {

    const val apiUrl = "https://api.themoviedb.org/3/"
    const val assetsUrl = "https://image.tmdb.org/t/p/w500/"

    const val tmdbApiKeyV3 = "76909ebd17e275d556f8e0ed68bbc2b5"
    const val tmdbApiKeyV4 =
        "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3NjkwOWViZDE3ZTI3NWQ1NTZmOGUwZWQ2OGJiYzJiNSIsInN1YiI6IjYzOTljOWExNzdjMDFmMDBhMjE0NGRkNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.P4cLbLgDuQ93kq5fPWj84ioH3pbpaMFx8P1bU9Sc7VU"

    /*
        Here are the payment urls the customer is directed
        to based on success, failure, or cancellation.
     */

    const val paymentSuccessUrl = "https://magicpark-gn.com/?success"
    const val paymentErrorUrl = "https://magicpark-gn.com/?cancel"
    const val paymentCancelUrl = "https://magicpark-gn.com/?cancel"
}