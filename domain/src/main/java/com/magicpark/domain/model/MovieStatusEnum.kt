package com.magicpark.domain.model

enum class MovieStatusEnum(val value: String) {
    RUMORED("rumored"),
    PLANNED("planned"),
    IN_PRODUCTION("im_production"),
    POST_PRODUCTION("post_production"),
    RELEASED("released"),
    CANCELED("canceled")
}