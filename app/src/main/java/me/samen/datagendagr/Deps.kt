
package me.samen.datagendagr

import dagger.Component
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Qualifier

/**
 * @author satosh.dhanymaraju
 */

@Qualifier
annotation class isbn

@Qualifier
annotation class username

@Qualifier
annotation class bookname


data class Book @Inject constructor(@isbn val isbn: String, @bookname val name: String, val price: Double)

data class User @Inject constructor(@username val name: String, val age: Int, val purchases: List<Book>)


@Module
class UsrModule {
    private val r = Random()
    private val names = arrayListOf("Albus", "Severus", "Potter")

    @Provides
    @isbn
    fun prvdIsbn() = "ISBN-${System.currentTimeMillis()}"

    @Provides
    @username
    fun prvdUsername() = names[r.nextInt(names.size)]

    @Provides
    @bookname
    fun prvdBookname() = "book-${System.currentTimeMillis()}"

    @Provides
    fun prvdI() = 42

    @Provides
    fun prvdF() = 1.0

    @Provides
    fun prvdBooks(bookprv: Provider<Book>): List<Book> {
        return IntRange(1, r.nextInt(6))
                .map { bookprv.get() }
                .toList()
    }

}

@Component(modules = [UsrModule::class])
interface UsrComp {
    fun inject(mainActivity: MainActivity)
}