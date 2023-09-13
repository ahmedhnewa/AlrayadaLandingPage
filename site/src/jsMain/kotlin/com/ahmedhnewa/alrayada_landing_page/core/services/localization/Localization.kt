package com.ahmedhnewa.alrayada_landing_page.core.services.localization

import com.ahmedhnewa.alrayada_landing_page.core.data.StringRes

interface Localization {
    fun getLocalizedString(resValue: StringRes, locale: Local): String
}