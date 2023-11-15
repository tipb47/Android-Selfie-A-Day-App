package com.example.project9

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.project9.R
import androidx.navigation.fragment.findNavController


class AppSplash : Fragment() {

    val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_app_splash, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()

        val currentUser = viewModel.getCurrentUser()

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            if (isAdded) { // Check if fragment is still added
                findNavController().navigate(R.id.action_appSplash_to_signInFragment)
            }
        }, 2000)
    }
}
