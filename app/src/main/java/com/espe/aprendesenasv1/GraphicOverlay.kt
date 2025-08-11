package com.example.mlkitobjectdetection

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

open class GraphicOverlay(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val lock = Any()
    private val graphics: MutableList<Graphic> = ArrayList()

    // Tamaño real del frame de cámara
    var imageWidth = 0
        private set
    var imageHeight = 0
        private set

    // Factores de escala y offsets (para escalar coordenadas del frame al overlay)
    private var widthScaleFactor = 1f
    private var heightScaleFactor = 1f
    private var isImageFlipped = false

    abstract class Graphic(private val overlay: GraphicOverlay) {
        abstract fun draw(canvas: Canvas)

        // Helpers: SIEMPRE usar los métodos públicos del overlay
        fun translateX(x: Float): Float = overlay.translateX(x)
        fun translateY(y: Float): Float = overlay.translateY(y)
        fun scaleX(v: Float): Float = overlay.scaleX(v)
        fun scaleY(v: Float): Float = overlay.scaleY(v)
    }

    fun clear() {
        synchronized(lock) { graphics.clear() }
        postInvalidate()
    }

    fun add(graphic: Graphic) {
        synchronized(lock) { graphics.add(graphic) }
        postInvalidate()
    }

    /**
     * Llama esto cada vez que cambia la rotación o el tamaño del frame
     * (ya lo haces desde DetectionFragment).
     */
    fun setImageSourceInfo(imageWidth: Int, imageHeight: Int, isFlipped: Boolean) {
        this.imageWidth = imageWidth
        this.imageHeight = imageHeight
        this.isImageFlipped = isFlipped
        updateScaleFactors()
        postInvalidate()
    }

    // Recalcula escalas cuando ya tenemos el tamaño del overlay
    private fun updateScaleFactors() {
        if (imageWidth > 0 && imageHeight > 0 && width > 0 && height > 0) {
            widthScaleFactor  = width  * 1f / imageWidth
            heightScaleFactor = height * 1f / imageHeight
        }
    }

    // Si cambia el tamaño del overlay (rotación, split-screen, etc.)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateScaleFactors()
    }

    // Métodos públicos que usa Graphic para convertir coordenadas
    fun translateX(x: Float): Float {
        val scaledX = x * widthScaleFactor
        return if (isImageFlipped) width - scaledX else scaledX
    }

    fun translateY(y: Float): Float = y * heightScaleFactor
    fun scaleX(v: Float): Float = v * widthScaleFactor
    fun scaleY(v: Float): Float = v * heightScaleFactor

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        synchronized(lock) {
            for (g in graphics) g.draw(canvas)
        }
    }
}
