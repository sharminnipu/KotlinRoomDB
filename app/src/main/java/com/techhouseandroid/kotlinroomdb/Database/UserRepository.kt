package com.techhouseandroid.kotlinroomdb.Database

import com.techhouseandroid.kotlinroomdb.Model.User
import io.reactivex.Flowable

/**
 * Created by Nipu on 11/3/2018.
 */
class UserRepository(private val mLocationDataSource: IUserDataSource):IUserDataSource {


    override val allUsers: Flowable<List<User>>

        get()=mLocationDataSource.allUsers


    override fun getUserById(UserId: Int): Flowable<User> {

        return mLocationDataSource.getUserById(UserId)

    }

    override fun insertUser(vararg users: User) {

        mLocationDataSource.insertUser(*users)
    }

    override fun updateUser(vararg users: User) {

        mLocationDataSource.updateUser(*users)
    }

    override fun deletUser(user: User) {


        mLocationDataSource.deletUser(user)
    }

    override fun deleteAllUsers() {

        mLocationDataSource.deleteAllUsers()
    }

    companion object {
        private var mInstance:UserRepository?=null

        fun getInstance(mLocationDataSource: IUserDataSource):UserRepository{

            if (mInstance==null)

                mInstance=UserRepository(mLocationDataSource)

            return mInstance!!

        }
    }
}