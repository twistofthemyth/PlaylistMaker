package com.practicum.playlistmaker.createplaylist.ui.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createplaylist.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CreatePlaylistFragment : Fragment() {

    private val args by navArgs<CreatePlaylistFragmentArgs>()
    private val createPlaylistModel: CreatePlaylistViewModel by viewModel<CreatePlaylistViewModel>()
    private val playlistViewModel: MediaViewModel by activityViewModel<MediaViewModel>()
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
            when (createPlaylistModel.getState().value) {
                is CreatePlaylistViewModel.CreatePlaylistState.ReadyForCreate,
                is CreatePlaylistViewModel.CreatePlaylistState.InEdit -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Завершить создание плейлиста?")
                        .setMessage("Все несохраненные данные будут потеряны")
                        .setNegativeButton("Нет") { dialog, which -> }
                        .setPositiveButton("Да") { dialog, which ->
                            navigateToPrevScreen()
                        }
                        .show()
                }

                else -> navigateToPrevScreen()
            }
        }

        createPlaylistModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is CreatePlaylistViewModel.CreatePlaylistState.Created -> {
                    navigateToPrevScreen()
                    (requireActivity().application as App).showToast(
                        binding.root,
                        "Плейлист ${binding.nameEt.text} создан"
                    )
                }

                is CreatePlaylistViewModel.CreatePlaylistState.Empty -> binding.createBtn.isEnabled =
                    false

                is CreatePlaylistViewModel.CreatePlaylistState.InEdit -> binding.createBtn.isEnabled =
                    false

                is CreatePlaylistViewModel.CreatePlaylistState.ReadyForCreate -> binding.createBtn.isEnabled =
                    true
            }
        }

        val pickMedia = registerPickMedia()
        binding.addImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.nameEt.doOnTextChanged { text, start, before, count ->
            if (text != null && text.isNotEmpty()) {
                createPlaylistModel.setName(text.toString())
            }
        }

        binding.descriptionEt.doOnTextChanged { text, start, before, count ->
            if (text != null && text.isNotEmpty()) {
                createPlaylistModel.setDescription(text.toString())
            }
        }

        binding.createBtn.setOnClickListener {
            createPlaylistModel.createPlaylist()
            lifecycleScope.async {
                delay(100)
                playlistViewModel.updatePlaylist()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToPrevScreen() {
        if (args.trackId.isEmpty()) {
            findNavController().navigate(R.id.action_createPlaylistFragment_to_mediaFragment)
        } else {
            val direction =
                CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToTrackFragment(args.trackId)
            findNavController().navigate(direction)
        }
    }

    private fun registerPickMedia(): ActivityResultLauncher<PickVisualMediaRequest> {
        return registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                binding.addImageView.setImageURI(uri)
                createPlaylistModel.setImage(saveImageToStorage(uri))
            }
        }
    }

    private fun saveImageToStorage(uri: Uri): Uri {
        val path = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File(path, UUID.randomUUID().toString())
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.toUri()
    }
}