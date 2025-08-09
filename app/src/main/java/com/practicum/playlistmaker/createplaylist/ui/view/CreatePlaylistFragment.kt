package com.practicum.playlistmaker.createplaylist.ui.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.bumptech.glide.Glide
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.createplaylist.ui.view_model.CreatePlaylistViewModel
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreateBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import kotlinx.coroutines.launch
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

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when (createPlaylistModel.getState().value) {
                is CreatePlaylistViewModel.CreatePlaylistState.ReadyForCreate,
                is CreatePlaylistViewModel.CreatePlaylistState.InEdit -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle(requireActivity().getString(R.string.cancel_playlist_creating_title))
                        .setMessage(requireActivity().getString(R.string.cancel_playlist_creating_subtitle))
                        .setNeutralButton(requireActivity().getString(R.string.dialog_cancel)) { dialog, which -> }
                        .setPositiveButton(requireActivity().getString(R.string.dialog_complete)) { dialog, which ->
                            navigateToPrevScreen()
                        }
                        .show()
                }

                else -> navigateToPrevScreen()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        binding.arrowBackIv.setOnClickListener {
            backPressedCallback.handleOnBackPressed()
        }

        val pickMedia = registerPickMedia()
        binding.addImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.nameEt.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                createPlaylistModel.setName(text.toString())
            }
        }

        binding.descriptionEt.doOnTextChanged { text, start, before, count ->
            if (text != null) {
                createPlaylistModel.setDescription(text.toString())
            }
        }

        setupCreateBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupCreateBtn() {
        binding.createBtn.apply {
            createPlaylistModel.getState().observe(viewLifecycleOwner) { state ->
                isEnabled = when (state) {
                    CreatePlaylistViewModel.CreatePlaylistState.Created,
                    CreatePlaylistViewModel.CreatePlaylistState.ReadyForCreate -> true

                    else -> false
                }
            }
            setOnClickListener {
                lifecycleScope.launch {
                    val newPlaylist = createPlaylistModel.exitEditor()
                    playlistViewModel.createPlaylist(newPlaylist)
                    navigateToPrevScreen()
                    Toast.makeText(
                        requireContext(),
                        requireActivity().getString(R.string.playlist_created)
                            .format(binding.nameEt.text), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToPrevScreen() {
        val direction = if (args.trackId.isEmpty()) {
            CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToMediaFragment(1)
        } else {
            CreatePlaylistFragmentDirections.actionCreatePlaylistFragmentToTrackFragment(args.trackId)
        }
        findNavController().navigate(direction)
    }

    private fun registerPickMedia(): ActivityResultLauncher<PickVisualMediaRequest> {
        return registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Glide.with(binding.addImageView)
                    .load(uri)
                    .centerCrop()
                    .into(binding.addImageView)
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