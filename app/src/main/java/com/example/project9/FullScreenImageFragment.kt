package com.example.project9

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.project9.databinding.FragmentFullScreenImageBinding
import com.example.project9.databinding.FragmentImagesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [FullScreenImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FullScreenImageFragment : Fragment() {
    val viewModel : AppViewModel by activityViewModels()

    private var _binding: FragmentFullScreenImageBinding? = null
    private val binding get() = _binding!!

    fun yesPressed (imageId : String) {
        Log.d("ImagesFragment","in yesPressed(): imageId = $imageId")
        binding.viewModel?.deleteImage(imageId)
    }

    fun deleteClicked (imageId : String) {
        ConfirmDeletePopup(imageId,::yesPressed).show(childFragmentManager,
            ConfirmDeletePopup.TAG)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFullScreenImageBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        val view = binding.root
        val imageId = FullScreenImageFragmentArgs.fromBundle(requireArguments()).imageId

        //get image object given imageId from firebase, load into xml
        viewModel.getImageById(imageId) { image ->
            if (image != null) {
                //load image
                Glide.with(binding.root)
                    .load(image.url)
                    .placeholder(R.drawable.ic_camera_icon_splash)
                    .into(binding.imageSrc)
            } else {
                //error
                Log.d("ViewModel", "Image retrieval failed.")
            }
        }

        //delete button click listener, need to pass in imageId to delete
        binding.deleteButton.setOnClickListener {
            deleteClicked(imageId)
        }

        //navigate back to imagesFragment when back button clicked
        viewModel.navigateToGallery.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate) {
                view.findNavController()
                    .navigate(R.id.action_fullScreenImageFragment_to_imagesFragment)
                viewModel.onNavigatedToGallery()
            }
        })

        return view
    }

}