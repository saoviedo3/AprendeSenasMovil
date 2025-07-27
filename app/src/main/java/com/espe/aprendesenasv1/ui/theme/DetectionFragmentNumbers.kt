package com.espe.aprendesenasv1.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.espe.aprendesenasv1.R
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Sólo guardamos la letra en pendingSign
        val                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        sign = requireArguments().getString("sign") ?: ""
        pendingSign = sign

        previewView    = view.findViewById(R.id.previewView)
        imgResult      = view.findViewById(R.id.imgResult)
        graphicOverlay = view.findViewById(R.id.graphicOverlay)


        view.findViewById<TextView>(R.id.tvSignTitleNumber).text = "Numero $sign"
        val imgRef = view.findViewById<ImageView>(R.id.imgReference)
        val instr = view.findViewById<TextView>(R.id.tvInstructions)

        when(sign) {
            "1" -> {
                imgRef.setImageResource(R.drawable.ref_1)
                instr.text = "Elevar el índice en posición vertical, representando la singularidad y el comienzo enérgico."
            }
            "2" -> {
                imgRef.setImageResource(R.drawable.ref_2)
                instr.text = "Levanta dos dedos, el índice y el dedo medio en posición vertical, simbolizando la conexión y la armonía de dos elementos."
            }
            "3" -> {
                imgRef.setImageResource(R.drawable.ref_3)
                instr.text = "Extiende tres dedos, el pulgar, índice y el dedo del medio en posición vertical, creando una representación visual de la trinidad y la vitalidad del número tres."
            }
            "4" -> {
                imgRef.setImageResource(R.drawable.ref_4)
                instr.text = "Levanta los cuatro dedos de la mano, menos el pulgar en posición vertical, formando un cuadrado imaginario que refleja la estabilidad y la estructura del número cuatro."
            }
            "5" -> {
                imgRef.setImageResource(R.drawable.ref_5)
                instr.text = "Extiende los cinco dedos de la mano en posición vertical, representando la totalidad de una mano y la plenitud del número cinco."
            }
            "6" -> {
                imgRef.setImageResource(R.drawable.ref_6)
                instr.text = "Extiende tres dedos, índice, el dedo del medio y el anular en posición vertical, creando una representación visual de la vitalidad del número seis."
            }
            "7" -> {
                imgRef.setImageResource(R.drawable.ref_7)
                instr.text = "Levanta el índice, medio y anular, manteniendo los demás dedos doblados, formando una línea diagonal que simboliza la inclinación ascendente del número siete en lengua de señas ecuatoriana."
            }
            "8" -> {
                imgRef.setImageResource(R.drawable.ref_8)
                instr.text = "Levanta el índice, anular y meñique, manteniendo los demás dedos doblados, formando una línea diagonal que simboliza la inclinación ascendente del número ocho en lengua de señas ecuatoriana."
            }
            "9" -> {
                imgRef.setImageResource(R.drawable.ref_9)
                instr.text = "Con el pulgar tocando la punta del dedo índice, extiende los dedos medio, anular y meñique, que simboliza el número nueve en lengua de señas ecuatoriana."
            }
        }
        // … código de ref_a / ref_b …

        view.findViewById<Button>(R.id.btnTry).setOnClickListener {
            // Mostrar el AlertDialog con las instrucciones antes de arrancar la cámara
            showInstructionsDialog()
            // Cuando pulsan Intentar, comprobamos permiso y arrancamos
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED) {
                startCamera(sign)
            } else {
                // Pedimos permiso, y si lo dan, en onRequestPermissionsResult arrancamos
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA
                )
            }
        }

    }

    // Función para mostrar el AlertDialog con las instrucciones
    private fun showInstructionsDialog() {
        val builder = AlertDialog.Builder(requireContext())

        // Configuramos el título y el mensaje
        builder.setTitle("Instrucciones")
            .setMessage("Para realizar la seña, asegúrate de estar en un lugar con buena iluminación y un fondo blanco.")

        // Agregamos el botón de Aceptar
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()  // Cerrar el diálogo cuando se haga clic en Aceptar
        }

        // Mostrar el AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario concedió el permiso, arrancamos la cámara con la letra pendiente
                pendingSign?.let { startCamera(it) }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permiso de cámara denegado.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun startCamera(targetSign: String) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // 1) Preview
            val preview = androidx.camera.core.Preview.Builder()
                .build()
                .also { it.setSurfaceProvider(previewView.surfaceProvider) }

            // 2) ML Kit
            val localModel = LocalModel.Builder()
                .setAssetFilePath("model_meta.tflite")
                .build()
            val options = CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.6f)
                .build()
            val detector = ObjectDetection.getClient(options)

            // 3) ImageAnalysis
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(detector, imageProxy, targetSign)
                    }
                }

            // 4) Bind
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
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
            .addOnFailureListener { /* opcional: logging */ }
            .addOnCompleteListener { imageProxy.close() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }
}
