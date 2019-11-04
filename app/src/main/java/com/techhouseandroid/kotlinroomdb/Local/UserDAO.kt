package com.techhouseandroid.kotlinroomdb.Local

import android.arch.persistence.room.*

import com.techhouseandroid.kotlinroomdb.Model.User
import io.reactivex.Flowable

/**
 * Created by Nipu on 11/3/2018.
 */

@Dao
interface UserDAO {

    @get:Query("SELECT * FROM users")
    val allusers:Flowable<List<User>>

    @Query("SELECT * FROM users WHERE id=:userId")
    fun getUserById(userId:Int):Flowable<User>


    @Insert
    fun insertUser(vararg users:User)


    @Update
    fun updateUser(vararg users:User)

    @Delete
    fun deleteUser(user:User)

    @Query("DELETE FROM users")
    fun deleteAllUser()




}