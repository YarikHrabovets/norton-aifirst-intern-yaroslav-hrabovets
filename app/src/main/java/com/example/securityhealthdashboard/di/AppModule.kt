package com.example.securityhealthdashboard.di

import com.example.securityhealthdashboard.data.repository.MockSecurityRepository
import com.example.securityhealthdashboard.data.repository.SecurityRepository
import com.example.securityhealthdashboard.domain.usecase.GetSecurityReportUseCase
import com.example.securityhealthdashboard.domain.usecase.StartSecurityScanUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

/**
 * Hilt module that provides project-wide dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides the singleton instance of [SecurityRepository].
     * Currently using a Mock implementation for development and testing.
     */
    @Provides
    @Singleton
    fun provideSecurityRepository(): SecurityRepository {
        return MockSecurityRepository()
    }

    /**
     * Provides the [GetSecurityReportUseCase].
     */
    @Provides
    @Singleton
    fun provideGetSecurityReportUseCase(repository: SecurityRepository): GetSecurityReportUseCase {
        return GetSecurityReportUseCase(repository)
    }

    /**
     * Provides the [StartSecurityScanUseCase].
     */
    @Provides
    @Singleton
    fun provideStartSecurityScanUseCase(): StartSecurityScanUseCase {
        return StartSecurityScanUseCase()
    }

    /**
     * IO Dispatcher for background tasks like network or disk I/O.
     */
    @Provides
    @Named("IO")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Main Dispatcher for UI-related tasks, using immediate to avoid unnecessary frame delays.
     */
    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
