package com.artofmainstreams.examples

import android.content.Context
import com.artofmainstreams.examples.data.ResourceManager
import com.artofmainstreams.examples.data.SomeRepository
import com.artofmainstreams.examples.data.SomeRepositoryImpl
import com.artofmainstreams.examples.data.SomeService
import com.artofmainstreams.examples.ui.example01.Fragment01
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Qualifier
import javax.inject.Scope
import javax.inject.Singleton

@Component(modules = [AppModule::class], dependencies = [AppDependencies::class])
@Singleton
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(fragment: Fragment01)

    @Component.Builder
    interface Builder {

        fun appDependencies(appDependencies: AppDependencies): Builder
        fun build(): AppComponent
    }
}

interface AppDependencies {
    val context: Context
}

@Module(includes = [NetworkModule::class, AppBindModule::class], subcomponents = [FeatureComponent::class])
object AppModule {
    @Provides
    @Singleton
    fun provideResourceManager(context: Context) = ResourceManager(context)
}

@Module
object NetworkModule {

    @Provides
    fun provideProductionNewsService(): SomeService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com")
            .build()
        return retrofit.create()
    }

    @Provides
    @Stage
    fun provideStageNewsService(): SomeService {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.github.com")
            .build()
        return retrofit.create()
    }
}

@Module
interface AppBindModule {

    @Suppress("FunctionName")
    @Binds
    fun bindSomeRepositoryImpl_to_SomeRepository(
        someRepositoryImpl: SomeRepositoryImpl
    ): SomeRepository
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Prod


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Stage

@Scope
annotation class Feature

@Module
object FeatureModule

@Feature
@Subcomponent(modules = [FeatureModule::class])
interface FeatureComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureComponent
    }
}
