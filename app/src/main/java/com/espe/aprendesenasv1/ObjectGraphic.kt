package com.example.mlkitobjectdetection

import android.graphics.*
import com.example.mlkitobjectdetection.GraphicOverlay.Graphic
import com.google.mlkit.vision.objects.DetectedObject
import java.util.Locale
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ObjectGraphic(
    overlay: GraphicOverlay,
    private val detectedObject: DetectedObject
) : Graphic(overlay) {

    private val d = overlay.resources.displayMetrics.density

    // Estilo
    private val boxPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFD600")     // Amarillo de marca
        style = Paint.Style.STROKE
        strokeWidth = 3f * d                    // 3dp
        strokeJoin = Paint.Join.ROUND
    }
    private val boxFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT               // sin relleno (puedes poner un leve alpha si quieres)
        style = Paint.Style.FILL
    }
    private val labelBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#B3000000")   // negro con ~70% alpha
        style = Paint.Style.FILL
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = 14f * d                      // 14sp aprox
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val corner = 10f * d                // radio esquinas 10dp
    private val pad = 6f * d                    // padding interno 6dp
    private val lineHeight = textPaint.textSize + 4f * d

    // Si tu detector recorta un poco, infla la caja un %:
    private val paddingPercent = 0.04f          // 4% del tamaño (ajusta 0.00–0.08)

    override fun draw(canvas: Canvas) {
        val src = detectedObject.boundingBox

        // Traducir a coords del overlay considerando flip y escala
        val x0 = translateX(src.left.toFloat())
        val x1 = translateX(src.right.toFloat())
        val y0 = translateY(src.top.toFloat())
        val y1 = translateY(src.bottom.toFloat())

        // Normalizar e inflar ligeramente
        var left   = min(x0, x1)
        var right  = max(x0, x1)
        var top    = min(y0, y1)
        var bottom = max(y0, y1)

        val w = right - left
        val h = bottom - top
        if (paddingPercent > 0f) {
            val dx = w * paddingPercent
            val dy = h * paddingPercent
            left   -= dx
            right  += dx
            top    -= dy
            bottom += dy
        }

        // Limitar a los bordes del canvas
        left = max(0f, left)
        top = max(0f, top)
        right = min(canvas.width.toFloat(), right)
        bottom = min(canvas.height.toFloat(), bottom)

        val rect = RectF(left, top, right, bottom)

        // Dibuja caja (opcional: relleno transparente)
        if (boxFillPaint.color != Color.TRANSPARENT) {
            canvas.drawRoundRect(rect, corner, corner, boxFillPaint)
        }
        canvas.drawRoundRect(rect, corner, corner, boxPaint)

        // Etiqueta principal (usa la primera si hay varias)
        val label = detectedObject.labels.firstOrNull()
        val labelText = if (label != null) {
            val pct = (label.confidence * 100f)
            // Ej: "A  •  92.3%"
            "${label.text.uppercase(Locale.getDefault())}  •  ${"%.1f".format(pct)}%"
        } else {
            // Fallback por si no hay labels
            "Detectado"
        }

        // Calcular caja de texto
        val textWidth = textPaint.measureText(labelText)
        val bgLeft = rect.left
        val bgRight = rect.left + textWidth + 2 * pad
        val bgBottom = rect.top - 8f * d
        val bgTop = bgBottom - (lineHeight + pad)

        // Si no cabe arriba lo ponemos dentro del rectángulo
        val finalBgTop: Float
        val finalBgBottom: Float
        val finalTextY: Float
        if (bgTop < 0f) {
            finalBgTop = rect.top
            finalBgBottom = rect.top + lineHeight + pad
            finalTextY = finalBgTop + lineHeight * 0.8f
        } else {
            finalBgTop = bgTop
            finalBgBottom = bgBottom
            finalTextY = finalBgTop + lineHeight * 0.8f
        }

        val labelBg = RectF(bgLeft, finalBgTop, min(bgRight, canvas.width.toFloat()), finalBgBottom)
        canvas.drawRoundRect(labelBg, corner, corner, labelBgPaint)
        canvas.drawText(labelText, labelBg.left + pad, finalTextY, textPaint)

        // Opcionalver el trackingId para debug:
        // detectedObject.trackingId?.let {
        //     val idText = "ID: $it"
        //     canvas.drawText(idText, rect.left, rect.bottom + lineHeight, textPaint)
        // }
    }
}
