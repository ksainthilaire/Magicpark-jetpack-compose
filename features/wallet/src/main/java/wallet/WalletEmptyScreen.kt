package wallet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.magicpark.utils.R


@Composable
@Preview
fun WalletEmptyScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.lottie_wallet_empty))

        Box(modifier = Modifier.wrapContentSize()) {
            LottieAnimation(
                modifier = Modifier
                    .size(200.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
        }

        Text(
            modifier = Modifier.padding(top = 32.dp, start = 32.dp, end = 32.dp),
            textAlign = TextAlign.Justify,
            text = stringResource(R.string.wallet_empty_text),
        )


    }
}
