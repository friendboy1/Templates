package com.artofmainstreams.examples.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.artofmainstreams.examples.R

/**
 * Пример создания ConstraintLayout в парадигме Compose
 */
@DevicesPreview
@Composable
fun ConstraintLayoutScreen() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val button = createRef()
        val (logo, text) = createRefs()
        val chain = createVerticalChain(logo, text, chainStyle = ChainStyle.Packed)
        constrain(chain) {
            top.linkTo(parent.top)
            bottom.linkTo(button.top, margin = 8.dp)
        }
        LogoWidget(modifier = Modifier
            .constrainAs(logo) {
                centerHorizontallyTo(parent)
            }
        )
        TextWithBoldSuffix(modifier = Modifier
            .constrainAs(text) {
                centerHorizontallyTo(parent)
            }
        )
        ButtonWidget(modifier = Modifier
            .constrainAs(button) {
                bottom.linkTo(parent.bottom, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview
@Composable
private fun ButtonWidget(
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        onClick = { },
        shape = RoundedCornerShape(topStart = 32.dp, bottomEnd = 32.dp),
        contentPadding = PaddingValues(
            horizontal = 32.dp,
            vertical = 20.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Cyan
        ),
    ) {
        ContactSupportText()
        Icon(
            painter = painterResource(id = R.drawable.ic_add_24),
            contentDescription = "Применить",
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun LogoWidget(
    modifier: Modifier = Modifier,
) {
    Image(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = "Логотип",
        alignment = Alignment.TopCenter,
        contentScale = ContentScale.Inside,
        alpha = 0.8f,
        colorFilter = ColorFilter.tint(Color.Magenta, BlendMode.ColorBurn),
        modifier = modifier
            .fillMaxWidth(0.5f)
            .clip(RoundedCornerShape(percent = 25))
            .border(
                BorderStroke(
                    width = 8.dp,
                    color = MaterialTheme.colors.primary
                ),
                RoundedCornerShape(percent = 25)
            )
    )
}

