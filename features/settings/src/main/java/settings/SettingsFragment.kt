package settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Size
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.enums.UserRank
import com.magicpark.utils.R
import com.magicpark.utils.ui.Alert
import com.magicpark.utils.ui.CallbackWithoutParameter
import org.koin.androidx.compose.getViewModel

@Composable
fun Title(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
        style = TextStyle(
            fontWeight = FontWeight.Bold
        ),
        fontSize = 24.sp
    )
}

/**
 * @param drawableId ID of the drawable to display to the left of the MenuItem.
 * @param text Text displayed in the menu.
 * @param onClick Function called when the user presses the MenuItem.
 */
@Composable
private fun MenuItem(
    @DrawableRes drawableId: Int,
    text: String,
    onClick: CallbackWithoutParameter
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painterResource(id = drawableId),
            modifier = Modifier
                .size(14.dp),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )

        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                color = MagicparkTheme.colors.primary,
            )
        )
    }
}


@Preview
@Composable
fun SettingScreen_Preview() =
    SettingsScreen(
        onBackPressed = { /*TODO*/ },
        logout = { /*TODO*/ },
        deleteAccount = { /*TODO*/ },
        goToAccountSettings = { /*TODO*/ },
        goToContact = { /*TODO*/ },
        goToEditPassword = {},
        goToPrivacyPolicy = {})

/**
 *
 * @param onBackPressed  listener go back
 *
 * @param logout Log out user
 * @param deleteAccount Delete user account
 *
 * @param goToAccountSettings Go to account settings
 * @param goToContact Go to contact screen
 * @param goToPrivacyPolicy Go to privacy policy
 */
@Composable
fun SettingsScreen(
    onBackPressed: CallbackWithoutParameter,

    logout: CallbackWithoutParameter,
    deleteAccount: CallbackWithoutParameter,

    goToAccountSettings: CallbackWithoutParameter,
    goToContact: CallbackWithoutParameter,
    goToPrivacyPolicy: CallbackWithoutParameter,
    goToEditPassword: CallbackWithoutParameter,
) {

    val viewModel: SettingsViewModel = getViewModel()
    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()

    val openAccountDeletionDialog = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 24.dp,
                start = 32.dp,
                end = 32.dp,
                bottom = 24.dp,
            )
    ) {

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, MagicparkTheme.colors.primary, CircleShape)
                    ) {

                        val painter = if (!user.avatarUrl.isNullOrEmpty()) {
                            rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .memoryCachePolicy(CachePolicy.ENABLED)
                                    .diskCachePolicy(CachePolicy.ENABLED)
                                    .decoderFactory(SvgDecoder.Factory())
                                    .data(user.avatarUrl)
                                    .size(Size.ORIGINAL)
                                    .build(), ImageLoader(LocalContext.current)
                            )
                        } else painterResource(R.drawable.illustration_elephant)

                        Image(
                            painter = painter,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = user.fullName.toString()
                )
            }

            user.avatarUrl?.let { Spacer(Modifier.padding(vertical = 24.dp)) } ?: Alert(
                modifier = Modifier.padding(vertical = 24.dp),
                text = stringResource(
                    R.string.settings_upload_picture
                ),
                backgroundColor = Color.Red,
                textColor = Color.White
            )

            MenuItem(
                drawableId = R.drawable.ic_edit_user,
                text = stringResource(id = R.string.settings_menu_edit_account),
                onClick = goToAccountSettings,
            )

            MenuItem(
                drawableId = R.drawable.ic_user,
                text = stringResource(id = R.string.settings_menu_edit_password),
                onClick = goToEditPassword,
            )

            MenuItem(
                drawableId = R.drawable.ic_trash,
                text = stringResource(id = R.string.settings_menu_delete_account),
                onClick = {
                    openAccountDeletionDialog.value = true
                },
            )

            Title(text = stringResource(R.string.settings_menu_legal))

            MenuItem(
                drawableId = R.drawable.ic_support,
                text = stringResource(id = R.string.settings_menu_contact),
                onClick = goToContact,
            )

            MenuItem(
                drawableId = R.drawable.ic_tos,
                text = stringResource(id = R.string.settings_menu_terms),
                onClick = goToPrivacyPolicy,
            )

            when (user.role) {

                UserRank.ADMINISTRATOR -> {
                    Title(text = stringResource(id = R.string.settings_menu_administration))
                    MenuItem(
                        drawableId = R.drawable.ic_admin_qr,
                        text = stringResource(id = R.string.settings_menu_administration),
                        onClick = {},
                    )
                    MenuItem(
                        drawableId = R.drawable.ic_bo,
                        text = stringResource(id = R.string.settings_menu_back_office),
                        onClick = {},
                    )
                }

                else -> Unit
            }

            val context = LocalContext.current

            Button(
                modifier = Modifier
                    .padding(top = 64.dp),
                onClick = { viewModel.logout(context) }
            ) {
                Image(
                    painterResource(R.drawable.ic_logout),
                    modifier = Modifier.size(24.dp),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(Color.White),
                )
                Text(
                    text = stringResource(R.string.settings_menu_logout),
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .offset(x = (-12).dp),
                )
            }
        }
    }

    val context = LocalContext.current

    if (openAccountDeletionDialog.value) {
        AccountDeletionDialog(
            isLoading = state is SettingsState.AccountDeletingProgress,
            onDismissRequest = {
                openAccountDeletionDialog.value = false
            },
            onConfirmation = {
                viewModel.deleteAccount()
            })
    }
}

@Preview
@Composable
fun AccountDeletionDialog(
    onDismissRequest: (() -> Unit)? = null,
    onConfirmation: (() -> Unit)? = null,
    isLoading: Boolean = false,
) {
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
            )
        },
        title = {
            Text(text = stringResource(id = R.string.settings_menu_delete_account))
        },
        text = {
            Text(text = stringResource(R.string.deletion_text))
        },
        onDismissRequest = {
            onDismissRequest?.invoke()
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirmation?.invoke()
                }
            ) {
                Text(text = stringResource(id = R.string.common_button_delete))

                if (isLoading)
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(16.dp)
                            .aspectRatio(1f),
                        color = Color.White,
                    )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismissRequest?.invoke()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            ) {
                Text(text = stringResource(id = R.string.common_button_cancel))
            }
        }
    )
}
