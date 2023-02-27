package com.artofmainstreams.examples.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Phone",  // имя, отображаемое над Preview
    group = "Devices",   // можно группировать превьюшки
    apiLevel = 33,  // версия андроида при рендеринге
    widthDp = -1,   // максимальная ширина для composable-функции
    heightDp = -1,  // максимальная высота для composable-функции
    locale = "en",   // настройка языка
    fontScale = 1.5f,    // пользовательский размер шрифта
    showSystemUi = true, // показать статус бар, actionbar
    showBackground = true, // будет ли отрисован фон по умолчанию или указанный далее
    backgroundColor = 0xFFF7ED93,   // цвет фона
    uiMode = Configuration.UI_MODE_NIGHT_NO,   // маска, цветовая схема + тип устройства
    device = Devices.PIXEL_3   // размер экарана, плотность и форма
)
annotation class PhonePreview

@Preview(
    name = "Tablet",
    group = "Devices",
    showSystemUi = true,
    device = Devices.TABLET
)
annotation class TabletPreview

@PhonePreview
@TabletPreview
annotation class DevicesPreview