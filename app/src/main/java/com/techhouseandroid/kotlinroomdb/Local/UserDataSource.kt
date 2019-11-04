package com.techhouseandroid.kotlinroomdb.Local

import com.techhouseandroid.kotlinroomdb.Database.IUserDataSource
import com.techhouseandroid.kotlinroomdb.Database.UserRepository
import com.techhouseandroid.kotlinroomdb.Model.User
import io.reactivex.Flowable

/**
 * Created by Nipu on 11/4/2018.
 */
class UserDataSource(private val userDAO: UserDAO):IUserDataSource{

    override val allUsers: Flowable<List<User>>
        get() = userDAO.allusers


    override fun getUserById(UserId: Int): Flowable<User> {

        return userDAO.getUserById(UserId)


    }

    override fun insertUser(vararg users: User) {

        userDAO.insertUser(*users)

    }

    override fun updateUser(vararg users: User) {

        userDAO.updateUser(*users)
    }

    override fun deletUser(user: User) {

        userDAO.deleteUser(user)


    }

    override fun deleteAllUsers() {
        userDAO.deleteAllUser()


    }

    companion object {
        private var mInstance: UserDataSource? = null

        fun getInstance(userDAO: UserDAO): UserDataSource {

            if (mInstance == null)

                mInstance = UserDataSource(userDAO)

            return mInstance!!

        }

    }


}