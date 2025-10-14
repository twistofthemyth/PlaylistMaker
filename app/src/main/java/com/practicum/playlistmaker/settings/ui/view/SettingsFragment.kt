package com.practicum.playlistmaker.settings.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentComposeBinding
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.compose.SettingsScreen
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.util.event.SingleLiveEventObserver
import com.practicum.playlistmaker.util.ui_utils.IntentUtils
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by activityViewModel<SettingsViewModel>()
    private var _binding: FragmentComposeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComposeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeView.setContent {
            SettingsScreen(viewModel)
        }
        observeNavigation()
        observeThemeChange()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun observeThemeChange() {
        viewModel.getChangeThemeEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { newTheme ->
                when (newTheme) {
                    AppStyle.LIGHT -> (requireActivity().application as App).setLightTheme()
                    AppStyle.DARK -> (requireActivity().application as App).setDarkTheme()
                }
            })
    }

    private fun observeNavigation() {
        viewModel.getNavigationEvent()
            .observe(viewLifecycleOwner, SingleLiveEventObserver { destination ->
                when (destination) {
                    is SettingsViewModel.NavigationDestination.Share -> {
                        val shareIntent = Intent(Intent.ACTION_SENDTO).apply {
                            setData(IntentUtils.URI_TYPE_SMS.toUri())
                            putExtra(
                                IntentUtils.EXTRA_TYPE_SMS_TEXT,
                                getString(R.string.share_app_url)
                            )
                        }
                        startActivity(shareIntent)
                    }

                    is SettingsViewModel.NavigationDestination.Support -> {
                        val sendToSupportIntent = Intent(Intent.ACTION_SENDTO).apply {
                            setData(IntentUtils.URI_TYPE_EMAIL.toUri())
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_email)))
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                getString(R.string.support_default_subject)
                            )
                            putExtra(Intent.EXTRA_TEXT, getString(R.string.support_default_text))
                        }
                        startActivity(sendToSupportIntent)
                    }

                    is SettingsViewModel.NavigationDestination.Agreement -> {
                        val viewAgreementIntent =
                            Intent(Intent.ACTION_VIEW, getString(R.string.agreement_url).toUri())
                        startActivity(viewAgreementIntent)
                    }
                }
            })
    }
}