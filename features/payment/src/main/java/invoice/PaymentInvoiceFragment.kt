package com.magicpark.features.payment.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.model.getAmount
import com.magicpark.domain.model.getDate
import com.magicpark.domain.model.getPaymentMethod
import com.magicpark.features.payment.PaymentStatusEnum
import com.magicpark.features.payment.PaymentStatusEnum.ERROR
import com.magicpark.features.payment.PaymentStatusEnum.FAILED
import com.magicpark.features.payment.PaymentStatusEnum.SUCCESS
import com.magicpark.features.payment.PaymentStatusEnum.UNKNOWN
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithoutParameter
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
private fun PaymentInvoiceScreenPreview() {
    MagicparkMaterialTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            PaymentInvoiceScreen(
                onBackPressed = {},
                goToShop = {},
                goToCart = {},
            )
        }
    }
}

@Composable
private fun PaymentStatusEnum.Headline() {
    when (this) {

        FAILED -> Text(
            text = stringResource(R.string.payment_failed),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Red
        )

        SUCCESS -> Text(
            text = stringResource(R.string.payment_successful),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Green
        )

        ERROR -> Text(
            text = stringResource(R.string.payment_error),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Red
        )

        else -> {
            Text(
                text = stringResource(R.string.payment_invoice),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )
        }
    }
}

private val PaymentStatusEnum.button: Int
    get() {
        return when (this) {
            FAILED ->
                R.string.payment_cart_button

            SUCCESS ->
                R.string.payment_shop_button

            ERROR ->
                R.string.payment_cart_button

            UNKNOWN ->
                R.string.payment_settings_button
        }
    }

/**
 * The animation on the invoice screen after payment is made.
 */
val PaymentStatusEnum.animationRes: Int
    get() = when (this) {
        SUCCESS ->
            R.raw.lottie_payment_success

        FAILED,
        ERROR ->
            R.raw.lottie_payment_error

        UNKNOWN ->
            R.raw.lottie_payment_success
    }


/**
 * @param onBackPressed
 * @param goToCart
 * @param goToShop
 */
@Composable
fun PaymentInvoiceScreen(
    onBackPressed: CallbackWithoutParameter,
    goToShop: CallbackWithoutParameter,
    goToCart: CallbackWithoutParameter,
) {
    val viewModel: PaymentInvoiceViewModel = getViewModel()
    val paymentStatus = viewModel.paymentStatus
    val invoice = viewModel.invoice

    val onClick = when (paymentStatus) {
        ERROR, FAILED -> goToCart
        SUCCESS -> goToShop
        UNKNOWN -> onBackPressed
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                paymentStatus.animationRes
            )
        )

        Box(
            modifier = Modifier
                .padding(top = 96.dp)
                .wrapContentSize(),
        ) {
            LottieAnimation(
                modifier = Modifier
                    .size(200.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
        }

        paymentStatus.Headline()

        Text(
            modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp),
            text = stringResource(R.string.payment_processed),
            style = TextStyle(
                fontSize = 16.sp,
            ),
            color = MagicparkTheme.colors.primary,
            textAlign = TextAlign.Center
        )


        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = stringResource(R.string.payment_transaction_number),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
        )

        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.payment_total_amount)
                    .uppercase(),
                style = TextStyle(
                    fontSize = 16.sp,
                ),
            )
            Text(text = invoice.getAmount())
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.payment_payed_by)
                    .uppercase(),
                style = TextStyle(
                    fontSize = 16.sp,
                ),
            )
            Text(text = invoice.getPaymentMethod())
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.payment_transaction_date)
                    .uppercase(),
                style = TextStyle(
                    fontSize = 16.sp,
                ),
            )
            Text(text = invoice.getDate())
        }

        Button(
            modifier = Modifier.padding(vertical = 48.dp),
            onClick = onClick,
        ) {
            Text(text = stringResource(paymentStatus.button))
        }
    }
}
