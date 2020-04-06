package com.danhdueexoictif.androidgenericadapter.utils.extension

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import com.danhdueexoictif.androidgenericadapter.utils.locale.LanguageDef.*

fun String.excludeHiragana(): String {
    return this.replace("｜", "{")
        .replace("《", ";")
        .replace("》", "}")
        .replace(Regex(Constants.REGEX_EXCLUDE_HIRAGANA), "")
        .replace("{", "")
}

fun String.toIntListByDelimiter(delimiter: String): List<Int> {
    val result = mutableListOf<Int>()
    this.split(delimiter).forEach { result.add(it.toInt()) }
    return result
}

fun String.mapLanguageToQueryParams(): String = when (this) {
    LANGUAGE_VIETNAMESE -> "vi"
    LANGUAGE_JAPANESE -> "jp"
    LANGUAGE_ENGLISH -> "en"
    else -> "vi"
}

fun String.getSpannedHtml(): Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
} else {
    Html.fromHtml(this)
}

fun String?.getAccessToken(): String? = if (isNullOrEmpty()) null else "Bearer $this"

fun String?.getHtmlContent(): String? = if (isNullOrEmpty()) null else ("<html><head>"
        + "<style type=\"text/css\">@font-face{font-family: \"Font\";src:url(\"file:///android_asset/OpenSans.ttf\")}"
        + "body{margin: 0; padding: 0; font-family: \"Font\", serif; font-size: medium; text-align: justify; color:#ffffff}"
        + "</style></head>"
        + "<body>"
        + "<p align=\"justify\">"
        + this
        + "</p> "
        + "</body></html>")
