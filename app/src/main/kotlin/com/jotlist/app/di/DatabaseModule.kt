package com.jotlist.app.di

import android.content.Context
import androidx.room.Room
import com.jotlist.app.data.local.database.JotListDatabase
import com.jotlist.app.data.local.database.dao.ListItemDao
import com.jotlist.app.data.local.database.dao.ShoppingListDao
import com.jotlist.app.data.local.database.dao.SuggestionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the Room database instance as a singleton.
     */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): JotListDatabase {
        return Room.databaseBuilder(
            context,
            JotListDatabase::class.java,
            JotListDatabase.DATABASE_NAME,
        ).build()
    }

    /**
     * Provides the ShoppingListDao from the database.
     */
    @Provides
    @Singleton
    fun provideShoppingListDao(database: JotListDatabase): ShoppingListDao {
        return database.shoppingListDao()
    }

    /**
     * Provides the ListItemDao from the database.
     */
    @Provides
    @Singleton
    fun provideListItemDao(database: JotListDatabase): ListItemDao {
        return database.listItemDao()
    }

    /**
     * Provides the SuggestionDao from the database.
     */
    @Provides
    @Singleton
    fun provideSuggestionDao(database: JotListDatabase): SuggestionDao {
        return database.suggestionDao()
    }
}
