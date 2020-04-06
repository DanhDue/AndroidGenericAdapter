package com.danhdueexoictif.androidgenericadapter.data.local.pref

import android.content.SharedPreferences
import androidx.collection.LongSparseArray
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPrefsApi constructor(var sharedPreferences: SharedPreferences) {

    fun set(key: String, value: String) =
        sharedPreferences.edit().apply { putString(key, value) }.apply()

    fun get(key: String, defValue: String?) = sharedPreferences.getString(key, defValue)

    fun set(key: String, value: Int) = sharedPreferences.edit().apply { putInt(key, value) }.apply()

    fun get(key: String, defValue: Int) = sharedPreferences.getInt(key, defValue)

    fun set(key: String, value: Boolean) =
        sharedPreferences.edit().apply { putBoolean(key, value) }.apply()

    fun get(key: String, defValue: Boolean) = sharedPreferences.getBoolean(key, defValue)

    fun set(key: String, value: Long) =
        sharedPreferences.edit().apply { putLong(key, value) }.apply()

    fun get(key: String, defValue: Long) = sharedPreferences.getLong(key, defValue)

    fun clear() = sharedPreferences.edit().apply { clear() }.apply()

    fun remove(key: String) = sharedPreferences.edit().apply { remove(key) }.apply()

    fun <T> setList(key: String, list: List<T>) {
        val json = Gson().toJson(list)
        set(key, json)
    }

    fun setLongSparseArray(key: String, array: LongSparseArray<Boolean>) {
        val json = Gson().toJson(array)
        set(key, json)
    }

    fun contains(key: String) = sharedPreferences.contains(key)

    inline fun <reified T> setObject(key: String, value: T) = sharedPreferences.edit {
        putString(key, Gson().toJson(value))
        apply()
    }

    inline fun <reified T> getObject(key: String): T? = run {
        val data = get(key, "")
        return if (data.isNullOrEmpty()) {
            null
        } else {
            Gson().fromJson(data, T::class.java)
        }
    }
}

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)
