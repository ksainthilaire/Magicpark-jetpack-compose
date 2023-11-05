package com.magicpark.core

object Config {

    const val API_URL = "http://192.168.1.31:8098"

    /*
        Here are the payment urls the customer is directed
        to based on success, failure, or cancellation.
     */

    const val paymentSuccessUrl = "https://magicpark-gn.com/?success"
    const val paymentErrorUrl = "https://magicpark-gn.com/?cancel"
    const val paymentCancelUrl = "https://magicpark-gn.com/?cancel"


    /*
        FB
     */
    const val SERVER_CLIENT_ID = "466002208137-o6rs77cfkvtqiat9tbciot15ghlgd9vj.apps.googleusercontent.com"


    const val KEY_SHARED_PREFERENCES = "KEY-MAGICPARK"
}
