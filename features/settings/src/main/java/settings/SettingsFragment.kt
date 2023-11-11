package settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.magicpark.core.MagicparkTheme
import com.magicpark.domain.enums.UserRank
import com.magicpark.utils.R
import com.magicpark.utils.ui.Alert
import com.magicpark.utils.ui.CallbackWithoutParameter
import org.koin.androidx.compose.getViewModel

/*
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    val user by viewModel.user.collectAsState()

                    MagicparkMaterialTheme {
                        SettingsScreen(
                            onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() },
                            logout = {
                                viewModel.logout(context = requireContext())
                            },
                            deleteAccount = ::deleteAccount,
                            goToAccountSettings = {
                                navController.navigate("/account/settings")
                            },
                            goToPrivacyPolicy = {
                                navController.navigate("/privacy-policy")
                            },
                            goToContact = { navController.navigate("/contact") },
                        )
                    }
                }
            }


    @SuppressLint("InflateParams")
    private fun deleteAccount() {
        val context = requireContext()
        val activity = requireActivity()
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(
            inflater.inflate(
                com.magicpark.features.settings.R.layout.dialog_delete,
                null
            )
        )
            .setPositiveButton(R.string.common_button_delete) { _, _ ->
                viewModel.deleteAccount(context)
            }
            .setNegativeButton(R.string.common_button_cancel) { dialog, _ ->
                dialog.cancel()
            }
        builder.create().show()
    }
}

 */

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
        modifier = Modifier.clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painterResource(id = drawableId),
            modifier = Modifier
                .width(28.dp)
                .height(28.dp),
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
        goToContact = { /*TODO*/ }) {
    }

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
) {

    val viewModel: SettingsViewModel = getViewModel()
    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 64.dp,
                start = 32.dp,
                end = 32.dp,
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
                        Image(
                            painter = painterResource(R.drawable.illustration_elephant),
                            modifier = Modifier.fillMaxSize(),
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

            user.avatarUrl?.let {} ?: Alert(
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
                drawableId = R.drawable.ic_support,
                text = stringResource(id = R.string.settings_menu_contact),
                onClick = goToContact,
            )
            MenuItem(
                drawableId = R.drawable.ic_trash,
                text = stringResource(id = R.string.settings_menu_delete_account),
                onClick = deleteAccount,
            )

            Title(text = stringResource(R.string.settings_menu_legal))

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
                onClick = {
                          viewModel.logout(context)
                },
                modifier = Modifier
                    .padding(top = 64.dp),
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
                        .offset(x = (-12).dp)
                        .clickable { logout() },
                )
            }
        }
    }
}
