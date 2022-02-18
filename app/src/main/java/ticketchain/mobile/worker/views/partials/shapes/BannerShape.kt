package ticketchain.mobile.worker.views.partials.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object BannerShape : Shape {

    fun generatePath(size: Size) = Path().apply {
        reset()
        moveTo(0f, 0f)
        lineTo(size.width, size.height / 4f)
        lineTo(size.width, size.height * 3f / 4f)
        lineTo(0f, size.height)
        close()
    }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ) = Outline.Generic(generatePath(size))
}