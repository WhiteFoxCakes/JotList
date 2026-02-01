package com.jotlist.app.di

import com.jotlist.app.data.repository.ListItemRepositoryImpl
import com.jotlist.app.data.repository.ShoppingListRepositoryImpl
import com.jotlist.app.data.repository.SuggestionRepositoryImpl
import com.jotlist.app.domain.repository.ListItemRepository
import com.jotlist.app.domain.repository.ShoppingListRepository
import com.jotlist.app.domain.repository.SuggestionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing repository bindings.
 * Binds repository interfaces to their implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds [ShoppingListRepositoryImpl] to [ShoppingListRepository].
     */
    @Binds
    @Singleton
    abstract fun bindShoppingListRepository(
        impl: ShoppingListRepositoryImpl,
    ): ShoppingListRepository

    /**
     * Binds [ListItemRepositoryImpl] to [ListItemRepository].
     */
    @Binds
    @Singleton
    abstract fun bindListItemRepository(
        impl: ListItemRepositoryImpl,
    ): ListItemRepository

    /**
     * Binds [SuggestionRepositoryImpl] to [SuggestionRepository].
     */
    @Binds
    @Singleton
    abstract fun bindSuggestionRepository(
        impl: SuggestionRepositoryImpl,
    ): SuggestionRepository
}
