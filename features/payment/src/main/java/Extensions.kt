package com.magicpark.features.payment

enum class PaymentStatusEnum(val value: String) {
    SUCCESS("success"),
    FAILED("failed"),
    ERROR("error"),
    UNKNOWN("unknown"),
}
