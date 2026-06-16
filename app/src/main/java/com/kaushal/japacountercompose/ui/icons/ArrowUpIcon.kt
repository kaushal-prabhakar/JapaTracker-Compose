package com.kaushal.japacountercompose.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp


val ArrowUpIcon: ImageVector
    get() {
        if (_arrow_upward_alt != null) {
            return _arrow_upward_alt!!
        }
        _arrow_upward_alt =
            ImageVector.Builder(
                name = "arrow_upward_alt",
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
                        moveTo(11f, 18f)
                        verticalLineTo(8.8f)
                        lineTo(7.4f, 12.4f)
                        lineTo(6f, 11f)
                        lineTo(12f, 5f)
                        lineToRelative(6f, 6f)
                        lineToRelative(-1.4f, 1.4f)
                        lineTo(13f, 8.8f)
                        verticalLineTo(18f)
                        horizontalLineTo(11f)
                        close()
                    }
                }
                .build()
        return _arrow_upward_alt!!
    }

private var _arrow_upward_alt: ImageVector? = null