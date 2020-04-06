package com.danhdueexoictif.androidgenericadapter.utils.locale;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({LanguageDef.LANGUAGE_ENGLISH, LanguageDef.LANGUAGE_JAPANESE, LanguageDef.LANGUAGE_VIETNAMESE})
public @interface LanguageDef {
    String LANGUAGE_ENGLISH = "en";
    String LANGUAGE_JAPANESE = "ja";
    String LANGUAGE_VIETNAMESE = "vi";
}
