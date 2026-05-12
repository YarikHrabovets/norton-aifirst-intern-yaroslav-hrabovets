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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSecurityRepository(): SecurityRepository {
        return MockSecurityRepository()
    }

    @Provides
    @Singleton
    fun provideGetSecurityReportUseCase(repository: SecurityRepository): GetSecurityReportUseCase {
        return GetSecurityReportUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideStartSecurityScanUseCase(): StartSecurityScanUseCase {
        return StartSecurityScanUseCase()
    }

    @Provides
    @Named("IO")
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Named("Main")
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main.immediate
}
