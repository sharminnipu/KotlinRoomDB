package com.techhouseandroid.kotlinroomdb.Local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.techhouseandroid.kotlinroomdb.Local.UserDatabase.Companion.DATABASE_VERSION
import com.techhouseandroid.kotlinroomdb.Model.User

/**
 * Created by Nipu on 11/3/2018.
 */

@Database(entities = arrayOf(User::class),version = DATABASE_VERSION)
        abstract class UserDatabase:RoomDatabase(){

    abstract fun userDAO():UserDAO

    companion object {
        const val DATABASE_VERSION=1

        val DATABASE_NAME="userDtabase"
        private var mInstance:UserDatabase?=null
        fun getInstance(context: Context):UserDatabase{
             if (mInstance==null)
                 mInstance=Room.databaseBuilder(context,UserDatabase::class.java, DATABASE_NAME)
                         .fallbackToDestructiveMigration()
                         .build()
            return mInstance!!


        }

    }
}