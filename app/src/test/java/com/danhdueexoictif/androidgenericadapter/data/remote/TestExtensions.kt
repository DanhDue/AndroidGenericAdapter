package com.danhdueexoictif.androidgenericadapter.data.remote

import com.google.common.reflect.TypeToken
import java.lang.reflect.Type

internal inline fun <reified T> typeOf(): Type = object : TypeToken<T>() {}.type
