package com.karurisuro.jettip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.karurisuro.jettip.components.InputField
import com.karurisuro.jettip.ui.theme.JetTipTheme
import com.karurisuro.jettip.utility.calculateTotalPerPerson
import com.karurisuro.jettip.utility.calculateTotalTip
import com.karurisuro.jettip.utility.changeValueWithInRange
import com.karurisuro.jettip.widget.RoundIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    JetTipTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPersonState: MutableDoubleState = mutableDoubleStateOf(0.0)) {
    val total = "%.2f".format(totalPerPersonState.doubleValue)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(12.dp))),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE9D7F7),
            contentColor = Color.DarkGray
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Per Person",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "$$total",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MyApp {
        MainContent()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
fun MainContent() {
    val splitByState = remember {
        mutableIntStateOf(1)
    }

    val tipAmountState = remember {
        mutableDoubleStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableDoubleStateOf(0.0)
    }

    Column(modifier = Modifier.padding(all = 20.dp)) {
        TopHeader(totalPerPersonState)
        BillForm(
            splitByState = splitByState,
            tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState
        ) {
            Log.d("formState", "MainContent: $it")
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableIntState,
    tipAmountState: MutableDoubleState,
    totalPerPersonState: MutableDoubleState,
    onValueChange: (String) -> Unit = {},

    ) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState) {
        totalBillState.value.trim().isEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val sliderPositionState = remember {
        mutableFloatStateOf(0f)
    }
    val tipPercentage = (sliderPositionState.floatValue * 100).toInt()


    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValueChange(totalBillState.value.trim())
                    keyboardController?.hide()
                }
            )
            if (validState) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "split",
                        modifier = Modifier
                            .padding(6.dp)
                            .align(alignment = Alignment.CenterVertically),
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RoundIconButton(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_remove_24),
                            contentDesc = "Decrease value",
                            onClick = {
                                splitByState.intValue = changeValueWithInRange(
                                    splitByState.intValue - 1,
                                    totalBillState.value.toInt()
                                )
                                totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                    totalBillState.value,
                                    tipPercentage,
                                    splitByState.intValue
                                )
                            }
                        )
                        Text(
                            text = "${splitByState.intValue}",
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp),
                            fontSize = 20.sp
                        )
                        RoundIconButton(
                            imageVector = Icons.Default.Add,
                            contentDesc = "Increse value",
                            onClick = {
                                splitByState.intValue = changeValueWithInRange(
                                    splitByState.intValue + 1,
                                    totalBillState.value.toInt()
                                )
                                totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                    totalBillState.value,
                                    tipPercentage,
                                    splitByState.intValue
                                )
                            }
                        )

                    }
                }

                // Tip row
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    Text(text = "$${tipAmountState.doubleValue}")
                }

                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercentage %")
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(
                        value = sliderPositionState.floatValue,
                        onValueChange = {
                            sliderPositionState.floatValue = it
                        },
                        steps = 5,
                        modifier = Modifier.padding(horizontal = 6.dp),
                        onValueChangeFinished = {
                            tipAmountState.doubleValue =
                                calculateTotalTip(totalBillState.value, tipPercentage)
                            totalPerPersonState.doubleValue = calculateTotalPerPerson(
                                totalBillState.value,
                                tipPercentage,
                                splitByState.intValue
                            )
                        }
                    )

                }
            } else {
                Box {
                    Text(text = "noting")
                }
            }
        }
    }
}