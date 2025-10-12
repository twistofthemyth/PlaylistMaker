package com.practicum.playlistmaker.settings.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.models.AppStyle
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.practicum.playlistmaker.util.ui_utils.Fonts
import com.practicum.playlistmaker.util.ui_utils.Material2Switcher
import com.practicum.playlistmaker.util.ui_utils.TextScreenTitle
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    Column {
        TextScreenTitle(stringResource(R.string.settings_button_txt))
        SettingsItem(
            textId = R.string.setting_item_theme,
            onCheckedChange = {
                viewModel.setTheme(if (it) AppStyle.DARK else AppStyle.LIGHT)
            },
            checked = viewModel.getScreenState().value?.theme == AppStyle.DARK
        )
        SettingsItem(
            textId = R.string.setting_item_share,
            iconId = R.drawable.ic_share,
            onClick = {
                viewModel.navigateTo(
                    SettingsViewModel.NavigationDestination.Share
                )
            })
        SettingsItem(
            textId = R.string.setting_item_support,
            iconId = R.drawable.ic_support,
            onClick = {
                viewModel.navigateTo(
                    SettingsViewModel.NavigationDestination.Support
                )
            })
        SettingsItem(
            textId = R.string.setting_item_agreement,
            iconId = R.drawable.ic_arrow_forward,
            onClick = {
                viewModel.navigateTo(
                    SettingsViewModel.NavigationDestination.Agreement
                )
            })
    }
}

@Composable
fun SettingsItem(
    textId: Int,
    iconId: Int? = null,
    onClick: (() -> Unit)? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    checked: Boolean? = null
) {
    Row(
        modifier = Modifier
            .height(61.dp)
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(textId),
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            style = TextStyle(
                fontFamily = Fonts.ysDisplayRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = colorResource(R.color.text_color)
            )
        )
        if (iconId != null) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp),
                tint = colorResource(R.color.text_color)
            )
        }
        if (onCheckedChange != null && checked != null) {
            var isChecked by remember { mutableStateOf(checked) }
            Material2Switcher(
                checked = isChecked,
                onCheckedChange = { isChecked = !isChecked },
                modifier = Modifier.padding(end = 8.dp)
            )
            LaunchedEffect(isChecked) {
                delay(200)
                onCheckedChange(isChecked)
            }
        }
    }
}