package settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R
import com.magicpark.domain.enums.UserRank
import com.magicpark.domain.model.User
import com.magicpark.utils.ui.Alert
import com.magicpark.utils.ui.CallbackWithoutParameter
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

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

                    SettingsScreen(
                        user = user,

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


    @SuppressLint("InflateParams")
    private fun deleteAccount() {
        val context = requireContext()
        val activity = requireActivity()
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(com.magicpark.features.settings.R.layout.dialog_delete, null))
            .setPositiveButton(R.string.common_button_delete) { _, _ ->
                viewModel.deleteAccount(context)
            }
            .setNegativeButton(R.string.common_button_cancel) { dialog, _ ->
                dialog.cancel()
            }
        builder.create().show()
    }
}

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
 * @param id ID of the drawable to display to the left of the MenuItem.
 * @param text Text displayed in the menu.
 * @param listener Function called when the user presses the MenuItem.
 */
@Composable
private fun MenuItem(
    @DrawableRes id: Int,
    text: String,
    listener: CallbackWithoutParameter
) {
    Row(
        modifier = Modifier.clickable { listener.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = id),
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
                color = MagicparkTheme.colors.primary
            )
        )
    }
}


@Preview
@Composable
fun SettingScreen_Preview() =
    SettingsScreen(
        user = User(),
        onBackPressed = { /*TODO*/ },
        logout = { /*TODO*/ },
        deleteAccount = { /*TODO*/ },
        goToAccountSettings = { /*TODO*/ },
        goToContact = { /*TODO*/ }) {
    }

/**
 *
 * @param user User information
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
    user: User,
    onBackPressed: CallbackWithoutParameter,

    logout: CallbackWithoutParameter,
    deleteAccount: CallbackWithoutParameter,

    goToAccountSettings: CallbackWithoutParameter,
    goToContact: CallbackWithoutParameter,
    goToPrivacyPolicy: CallbackWithoutParameter,
) {

    Image(
        painter = painterResource(id = R.drawable.ic_back),
        modifier = Modifier
            .clickable { }
            .width(100.dp)
            .height(50.dp)
            .padding(
                top = MagicparkTheme.defaultPadding,
                end = MagicparkTheme.defaultPadding
            )
            .clickable {
                onBackPressed()
            },
        contentDescription = null,
        colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 50.dp,
                start = 32.dp,
                end = 32.dp
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
                            contentDescription = "avatar",
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
                Text(modifier = Modifier.padding(top = 10.dp), text = user.fullName.toString())
            }

            Spacer(Modifier.height(10.dp))

            user.avatarUrl?.let {} ?: Alert(
                stringResource(
                   R.string.settings_upload_picture
                ),
                Color.Red,
                Color.White
            )


            Title("Mon compte")
            MenuItem(R.drawable.ic_edit_user, "Modifier mon profil", goToAccountSettings)
            MenuItem(R.drawable.ic_support, "Nous contacter", goToContact)
            MenuItem(R.drawable.ic_trash, "Supprimer mon compte", deleteAccount)

            Title("Légal")
            MenuItem(R.drawable.ic_tos, "Conditions générales", goToPrivacyPolicy)

            if (user.role == UserRank.ADMINISTRATOR) {
                Title("Administration")
                MenuItem(R.drawable.ic_admin_qr, "Contrôler un ticket") {}
                MenuItem(R.drawable.ic_bo, "Back-office") {}
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .padding(top = 50.dp)
            ) {
                Image(
                    painterResource(R.drawable.ic_logout),
                    modifier = Modifier.size(24.dp),
                    contentDescription = "drawable icons",
                    colorFilter = ColorFilter.tint(Color.White)
                )
                Text(
                    text = "Se déconnecter",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .offset(x = (-12).dp)
                        .clickable {
                            logout()
                        }
                )
            }
        }
    }
}
