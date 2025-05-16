package di

interface Factory<T> {
    fun create(): T
}