package com.karurisuro.jettip.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val IconButtonSizeModifier = Modifier.size(40.dp)

// add elevation
@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    cardColors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Color.Black.copy(alpha = 0.8f),

        ),
    contentDesc: String
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable {
                onClick.invoke()
            }
            .then(IconButtonSizeModifier),
        shape = CircleShape,
        colors = cardColors,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDesc,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                tint = tint
            )
    }

}