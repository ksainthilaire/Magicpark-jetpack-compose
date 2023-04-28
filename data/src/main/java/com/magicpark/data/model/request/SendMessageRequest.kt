package com.magicpark.data.model.request

data class SendMessageRequest(
    val text: String? = null
) : Request()