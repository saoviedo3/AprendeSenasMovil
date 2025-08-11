package com.espe.aprendesenasv1.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.espe.aprendesenasv1.R
import com.example.mlkitobjectdetection.GraphicOverlay
import com.example.mlkitobjectdetection.ObjectGraphic
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
class DetectionFragmentNumbers : Fragment(R.layout.fragment_detection_number) {

    private lateinit var previewView: PreviewView
    private lateinit var imgResult: ImageView
    private lateinit var graphicOverlay: GraphicOverlay

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private val REQUEST_CAMERA = 1001
    private var pendingSign: String? = null

    // === NUEVO: para configurar overlay según rotación/lente ===
    private var overlayConfiguredRotation: Int? = null
    private val lensFacing = CameraSelector.LENS_FACING_BACK
    // ==========================================================

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sign = requireArguments().getString("sign") ?: ""
        pendingSign = sign

        previewView    = view.findViewById(R.id.previewView)
        imgResult      = view.findViewById(R.id.imgResult)
        graphicOverlay = view.findViewById(R.id.graphicOverlay)

        // Recomendado para cámara
        previewView.scaleType = PreviewView.ScaleType.FILL_CENTER
        previewView.implementationMode = PreviewView.ImplementationMode.PERFORMANCE

        view.findViewById<TextView>(R.id.tvSignTitleNumber).text = "Número $sign"
        val imgRef = view.findViewById<ImageView>(R.id.imgReference)
        val instr = view.findViewById<TextView>(R.id.tvInstructions)

        when (sign) {
            "1" -> { imgRef.setImageResource(R.drawable.ref_1); instr.text = "Elevar el índice en posición vertical, representando la singularidad." }
            "2" -> { imgRef.setImageResource(R.drawable.ref_2); instr.text = "Levanta índice y medio en vertical, en armonía." }
            "3" -> { imgRef.setImageResource(R.drawable.ref_3); instr.text = "Extiende pulgar, índice y medio en vertical." }
            "4" -> { imgRef.setImageResource(R.drawable.ref_4); instr.text = "Levanta cuatro dedos (sin pulgar), estable y claro." }
            "5" -> { imgRef.setImageResource(R.drawable.ref_5); instr.text = "Extiende los cinco dedos de la mano." }
            "6" -> { imgRef.setImageResource(R.drawable.ref_6); instr.text = "Extiende índice, medio y anular en vertical." }
            "7" -> { imgRef.setImageResource(R.drawable.ref_7); instr.text = "Índice, medio y anular extendidos; resto doblados." }
            "8" -> { imgRef.setImageResource(R.drawable.ref_8); instr.text = "Índice, anular y meñique extendidos; resto doblados." }
            "9" -> { imgRef.setImageResource(R.drawable.ref_9); instr.text = "Pulgar toca índice; medio, anular y meñique extendidos." }
        }

        view.findViewById<Button>(R.id.btnTry).setOnClickListener {
            showInstructionsDialog()
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                startCamera(sign)
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA
                )
            }
        }
    }

    private fun showInstructionsDialog() {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Instrucciones")
            .setMessage("Para realizar la seña, asegúrate de estar en un lugar con buena iluminación y un fondo claro.")
            .setPositiveButton("Aceptar") { d, _ -> d.dismiss() }
            .create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pendingSign?.let { startCamera(it) }
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startCamera(targetSign: String) {
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            val modelFile = com.espe.aprendesenasv1.ModelSelector.getModelFile(targetSign)
            val localModel = LocalModel.Builder()
                .setAssetFilePath(modelFile)
                .build()
            val options = CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.6f)
                .build()
            val detector = ObjectDetection.getClient(options)

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->

                        // === NUEVO: configurar GraphicOverlay según rotación y lente ===
                        val rotation = imageProxy.imageInfo.rotationDegrees
                        val needConfig = overlayConfiguredRotation != rotation ||
                                graphicOverlay.width == 0 || graphicOverlay.height == 0

                        if (needConfig) {
                            val isFlipped = (lensFacing == CameraSelector.LENS_FACING_FRONT)
                            if (rotation == 0 || rotation == 180) {
                                graphicOverlay.setImageSourceInfo(
                                    imageProxy.width,
                                    imageProxy.height,
                                    isFlipped
                                )
                            } else {
                                // 90/270: ancho/alto intercambiados
                                graphicOverlay.setImageSourceInfo(
                                    imageProxy.height,
                                    imageProxy.width,
                                    isFlipped
                                )
                            }
                            overlayConfiguredRotation = rotation
                        }
                        // ===============================================================

                        processImageProxy(detector, imageProxy, targetSign)
                    }
                }

            overlayConfiguredRotation = null
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun processImageProxy(
        detector: com.google.mlkit.vision.objects.ObjectDetector,
        imageProxy: ImageProxy,
        targetSign: String
    ) {
        val image = InputImage.fromMediaImage(
            imageProxy.image!!,
            imageProxy.imageInfo.rotationDegrees
        )
        detector.process(image)
            .addOnSuccessListener { detectedObjects ->
                graphicOverlay.clear()
                var found = false
                for (obj in detectedObjects) {
                    graphicOverlay.add(ObjectGraphic(graphicOverlay, obj))
                    if (obj.labels.any { it.text.equals(targetSign, true) }) {
                        found = true
                    }
                }
                graphicOverlay.postInvalidate()
                imgResult.visibility = if (found) View.VISIBLE else View.GONE
            }
            .addOnFailureListener { /* log opcional */ }
            .addOnCompleteListener { imageProxy.close() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }
}
