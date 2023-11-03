package contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private val viewModel: ContactViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext())
            .apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    ContactScreen(
                        onBackPressed = { activity?.onBackPressedDispatcher?.onBackPressed() },
                        sendBugReport = viewModel::sendBugReport,
                    )
                }
            }
}

@Preview
@Composable
fun ContactScreenPreview() =
    ContactScreen(
        onBackPressed = {},
        sendBugReport = {},
    )

/**
 * @param onBackPressed listener go back
 * @param sendBugReport Send a bug report to the server.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    onBackPressed: () -> Unit,
    sendBugReport: (text: String) -> Unit,
) {

    var text by remember { mutableStateOf("") }

    Image(
        painter = painterResource(id = R.drawable.ic_back),
        modifier = Modifier
            .clickable {
                onBackPressed()
            }
            .width(100.dp)
            .height(50.dp)
            .padding(
                vertical = MagicparkTheme.defaultPadding,
            ),
        contentDescription = null,
        colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(
                Modifier.padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "Pouvez-vous décrire le problème que vous rencontrer?",
                    modifier = Modifier.padding(top = 76.dp),
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = "Nous ferons part de votre commentaire au support Magicpark afin d'améliorer notre service et éviter que ce problème se reproduise."
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .height(400.dp)
                    .width(300.dp),
                value = text,
                onValueChange = { value ->
                    text = value
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = { Text("Renseignez-nous ici le problème rencontré") },
            )

            Button(
                modifier = Modifier.padding(top = 54.dp),
                onClick = {
                    sendBugReport(text)
                },
            ) {
                Text("Transmettre au support")
            }
        }
    }
}
