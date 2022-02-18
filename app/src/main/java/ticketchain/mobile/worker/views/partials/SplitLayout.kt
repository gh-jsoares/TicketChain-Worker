package ticketchain.mobile.worker.views.partials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplitLayout(
    leftTopText: String,
    leftBottomText: String,
    rightTopText: String,
    rightBottomText: String,
    topFontSize: TextUnit = 30.sp,
    bottomFontSize: TextUnit = 18.sp,
    leftIcon: ImageVector? = null,
    rightIcon: ImageVector? = null,
    paddingVertical: Dp = 0.dp,
    color: Color = Color.Unspecified,
    height: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        //horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(color = color)
            .padding(vertical = paddingVertical)
            .fillMaxWidth()
            .height(height)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = leftTopText,
                    fontSize = topFontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = leftBottomText,
                    fontSize = bottomFontSize,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            leftIcon?.let {
                Icon(
                    imageVector = it, contentDescription = leftBottomText,
                    tint = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier
                        .size(32.dp)
                        .absoluteOffset(x = 20.dp)
                )
            }
        }

        Divider(
            color = MaterialTheme.colors.secondaryVariant,
            modifier = Modifier
                .fillMaxHeight(fraction = 0.75f)
                .width(1.dp)
                .alpha(0.4f)
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = rightTopText,
                    fontSize = topFontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = rightBottomText,
                    fontSize = bottomFontSize,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.secondaryVariant
                )
            }
            rightIcon?.let {
                Icon(
                    imageVector = it, contentDescription = leftBottomText,
                    tint = MaterialTheme.colors.secondaryVariant,
                    modifier = Modifier
                        .size(32.dp)
                        .absoluteOffset(x = 20.dp)
                )
            }
        }
    }
}
