package com.practicum.playlistmaker.compose_example.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.compose_example.domain.Contact

class ComposeExampleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactDetailsExample1()
//            ContactDetailsExample2()
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    fun ContactDetailsExample1() {
        ContactDetails(fullyMockedContact())
    }

    @Preview(showSystemUi = true)
    @Composable
    fun ContactDetailsExample2() {
        ContactDetails(partMockedContact())
    }

    @Composable
    fun ContactDetails(contact: Contact) {
        Column {
            ContactImage(contact)
            ContactName(contact)
            InfoRow("Телефон", contact.phone)
            InfoRow("Адрес", contact.address)
            InfoRow("E-mail", contact.email)
        }
    }


    @Composable
    fun ContactImage(contact: Contact) {
        Box(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
        ) {
            if (contact.imageRes == null) {
                Icon(
                    painter = painterResource(R.drawable.circle),
                    contentDescription = "icon",
                    tint = colorResource(R.color.list_item_icon_color),
                    modifier = Modifier
                        .size(152.dp)
                        .align(Alignment.Center)
                )
                Text(
                    text = "${contact.name[0]} ${contact.familyName[0]}",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.displayMedium
                )
            } else {
                Image(
                    painter = painterResource(contact.imageRes),
                    contentDescription = "Иконка профиля",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(152.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }

    @Composable
    fun ContactName(contact: Contact) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column {
                Text(
                    text = "${contact.name} ${contact.surname.orEmpty()}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Column {
                Row {
                    Text(
                        text = contact.familyName,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    if (contact.isFavorite) {
                        Icon(
                            painter = painterResource(R.drawable.star),
                            contentDescription = "Избранный",
                            modifier = Modifier
                                .size(size = 32.dp)
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically),
                            tint = colorResource(R.color.background_main)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun InfoRow(label: String, value: String?) {
        if (value != null) {
            Row(modifier = Modifier.padding(8.dp)) {
                Column(modifier = Modifier.weight(0.3f)) {}
                Column(modifier = Modifier.weight(0.25f)) {
                    Text(
                        text = "${label}: ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontStyle = Italic,
                    )
                }
                Column(modifier = Modifier.weight(0.45f)) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }

    private fun fullyMockedContact(): Contact {
        return Contact(
            name = "Леонид",
            surname = "Семенович",
            familyName = "Каневский",
            isFavorite = true,
            phone = "8 800 555 35 35",
            address = "г. Москва, 3-я улица Строителей, д.25, кв.12",
            email = "l.kanevsky@gmail.com",
            imageRes = R.drawable.compose_example_image
        )
    }

    private fun partMockedContact(): Contact {
        return Contact(
            name = "Леонид",
            familyName = "Каневский",
            isFavorite = false,
            phone = "8 800 555 35 35",
            address = "г. Москва, 3-я улица Строителей, д.25, кв.12"
        )
    }
}