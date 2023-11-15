package com.example.project9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.project9.databinding.FragmentImagesBinding
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.ContextCompat.getSystemService

/**
 * A simple [Fragment] subclass.
 * Use the [ImagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImagesFragment : Fragment() {
    private var sensorManager: SensorManager? = null
    private var shakeDetector: ShakeDetector? = null

    val viewModel : AppViewModel by activityViewModels()

    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        fun imageClicked (image : Image) {
            viewModel.onImageClicked(image)
        }

        val adapter = ImageAdapter(::imageClicked)


        //config adapter
        binding.imagesList.adapter = adapter

        viewModel.images.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        //navigate to x image when image clicked
        viewModel.navigateToImage.observe(viewLifecycleOwner, Observer { imageId ->
            imageId?.let {
                val action = ImagesFragmentDirections.actionImagesFragmentToFullScreenImageFragment(imageId)
                this.findNavController().navigate(action)
                viewModel.onImageNavigated()
            }
        })

        //navigate back to sign in when signed out
        viewModel.navigateToSignIn.observe(viewLifecycleOwner, Observer { navigate ->
            if(navigate) {
                this.findNavController().navigate(R.id.action_imagesFragment_to_signInFragment)
                viewModel.onNavigatedToSignIn()
            }
        })

        //navigate to selfie camera when shook
        viewModel.navigateToSelfie.observe(viewLifecycleOwner, Observer { imageId ->
            imageId?.let {
                val action = ImagesFragmentDirections.actionImagesFragmentToSelfieCameraFragment(imageId)
                this.findNavController().navigate(action)
                viewModel.onSelfieNavigated()
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()

        // intialize sensor listening only when fragment is active
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        if (accelerometer != null) {
            shakeDetector = ShakeDetector(viewModel)
            sensorManager?.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()

        // stop sensor listener when fragment changes
        sensorManager?.unregisterListener(shakeDetector)
        sensorManager = null
        shakeDetector = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // stop sensor listener when fragment changes
        sensorManager?.unregisterListener(shakeDetector)
        sensorManager = null
        shakeDetector = null
    }
}