package com.magicpark.features.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R
import com.magicpark.utils.ui.Alert
import com.magicpark.utils.ui.OnLifecycleEvent


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

@Composable
fun MenuItem(
    @DrawableRes id: Int,
    text: String,
    listener: (() -> Unit)? = null
) {

    Row(
        modifier = Modifier.clickable { listener?.invoke() },
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
fun SettingsScreen(navController: NavController? = null, viewModel: SettingsViewModel, activity: AppCompatActivity) {

    val state by viewModel.state.collectAsState()
    val user by viewModel.user.collectAsState()


    if (state is SettingsState.LogoutSucceeded) {
        navController?.navigate("/login")
    }

    OnLifecycleEvent { _, event ->

        when (event) {
            Lifecycle.Event.ON_RESUME -> {
             //   viewModel.loadUser()
            }
            else -> {}
        }
    }



    val isAdministrator = user?.role?.equals("") ?: false

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
            // process eith the received image uri
        }


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
                navController?.popBackStack()
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

                Text(modifier = Modifier.padding(top = 10.dp), text = user?.fullName.toString())

            }

            Spacer(Modifier.height(10.dp))

            user?.avatarUrl?.let {} ?: Alert(
                stringResource(
                    com.magicpark.utils.R.string.settings_upload_picture
                ),
                Color.Red,
                Color.White
            )


            Title("Mon compte")
            MenuItem(R.drawable.ic_edit_user, "Modifier mon profil") {
                navController?.navigate("/account/update")
            }
            MenuItem(R.drawable.ic_support, "Nous contacter") {
                navController?.navigate("/support")
            }


            MenuItem(R.drawable.ic_trash, "Supprimer mon compte") {


                val builder = AlertDialog.Builder(activity)
                val inflater = activity.layoutInflater;
                builder.setView(inflater.inflate(com.magicpark.features.settings.R.layout.dialog_delete, null))
                    .setPositiveButton(com.magicpark.utils.R.string.common_button_delete) { _, _ ->
                      //  viewModel.delete()
                    }
                    .setNegativeButton(com.magicpark.utils.R.string.common_button_cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                builder.create().show()
            }


            Title("Légal")
            MenuItem(R.drawable.ic_tos, "Conditions générales") {
                navController?.navigate("/privacy-policy")
            }




            if (isAdministrator) {
                Title("Administration")
                MenuItem(R.drawable.ic_admin_qr, "Contrôler un ticket")
                MenuItem(R.drawable.ic_bo, "Back-office")
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .padding(top = 50.dp)
                    .clickable {
                        TODO("Login with facebook")
                    }
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

                            navController?.navigate("/login")
                        }
                )
            }
        }

    }

}
