package com.techhouseandroid.kotlinroomdb.Database

import com.techhouseandroid.kotlinroomdb.Model.User
import io.reactivex.Flowable

/**
 * Created by Nipu on 11/3/2018.
 */
interface IUserDataSource {

    val allUsers:Flowable<List<User>>

    fun getUserById(UserId:Int):Flowable<User>
    fun insertUser(vararg users:User)
    fun updateUser(vararg users:User)
    fun deletUser(user:User)
    fun deleteAllUsers()

}