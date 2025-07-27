package com.espe.aprendesenasv1.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.espe.aprendesenasv1.R

class NumbersFragment : Fragment(R.layout.fragment_numbers) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = findNavController()
        view.findViewById<Button>(R.id.btn1).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "1"))
        }
        view.findViewById<Button>(R.id.btn2).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "2"))
        }
        view.findViewById<Button>(R.id.btn3).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "3"))
        }
        view.findViewById<Button>(R.id.btn4).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "4"))
        }
        view.findViewById<Button>(R.id.btn5).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "5"))
        }
        view.findViewById<Button>(R.id.btn6).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "6"))
        }
        view.findViewById<Button>(R.id.btn7).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "7"))
        }
        view.findViewById<Button>(R.id.btn8).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "8"))
        }
        view.findViewById<Button>(R.id.btn9).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "9"))
        }

    }
}
