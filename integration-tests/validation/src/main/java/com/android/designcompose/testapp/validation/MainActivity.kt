/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.designcompose.testapp.validation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.designcompose.DesignSettings
import com.android.designcompose.testapp.common.interFont
import com.android.designcompose.testapp.validation.examples.EXAMPLES

const val TAG = "DesignCompose"

// Main Activity class. Setup auth token and font, then build the UI with buttons for each test
// on the left and the currently selected test on the right.
class MainActivity : ComponentActivity() {
    private lateinit var currentDisplay:
        MutableState<Triple<String, @Composable () -> Unit, String?>>
    private lateinit var enableRendererToggle: MutableState<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DesignSettings.run { addFontFamily("Inter", interFont) }
        DesignSettings.enableLiveUpdates(this)

        setContent {
            currentDisplay = remember { mutableStateOf(EXAMPLES[0]) }
            Row {
                Column(modifier = Modifier.width(110.dp)) { TestButtons() }
                VerticalDivider(
                    color = Color.Black,
                    modifier = Modifier.fillMaxHeight().width(1.dp),
                )
                TestContent { currentDisplay.value.second() }
            }
        }
    }

    // Draw all the buttons on the left side of the screen, one for each test
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TestButtons() {
        val listState = rememberLazyListState()

        LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
            itemsIndexed(items = EXAMPLES) { _, example -> TestButton(example = example) }
        }
    }

    // Draw a single button
    @Composable
    fun TestButton(example: Triple<String, @Composable () -> Unit, String?>) {
        val weight = if (currentDisplay.value == example) FontWeight.Bold else FontWeight.Normal

        Column {
            Text(
                modifier =
                    Modifier.clickable {
                            Log.i(TAG, "Button ${example.first}")
                            currentDisplay.value = example
                        }
                        .heightIn(min = 36.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(Alignment.CenterVertically),
                text = example.first,
                fontSize = 20.sp,
                fontWeight = weight,
            )
            HorizontalDivider(color = Color.Black, modifier = Modifier.fillMaxWidth().width(1.dp))
        }
    }

    // Draw the content for the current test
    @Composable
    fun TestContent(content: @Composable () -> Unit) {
        Box { content() }
    }
}
