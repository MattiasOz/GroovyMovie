package com.ltu.m7019e.themoviedb

import com.ltu.m7019e.themoviedb.database.GenreConverters
import org.junit.Test

class GenreConverterTest {

    @Test
    fun fromStringTest(){
        val conv = GenreConverters()
        val testString: String = "2,43,6,2"
        val res = conv.fromString(testString)
        val answer = listOf(2, 43, 6, 2)
        assert(res == answer) { "$res is not $answer" }
    }

    @Test
    fun fromListTest(){
        val conv = GenreConverters()
        val testList = listOf(2, 3, 4, 5)
        val res = conv.fromList(testList)
        val answer = "2,3,4,5"
        assert(res == answer) { "$res is not $answer" }
    }

    @Test
    fun restoreString(){
        val conv = GenreConverters()
        val testString: String = "2,43,6,2"
        val middle = conv.fromString(testString)
        val res = conv.fromList(middle)
        assert(res == testString) { "$res is not $testString" }
    }

    @Test
    fun restoreListTest(){
        val conv = GenreConverters()
        val testList = listOf(65, 3, 4, 5)
        val middle = conv.fromList(testList)
        val res = conv.fromString(middle)
        assert(res == testList) { "$res is not $testList" }
    }
}