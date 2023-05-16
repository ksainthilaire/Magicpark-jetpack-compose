package com.magicpark.data.model.response

import com.magicpark.domain.model.Voucher

data class VouchersResponse(
    val vouchers: List<Voucher>? = null
)