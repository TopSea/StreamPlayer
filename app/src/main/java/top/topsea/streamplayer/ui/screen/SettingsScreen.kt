package top.topsea.streamplayer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import top.topsea.streamplayer.data.state.UISetsState
import top.topsea.streamplayer.ui.theme.SetsItemHeight

@Composable
fun SettingsScreen(
    uiSetsState: UISetsState
) {
    LazyColumn(modifier = Modifier.background(Color.LightGray)) {
        item {
            SetsBlock()
        }
    }
}

@Composable
fun SetsBlock(

) {
    Column(modifier = Modifier.fillMaxWidth().background(Color.White)) {
        SetsItemGoTo("ABCD") {}
    }
}

@Composable
fun SetsItemGoTo(
    itemName: String,
    onGoTo: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(SetsItemHeight)
            .clickable {
                onGoTo()
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = itemName)
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Go to $itemName"
        )
    }
}