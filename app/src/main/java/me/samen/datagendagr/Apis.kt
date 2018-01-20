
package me.samen.datagendagr

import io.reactivex.Observable
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Provider

interface UserApi {
    @GET("/users/all")
    fun users() : Observable<List<User>>
}

class MockUserApi @Inject constructor(private val user : Provider<User>): UserApi {
    override fun users(): Observable<List<User>> {
        return Observable.just(listOf(user.get(), user.get()))
    }
}