package com.magicpark.utils.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.magicpark.core.MagicparkTheme
import com.magicpark.core.R

@Composable
fun MagicparkContainer(content: (@Composable () -> Unit)) {


    val isBackEnabled = false

    ConstraintLayout(Modifier.background(Color(0xFFFFCA00))) {
        val (view, backButton, shapeTopLeft, shapeMidRight, shapeBottomLeft) = createRefs()


        Image(
            painter = painterResource(id = R.drawable.shape_top_left_gold),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .constrainAs(shapeTopLeft) {
                    top.linkTo(parent.top, (-50).dp)
                    start.linkTo(parent.start)
                },
            contentDescription = null
        )

        Image(
            painter = painterResource(id = R.drawable.shape_mid_gold),
            modifier = Modifier
                .constrainAs(shapeMidRight) {
                    top.linkTo(parent.top, 100.dp)
                    end.linkTo(parent.end)
                },
            contentDescription = null
        )



        Image(
            painter = painterResource(id = R.drawable.shape_bottom_left_gold),
            modifier = Modifier
                .constrainAs(shapeBottomLeft) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize(),
            contentDescription = null
        )

        if (isBackEnabled) {

            Image(
                painter = painterResource(id = R.drawable.ic_back),
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .constrainAs(backButton) {
                        top.linkTo(parent.top, 20.dp)
                        start.linkTo(parent.start, 20.dp)
                    }
                    .clickable {
                    },
                contentDescription = null,
                colorFilter = ColorFilter.tint(MagicparkTheme.colors.primary)
            )
        }

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize()
                .constrainAs(view) {
                    top.linkTo(backButton.top)
                    start.linkTo(parent.start)
                }) {
            content()
        }
    }
}