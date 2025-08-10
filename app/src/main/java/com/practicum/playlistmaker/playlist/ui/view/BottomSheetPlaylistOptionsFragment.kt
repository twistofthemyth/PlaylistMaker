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
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BottomSheetPlaylistOptionsFragment(val playlistId: Long) : BottomSheetDialogFragment(),
    PlaylistSharer {

    companion object {
        const val TAG = "BottomSheetPlaylistOptionsViewModel"
    }

    private var _binding: FragmentBottomSheetTracksBinding? = null
    private val binding get() = _binding!!

    private val playlistViewModel by viewModel<PlaylistViewModel> {
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
        observePlaylistState()
        observeShareEvent()
    }

    private fun observePlaylistState() {
        playlistViewModel.getState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistViewModel.PlaylistState.Content -> {
                    Glide.with(binding.imageIv)
                        .load(it.playlist.image.toUri())
                        .centerCrop()
                        .placeholder(R.drawable.placeholder_album)
                        .into(binding.imageIv)
                    binding.nameTv.text = it.playlist.name
                    binding.countTv.text = resources.getQuantityString(
                        R.plurals.plular_track,
                        it.playlist.track.size,
                        it.playlist.track.size
                    )

                    binding.deleteTv.setOnClickListener { showAlert() }

                    binding.editTv.setOnClickListener {
                        val direction =
                            PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistEditorFragment(
                                playlistId
                            )
                        findNavController().navigate(direction)
                        dismiss()
                    }
                    binding.shareTv.setOnClickListener {
                        playlistViewModel.sharePlaylist()
                    }
                }

                PlaylistViewModel.PlaylistState.Loading -> {}
            }
        }
    }

    private fun observeShareEvent() {
        playlistViewModel.getShareNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { event ->
                dismiss()
                when (event) {
                    is PlaylistViewModel.ShareState.Content -> startActivity(makeShareIntent(event))
                    PlaylistViewModel.ShareState.Empty -> makeEmptyListToast(requireContext()).show()
                }
            })
    }

    fun showAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_playlist_title)
            .setMessage(R.string.dialog_delete_playlist_message)
            .setNegativeButton(R.string.dialog_cancel) { dialog, which -> }
            .setPositiveButton(R.string.dialog_delete) { dialog, which ->
                mediaViewModel.deletePlaylist(playlistId)
                dismiss()
                parentFragmentManager.popBackStack()
                showToast()
            }
            .show()
    }

    fun showToast() {
        Toast.makeText(
            requireContext(),
            requireActivity().getString(R.string.playlist_deleted).format(binding.nameTv.text),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}