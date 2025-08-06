package com.practicum.playlistmaker.createplaylist.ui.view

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createplaylist.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreateBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File
import java.net.URI

class CreatePlaylistFragment : Fragment() {

    private val viewModel: CreatePlaylistViewModel by activityViewModel<CreatePlaylistViewModel>()
    private var _binding: FragmentPlaylistCreateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBackIv.setOnClickListener {
            findNavController().navigate(R.id.action_createPlaylistFragment_to_mediaFragment)
        }

        viewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is CreatePlaylistViewModel.CreatePlaylistState.Completed -> binding.createBtn.isEnabled =
                    true

                is CreatePlaylistViewModel.CreatePlaylistState.Empty -> binding.createBtn.isEnabled =
                    false

                is CreatePlaylistViewModel.CreatePlaylistState.InEdit -> binding.createBtn.isEnabled =
                    false
            }
        }

        val pickMedia = registerPickMedia()
        binding.addImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.nameEt.doOnTextChanged { text, start, before, count ->
            if (text != null && text.isNotEmpty()) {
                viewModel.setName(text.toString())
            }
        }

        binding.descriptionEt.doOnTextChanged { text, start, before, count ->
            if (text != null && text.isNotEmpty()) {
                viewModel.setDescription(text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun registerPickMedia(): ActivityResultLauncher<PickVisualMediaRequest> {
        return registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.addImageView.setImageURI(uri)
                viewModel.setImage(uri)
            }
        }
    }

    private fun saveImageToStorage(uri: URI) {
        val path = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File(path, "123.jpg")
    }
}