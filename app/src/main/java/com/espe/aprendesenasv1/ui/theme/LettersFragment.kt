package com.espe.aprendesenasv1.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.espe.aprendesenasv1.R

class LettersFragment : Fragment(R.layout.fragment_letters) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = findNavController()
        view.findViewById<Button>(R.id.btnA).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "A"))
        }
        view.findViewById<Button>(R.id.btnB).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "B"))
        }
        view.findViewById<Button>(R.id.btnC).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "C"))
        }
        view.findViewById<Button>(R.id.btnCH).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "CH"))
        }
        view.findViewById<Button>(R.id.btnD).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "D"))
        }
        view.findViewById<Button>(R.id.btnE).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "E"))
        }
        view.findViewById<Button>(R.id.btnF).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "F"))
        }
        view.findViewById<Button>(R.id.btnG).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "G"))
        }
        view.findViewById<Button>(R.id.btnH).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "H"))
        }
        view.findViewById<Button>(R.id.btnI).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "I"))
        }
        view.findViewById<Button>(R.id.btnK).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "K"))
        }
        view.findViewById<Button>(R.id.btnL).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "L"))
        }
        view.findViewById<Button>(R.id.btnM).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "M"))
        }
        view.findViewById<Button>(R.id.btnN).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "N"))
        }
        view.findViewById<Button>(R.id.btnO).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "O"))
        }
        view.findViewById<Button>(R.id.btnP).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "P"))
        }
        view.findViewById<Button>(R.id.btnQ).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "Q"))
        }
        view.findViewById<Button>(R.id.btnR).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "R"))
        }
        view.findViewById<Button>(R.id.btnS).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "S"))
        }
        view.findViewById<Button>(R.id.btnT).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "T"))
        }
        view.findViewById<Button>(R.id.btnU).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "U"))
        }
        view.findViewById<Button>(R.id.btnV).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "V"))
        }
        view.findViewById<Button>(R.id.btnW).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "W"))
        }
        view.findViewById<Button>(R.id.btnX).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "X"))
        }
        view.findViewById<Button>(R.id.btnY).setOnClickListener {
            nav.navigate(R.id.action_letters_to_detection, bundleOf("sign" to "Y"))
        }
    }
}
