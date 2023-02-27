package com.artofmainstreams.examples.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Тестирование виджета с сохранением состояния двумя способами
 */
class StatefulWidgetsKtTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testRatingViewWithoutStateHoisting() {
        val starRate = Icons.Filled.StarRate.name
        val starBorder = Icons.Filled.StarBorder.name

        composeTestRule.setContent {
            RatingViewStateful(maxRating = 5)
        }

        val stars = composeTestRule.onAllNodesWithTag("star")
            .assertAll(hasContentDescription(starBorder))

        stars[3].performClick()
        stars[0].assertContentDescriptionContains(starRate)
        stars[1].assertContentDescriptionContains(starRate)
        stars[2].assertContentDescriptionContains(starRate)
        stars[3].assertContentDescriptionContains(starRate)
        stars[4].assertContentDescriptionContains(starBorder)

        stars[0].performClick()
        stars[0].assertContentDescriptionContains(starRate)
        stars[1].assertContentDescriptionContains(starBorder)
        stars[2].assertContentDescriptionContains(starBorder)
        stars[3].assertContentDescriptionContains(starBorder)
        stars[4].assertContentDescriptionContains(starBorder)
    }

    @Test
    fun testRatingViewWithStateHoisting() {
        var rating by mutableStateOf(0)
        val maxRating = 5
        val starFour = 4
        val starOne = 1
        composeTestRule.setContent {
            RatingViewStateless(rating = rating, maxRating = maxRating) {
                rating = it
            }
        }

        val stars = composeTestRule.onAllNodesWithTag("star")

        stars[starFour-1].performClick()
        Assert.assertEquals(starFour, rating)

        stars[starOne-1].performClick()
        composeTestRule.waitForIdle()
        Assert.assertEquals(starOne, rating)
    }
}