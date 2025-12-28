package com.shubham.jikananimeseekhotask.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shubham.jikananimeseekhotask.data.local.AnimeDao
import com.shubham.jikananimeseekhotask.data.local.AnimeEntity


@Database(
    entities =[AnimeEntity::class] ,
    version = 1,
    exportSchema = false
)
abstract  class AnimeDatabase: RoomDatabase() {
    abstract fun animeDao(): AnimeDao

}