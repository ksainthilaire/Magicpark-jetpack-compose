package contact

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.magicpark.core.MagicparkMaterialTheme
import com.magicpark.core.MagicparkTheme
import com.magicpark.utils.R
import org.koin.androidx.compose.getViewModel

@Preview
@Composable
fun ContactScreenPreview() =
    MagicparkMaterialTheme {
        ContactScreen(
            onBackPressed = {},
        )
    }

/**
 * @param onBackPressed listener go back
 * @param sendBugReport Send a bug report to the server.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    onBackPressed: () -> Unit,
) {

    val viewModel: ContactViewModel = getViewModel()
    val state by viewModel.state.collectAsState()

    var text by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_back),
            modifier = Modifier
                .padding(24.dp)
                .width(50.dp)
                .height(50.dp)
                .clickable {
                    onBackPressed()
                },
            contentDescription = null,
            colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
        )

        Column(
            Modifier.padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.support_headline),
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                ),
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.support_description),
                style = TextStyle(
                    fontSize = 12.sp,
                ),
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(top = 24.dp),
                value = text,
                onValueChange = { value ->
                    text = value
                },
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    unfocusedBorderColor = Color.Transparent
                ),
                placeholder = { Text(text = stringResource(R.string.support_text_placeholder)) },
            )

            Button(
                modifier = Modifier.padding(top = 54.dp),
                onClick = {
                    viewModel.sendBugReport(text)
                },
            ) {
                Text(text = stringResource(R.string.support_submit))
            }
        }
    }
}
