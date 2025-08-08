package com.practicum.playlistmaker.playlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentBottomSheetTracksBinding
import com.practicum.playlistmaker.media.ui.view_model.MediaViewModel
import com.practicum.playlistmaker.playlist.ui.view_model.BottomSheetPlaylistOptionsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomSheetPlaylistOptionsFragment(val playlistId: Long) : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "BottomSheetPlaylistOptionsViewModel"
    }

    private var _binding: FragmentBottomSheetTracksBinding? = null
    private val binding get() = _binding!!

    private val bottomSheetViewModel by viewModel<BottomSheetPlaylistOptionsViewModel> {
        parametersOf(
            playlistId
        )
    }

    private val mediaViewModel by activityViewModel<MediaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetTracksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetViewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is BottomSheetPlaylistOptionsViewModel.BottomSheetPlaylistOptionsState.Content -> {
                    Glide.with(binding.imageIv)
                        .load(it.playlist.image.toUri())
                        .placeholder(R.drawable.placeholder_album)
                        .into(binding.imageIv)
                    binding.nameTv.text = it.playlist.name
                    binding.countTv.text = "${it.playlist.track.size} + треков"

                    binding.deleteTv.setOnClickListener { showAlert() }
                }

                BottomSheetPlaylistOptionsViewModel.BottomSheetPlaylistOptionsState.Error -> dismiss()
            }
        }

    }

    fun showAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_playlist_title)
            .setMessage(R.string.dialog_delete_playlist_message)
            .setNegativeButton(R.string.dialog_no) { dialog, which -> }
            .setPositiveButton(R.string.dialog_yes) { dialog, which ->
                bottomSheetViewModel.deletePlaylist()
                mediaViewModel.updatePlaylist()
                dismiss()
                findNavController().navigate(R.id.action_playlistFragment_to_mediaFragment)
                showToast()
            }
            .show()
    }

    fun showToast() {
        Toast.makeText(requireContext(), R.string.playlist_item_delete, Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}