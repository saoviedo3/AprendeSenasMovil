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
            "Y", "1", "2" -> "model_y12.tflite"
            "3", "4", "5" -> "model_345.tflite"
            "6", "7", "8" -> "model_678.tflite"
            "9" -> "model_9.tflite"
            else -> "model_meta.tflite"
        }
    }
}