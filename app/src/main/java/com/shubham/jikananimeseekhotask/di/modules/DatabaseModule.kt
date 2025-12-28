package com.shubham.jikananimeseekhotask.di.modules

import android.app.Application
import androidx.room.Room
import com.shubham.jikananimeseekhotask.data.local.AnimeDao
import com.shubham.jikananimeseekhotask.data.local.database.AnimeDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton

    fun provideDatabase(app: Application): AnimeDatabase {
        return Room.databaseBuilder(app, AnimeDatabase::class.java, "anime_db").build()
    }

    @Provides
    fun provideCartDao(db: AnimeDatabase): AnimeDao {
        return db.animeDao()
    }


}