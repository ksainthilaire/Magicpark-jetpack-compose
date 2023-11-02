package com.magicpark.features.login.forgot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.facebook.bolts.Task.Companion.delay
import com.google.firebase.auth.FirebaseAuth
import com.magicpark.core.MagicparkTheme
import java.util.*
import com.magicpark.utils.R
import com.magicpark.utils.ui.CallbackWithoutParameter
import com.magicpark.utils.ui.CallbackWithParameter
import com.magicpark.utils.ui.ErrorSnackbar
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class ForgotFragment : Fragment() {

    companion object {
        private val TAG = ForgotFragment::class.java.simpleName
    }

    private val viewModel: ForgotViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val state by viewModel.state.collectAsState()

                    ForgotScreen(
                        state = state,
                        onBackPressed = this@ForgotFragment::onBackPressedListener,
                        onForgot = ::forgot,
                    )
                }
            }


    /**
     * User requests password reset.
     * @param mail User email
     */
    private fun forgot(mail: String)  {
        try {
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(mail)

            Log.i(TAG, "The user has been successfully created on Firebase.")
        } catch (e: Exception) {
            viewModel.handleForgotException(e)
        }
    }

    /**
     * The user wants to go back by clicking on the back button.
     */
    private fun onBackPressedListener() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }
}

@Preview
@Composable
fun ForgotScreen_Preview() =
    ForgotScreen(state = ForgotUiState.ForgotSuccessful, onBackPressed = {}, onForgot = {})

/**
 * @param state UI state
 * @param onBackPressed listener go back
 * @param onForgot listener called when user resets password
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotScreen(
    state: ForgotUiState,
    onBackPressed: CallbackWithoutParameter,
    onForgot: CallbackWithParameter<String>,
) {
    var mail by remember { mutableStateOf("") }

    Column(
        Modifier
            .padding(top = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Image(
            painter = painterResource(R.drawable.illustration_magicpark),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp),
            contentDescription = null,
        )

        Text(
            stringResource(id = R.string.forgot_title),
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp),
            style = TextStyle(textAlign = TextAlign.Center)
        )

        Text(
            stringResource(id = R.string.forgot_text),
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = MagicparkTheme.defaultPadding),
            style = TextStyle(textAlign = TextAlign.Center)
        )


        OutlinedTextField(
            modifier = Modifier.padding(horizontal = 24.dp),
            value = mail,
            singleLine = true,
            isError = state is ForgotUiState.ForgotFailed,
            onValueChange = { value -> mail = value },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent
            ),
            placeholder = { Text(stringResource(id = R.string.login_button_mail)) }
        )

        Button(
            enabled = true,
            onClick = { onForgot(mail) },
        ) {
            Text(text = stringResource(R.string.forgot_button_continue))
        }

        Button(
            onClick = onBackPressed,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = MagicparkTheme.colors.primary
            )
        ) {
            Text(text = stringResource(R.string.forgot_button_cancel))
        }

        if (state is ForgotUiState.ForgotFailed) {
            ErrorSnackbar(state.errorMessage) {}
            LaunchedEffect(key1 = state) { delay(2000L) }
        }
    }
}

