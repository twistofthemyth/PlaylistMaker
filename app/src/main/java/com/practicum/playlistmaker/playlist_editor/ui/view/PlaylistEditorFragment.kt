package com.practicum.playlistmaker.playlist_editor.ui.view

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
import androidx.core.net.toUri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistEditorBinding
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.playlist_editor.ui.view_model.PlaylistEditorViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PlaylistEditorFragment : Fragment() {
    private val args by navArgs<PlaylistEditorFragmentArgs>()
    private val createPlaylistModel: PlaylistEditorViewModel by viewModel<PlaylistEditorViewModel> {
        parametersOf(
            args.playlistId
        )
    }
    private val playlistViewModel: MediaViewModel by activityViewModel<MediaViewModel>()
    private var _binding: FragmentPlaylistEditorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistEditorBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        setupNavigation()
        setupPickMedia()
        setupTextFields()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeState() {
        createPlaylistModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                PlaylistEditorViewModel.CreatePlaylistState.Created -> enableConfirmButton()
                PlaylistEditorViewModel.CreatePlaylistState.InEdit -> disableConfirmButton()
                PlaylistEditorViewModel.CreatePlaylistState.InitCreation -> enableCreateMode()
                is PlaylistEditorViewModel.CreatePlaylistState.InitEdition -> enableEditMode(it.playlist)
                PlaylistEditorViewModel.CreatePlaylistState.ReadyForCreate -> enableConfirmButton()
            }
        }
    }

    private fun enableCreateMode() {
        binding.toolbar.text = requireActivity().getString(R.string.create_playlist)
        binding.createBtn.text = requireActivity().getString(R.string.create_playlist_button)
        disableConfirmButton()
        setupConfirmButtonForCreating()
    }

    private fun enableEditMode(playlist: Playlist) {
        binding.toolbar.setText(R.string.edit_playlist)
        binding.createBtn.setText(R.string.save_playlist_button)
        binding.nameEt.setText(playlist.name)
        binding.descriptionEt.setText(playlist.description)
        if (playlist.image.isNotEmpty()) {
            showPreview(playlist.image.toUri())
        }
        enableConfirmButton()
        setupConfirmButtonForEditing()
    }

    private fun setupNavigation() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        binding.arrowBackIv.setOnClickListener {
            backPressedCallback.handleOnBackPressed()
        }
    }

    private fun setupConfirmButtonForCreating() {
        binding.createBtn.setOnClickListener {
            lifecycleScope.launch {
                val newPlaylist = createPlaylistModel.exitEditor()
                playlistViewModel.createPlaylist(newPlaylist)

                if (args.trackId.isNotEmpty()) {
                    val direction =
                        PlaylistEditorFragmentDirections.actionPlaylistEditorFragmentToTrackFragment(
                            args.trackId
                        )
                    findNavController().navigate(direction)
                } else {
                    parentFragmentManager.popBackStack()
                }


                Toast.makeText(
                    requireContext(),
                    requireActivity().getString(R.string.playlist_created)
                        .format(binding.nameEt.text), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupConfirmButtonForEditing() {
        binding.createBtn.setOnClickListener {
            lifecycleScope.launch {
                val newPlaylist = createPlaylistModel.exitEditor()
                playlistViewModel.updatePlaylist(newPlaylist)

                val direction =
                    PlaylistEditorFragmentDirections.actionPlaylistEditorFragmentToPlaylistFragment(
                        args.playlistId
                    )
                findNavController().navigate(direction)

                Toast.makeText(
                    requireContext(),
                    requireActivity().getString(R.string.playlist_saved)
                        .format(binding.nameEt.text), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupPickMedia() {
        val pickMedia = registerPickMedia()
        binding.addImageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun setupTextFields() {
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
    }

    private fun disableConfirmButton() {
        binding.createBtn.isEnabled = false
    }

    private fun enableConfirmButton() {
        binding.createBtn.isEnabled = true
    }

    private fun registerPickMedia(): ActivityResultLauncher<PickVisualMediaRequest> {
        return registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                showPreview(uri)
                createPlaylistModel.setImage(saveImageToStorage(uri))
            }
        }
    }

    private fun showPreview(uri: Uri) {
        Glide.with(binding.addImageView)
            .load(uri)
            .centerCrop()
            .into(binding.addImageView)
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            when (createPlaylistModel.getState().value) {
                is PlaylistEditorViewModel.CreatePlaylistState.ReadyForCreate,
                is PlaylistEditorViewModel.CreatePlaylistState.InEdit -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(requireActivity().getString(R.string.cancel_playlist_creating_title))
                        .setMessage(requireActivity().getString(R.string.cancel_playlist_creating_subtitle))
                        .setNeutralButton(requireActivity().getString(R.string.dialog_cancel)) { dialog, which -> }
                        .setPositiveButton(requireActivity().getString(R.string.dialog_complete)) { dialog, which ->
                            parentFragmentManager.popBackStack()
                        }
                        .show()
                }

                else -> parentFragmentManager.popBackStack()
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