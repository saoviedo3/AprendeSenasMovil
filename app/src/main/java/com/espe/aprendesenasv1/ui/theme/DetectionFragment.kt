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
class DetectionFragment : Fragment(R.layout.fragment_detection) {
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


        view.findViewById<TextView>(R.id.tvSignTitle).text = "Letra $sign"
        val imgRef = view.findViewById<ImageView>(R.id.imgReference)
        val instr = view.findViewById<TextView>(R.id.tvInstructions)

        when(sign) {
            "A" -> {
                imgRef.setImageResource(R.drawable.ref_a)
                instr.text = "Se realiza levantando la mano y formando un puño con todos los dedos extendidos, excepto el pulgar, que se coloca sobre los otros dedos. Este gesto representa visualmente la forma de la letra \"A\"."
            }
            "B" -> {
                imgRef.setImageResource(R.drawable.ref_b)
                instr.text = "Se realiza levantando la mano con los dedos juntos y extendidos, excepto el pulgar, que se coloca sobre la palma. Este gesto representa visualmente la forma de la letra \"B\"."
            }
            "C" -> {
                imgRef.setImageResource(R.drawable.ref_c)
                instr.text = "Se realiza levantando la mano con los dedos juntos y extendidos, excepto el pulgar, que se pliega hacia la palma. Este gesto representa visualmente la forma de la letra \"C\"."
            }
            "CH" -> {
                imgRef.setImageResource(R.drawable.ref_ch)
                instr.text = "Se realiza combinando los gestos de las letras \"C\" y \"H\". levantando la mano con los dedos extendidos y ligeramente separados, excepto el meñique y anular que se pliega hacia la palma. Combina ambos gestos para representar la letra \"CH\"."
            }
            "D" -> {
                imgRef.setImageResource(R.drawable.ref_d)
                instr.text = "Se baja toda la mano con los dedos juntos y extendidos, excepto el pulgar, que se pliega hacia la palma. Luego, solo el dedo índice se extiende hacia adelante y ligeramente hacia abajo. Este gesto visualiza correctamente la forma de la letra \"D\"."
            }
            "E" -> {
                imgRef.setImageResource(R.drawable.ref_e)
                instr.text = "Se realiza levantando la mano con todos los dedos doblados y juntos hacia la palma. Los dedos se mantienen alineados y paralelos. Este gesto visualiza correctamente la forma de la letra \"E\"."
            }
            "F" -> {
                imgRef.setImageResource(R.drawable.ref_f)
                instr.text = "Se realiza levantando la mano con los dedos juntos y extendidos, excepto el pulgar y el índice, que se juntan para formar un círculo. Los demás dedos permanecen extendidos y separados. Este gesto visualiza correctamente la forma de la letra \"F\"."
            }
            "G" -> {
                imgRef.setImageResource(R.drawable.ref_g)
                instr.text = "Se realiza levantando la mano con los dedos juntos y extendidos. Luego, el pulgar y el dedo indice se tocan suavemente, formando una \"u\" con los demás dedos doblados hacia la palma. Este gesto visualiza la forma de la letra \"G\" y destaca la conexión única entre el pulgar y el dedo indice."
            }
            "H" -> {
                imgRef.setImageResource(R.drawable.ref_h)
                instr.text = "Debes extender tu mano con la palma hacia abajo y los dedos juntos. Luego, dobla el dedo pulgar, anular y el meñique hacia la palma de tu mano, manteniendo los otros dedos índice y del medio extendidos."
            }
            "I" -> {
                imgRef.setImageResource(R.drawable.ref_i)
                instr.text = "Debes extender tu mano con la palma hacia el lado opuesto a ti. Luego, dobla todos tus dedos hacia la palma de tu mano, excepto el dedo meñique que debe permanecer extendido hacia arriba. Asegúrate de que los otros dedos estén bien doblados para que el dedo índice se destaque claramente."
            }
            "K" -> {
                imgRef.setImageResource(R.drawable.ref_k)
                instr.text = "Debes extender tu mano y mantener tres dedos juntos: el pulgar, anular y el meñique. El indice se extiende hacia afuera, mientras que el dedo del medio se cruza para formar una forma que se asemeja a la letra “K”. Los otros dos dedos deben estar doblados hacia la palma de la mano."
            }
            "L" -> {
                imgRef.setImageResource(R.drawable.ref_l)
                instr.text = "Debes extender tu pulgar hacia afuera y apuntar hacia arriba, mientras que tu índice también debe apuntar hacia arriba, formando un ángulo recto con tu pulgar. Los otros tres dedos deben estar doblados hacia la palma de tu mano."
            }
            "M" -> {
                imgRef.setImageResource(R.drawable.ref_m)
                instr.text = "Debes cerrar tu mano en un puño, pero manteniendo los tres dedos del medio extendidos juntos y el pulgar y el meñique doblados hacia la palma de tu mano."
            }
            "N" -> {
                imgRef.setImageResource(R.drawable.ref_n)
                instr.text = "Debes cerrar tu mano en un puño, pero manteniendo los dedos indice y del medio extendidos juntos y el pulgar, anular y meñique doblados hacia la palma de tu mano."
            }
            "O" -> {
                imgRef.setImageResource(R.drawable.ref_o)
                instr.text = "Debes unir el pulgar y el dedo índice para formar un círculo, mientras los otros tres dedos permanecen unidos al indice formando un circulo. Asegúrate de que los dedos estén relajados y no rígidos para que el gesto se vea natural y fluido."
            }
            "P" -> {
                imgRef.setImageResource(R.drawable.ref_p)
                instr.text = "Debes extender tu mano con la palma hacia abajo. Luego, dobla tus dos dedos medios (el el anular e meñique) hacia la palma de tu mano, manteniendo el pulgar, dedo medio e índice extendidos. El pulgar debe estar apuntando hacia adelante, el dedo del medio apuntando hacia abajo y el índice apuntando hacia un lado."
            }
            "Q" -> {
                imgRef.setImageResource(R.drawable.ref_q)
                instr.text = "Debes extender tu mano y doblar tus dedos medio, anular y meñique hacia la palma de tu mano. El pulgar y el índice deben permanecer extendidos. El dedo índice se dobla ligeramente en la articulación central para formar una forma que se asemeja a un gancho."
            }
            "R" -> {
                imgRef.setImageResource(R.drawable.ref_r)
                instr.text = "Debes extender tu mano y mantener tres dedos hacia abajo (el meñique, el anular y el pulgar). Luego, levanta tu dedo índice y ponerlo frentre al dedo del medio."
            }
            "S" -> {
                imgRef.setImageResource(R.drawable.ref_s)
                instr.text = "Debes cerrar tu mano en un puño, manteniendo el pulgar extendido hacia arriba y cruzado sobre los dedos índice y medio. Los dedos anular y meñique deben estar doblados hacia la palma de la mano."
            }
            "T" -> {
                imgRef.setImageResource(R.drawable.ref_t)
                instr.text = "El dedo pulgar debe estar pegado a la palma de la mano. Esto mientras el dedo índice está extendido y ligeramente doblado en la articulación central. Los demás dedos deben estar extendidos y unidos."
            }
            "U" -> {
                imgRef.setImageResource(R.drawable.ref_u)
                instr.text = "Debes extender tu mano y mantener juntos tu dedo índice y medio mientras los otros dedos permanecen doblados hacia la palma de tu mano. Asegúrate de que los dos dedos extendidos estén paralelos y rectos para formar una representación clara de la letra “U”."
            }
            "V" -> {
                imgRef.setImageResource(R.drawable.ref_v)
                instr.text = "Debes extender tu mano con la palma hacia afuera y los dedos extendidos. Luego, dobla tu anular, meñique y pulgar hacia la palma de tu mano, manteniendo el índice y el dedo de medio extendidos, separados formando un \"V\". Este gesto es comúnmente asociado con señales de paz o victoria."
            }
            "W" -> {
                imgRef.setImageResource(R.drawable.ref_w)
                instr.text = "Debes extender tu mano y doblar tus dedos meñique y anular hacia la palma de tu mano, mientras mantienes el índice, dedo medio y anular extendidos. Asegúrate de que los dedos se mantenga alejados de los otros para formar claramente la letra “W”."
            }
            "X" -> {
                imgRef.setImageResource(R.drawable.ref_x)
                instr.text = "Debes extender tu dedo índice mientras los otros dedos permanecen cerrados. El dedo índice debe estar medio doblado."
            }
            "Y" -> {
                imgRef.setImageResource(R.drawable.ref_y)
                instr.text = "Debes extender tu mano con la palma hacia adelante. Mantén tu pulgar y meñique extendidos mientras doblas los dedos índice, medio y anular hacia la palma de tu mano."
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
