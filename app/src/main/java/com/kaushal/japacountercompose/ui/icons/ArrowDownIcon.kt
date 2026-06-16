package com.kaushal.japacountercompose.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val ArrowDownIcon: ImageVector
    get() {
        if (_arrow_downward_alt != null) {
            return _arrow_downward_alt!!
        }
        _arrow_downward_alt =
            ImageVector.Builder(
                name = "arrow_downward_alt",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(12f, 18f)
                        lineTo(6f, 12f)
                        lineTo(7.4f, 10.6f)
                        lineTo(11f, 14.2f)
                        verticalLineTo(5f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(9.2f)
                        lineToRelative(3.6f, -3.6f)
                        lineTo(18f, 12f)
                        lineToRelative(-6f, 6f)
                        close()
                    }
                }
                .build()
        return _arrow_downward_alt!!
    }

private var _arrow_downward_alt: ImageVector? = null