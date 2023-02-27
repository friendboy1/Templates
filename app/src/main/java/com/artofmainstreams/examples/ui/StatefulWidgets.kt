package com.artofmainstreams.examples.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artofmainstreams.examples.R

/**
 * Предположим, у нас есть кусочек ui с рейтингом, который использует кастомную вьюшку RatingView
 */
@Preview
@Composable
fun Rating(
    modifier: Modifier = Modifier
) {
    Column {
        Column(
            modifier = Modifier
                .weight(weight = 1f)
                .padding(horizontal = 16.dp)
        ) {

        }
        Text(
            text = stringResource(id = R.string.review_rating_message),
            fontSize = 24.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        RatingViewStateful(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
        )
    }
}

/**
 * Кастомная view рейтинга, для изменения состояния используется remember
 */
@Preview
@Composable
internal fun RatingViewStateful(
    modifier: Modifier = Modifier,
    maxRating: Int = 5,
    onRatingChange: (newRating: Int) -> Unit = {},
) {
    val rating = rememberSaveable { mutableStateOf(0) }
    var secondRating by remember { mutableStateOf(0) }
    val (value, setValue) = remember { mutableStateOf(0) } // так тоже можно, но зачем?

    Row(modifier = modifier) {
        repeat(maxRating) { starIndex ->
            val icon = if (starIndex < rating.value) {
                Icons.Filled.StarRate
            } else {
                Icons.Filled.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .clickable(onClick = {
                        secondRating += 1
                        rating.value = starIndex + 1
                        onRatingChange(starIndex + 1)
                    })
                    .testTag("star"),
            )
        }
    }
}

/**
 * Кастомная view рейтинга, состояние хранится вне
 */
@Preview
@Composable
internal fun RatingViewStateless(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    maxRating: Int = 5,
    onRatingChange: (newRating: Int) -> Unit = {},
) {
    Row(modifier = modifier) {
        repeat(maxRating) { starIndex ->
            val icon = if (starIndex < rating) {
                Icons.Filled.StarRate
            } else {
                Icons.Filled.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = icon.name,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .clickable(onClick = {
                        onRatingChange(starIndex + 1)
                    })
                    .testTag("star"),
            )
        }
    }
}