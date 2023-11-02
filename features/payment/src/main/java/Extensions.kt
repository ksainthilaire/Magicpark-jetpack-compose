package com.magicpark.features.payment

import com.magicpark.utils.R

enum class PaymentStatusEnum(val value: String) {
    SUCCESS("success"),
    FAILED("failed"),
    ERROR("error")
}

/**
 * Payment status text on invoice screen after payment.
 */
val PaymentStatusEnum.statusText: Int
    get() = when (this) {
        PaymentStatusEnum.SUCCESS -> 0
        PaymentStatusEnum.ERROR -> 0
        PaymentStatusEnum.FAILED -> 0
    }


/**
 * Button text on invoice screen after payment.
 */
val PaymentStatusEnum.buttonText: Int
    get() = when (this) {
        PaymentStatusEnum.SUCCESS -> 0
        PaymentStatusEnum.ERROR -> 0
        PaymentStatusEnum.FAILED -> 0
    }

/**
 * The animation on the invoice screen after payment is made.
 */
val PaymentStatusEnum.animationRes: Int
    get() = when (this) {
        PaymentStatusEnum.SUCCESS ->
            R.raw.lottie_payment_success

        PaymentStatusEnum.FAILED,
        PaymentStatusEnum.ERROR ->
            R.raw.lottie_payment_error
    }
