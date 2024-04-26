package com.ltu.m7019e.themoviedb.database

import androidx.room.TypeConverter

class GenreConverters {
    @TypeConverter
    fun fromString (value: String?): MutableList<Int> {
        if (value == null) {
            return mutableListOf()
        }
        val stringList = value.split(",")
        return stringList.map { it.toInt() }.toMutableList()
    }

    @TypeConverter
    fun fromList(values: List<Int>?): String {
        if (values.isNullOrEmpty()) {
            return ""
        }
        var res = ""
        for (i in values) {
            res += "$i,"
        }
        return res.substring(0, res.length-1)
    }
}