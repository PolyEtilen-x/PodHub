package com.example.podhub.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterBar(
    modifier: Modifier = Modifier,
    onFilterSelected: (FilterHome) -> Unit
) {
    var selected by remember { mutableStateOf(FilterHome.All) }

    val filters = listOf(FilterHome.All, FilterHome.Podcasts, FilterHome.Artists)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selected

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) Color(0xFFFFC533) else Color.White
                    )
                    .border(
                        width = 1.dp,
                        color = Color(0xFFFFC533),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        selected = filter
                        onFilterSelected(filter)
                    }
                    .padding(horizontal = 20.dp, vertical = 8.dp)


            ) {
                Text(
                    text = filter.title,
                    color = if (isSelected) Color.White else Color(0xFFFFC533),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


enum class FilterHome(val title: String) {
    All("All"),
    Podcasts("Podcasts"),
    Artists("Artists")
}
