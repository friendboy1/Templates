package com.artofmainstreams.examples.multibinding

import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Component(modules = [AnimalModule::class])
@Singleton
interface ZooComponent {
    val animals: Set<@JvmSuppressWildcards Animal>
    val zoo: Zoo
}

@Module
class AnimalModule {

    @Provides
    @IntoSet
    fun provideCat(): Animal = Cat()

    @Provides
    @ElementsIntoSet
    fun provideMultipleAnimals(
        dog: Dog,
        mouse: Mouse
    ): Set<Animal> = setOf(dog, mouse)
}