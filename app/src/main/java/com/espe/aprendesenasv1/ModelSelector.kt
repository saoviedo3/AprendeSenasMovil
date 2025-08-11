package com.espe.aprendesenasv1

object ModelSelector {
    fun getModelFile(sign: String): String {
        return when(sign.uppercase()) {
            "A", "B", "C" -> "model_abc.tflite"
            "CH", "D", "E" -> "model_chde.tflite"
            "F", "G", "H" -> "model_fgh.tflite"
            "I", "K", "M" -> "model_ikm.tflite"
            "L", "N", "O" -> "model_lno.tflite"
            "P", "Q", "R" -> "model_pqr.tflite"
            "S", "T", "U" -> "model_stu.tflite"
            "V", "W", "X" -> "model_vwx.tflite"
            "Y" -> "model_y.tflite"
            "1", "2", "3" -> "model_123.tflite"
            "4", "5", "6" -> "model_456.tflite"
            "7", "8", "9" -> "model_789.tflite"
            else -> "model_meta.tflite"
        }
    }
}