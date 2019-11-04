package com.techhouseandroid.kotlinroomdb

import android.app.AlertDialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.text.method.DialerKeyListener
import android.view.ContextMenu

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.techhouseandroid.kotlinroomdb.Database.UserRepository

import com.techhouseandroid.kotlinroomdb.Local.UserDataSource
import com.techhouseandroid.kotlinroomdb.Local.UserDatabase
import com.techhouseandroid.kotlinroomdb.Model.User
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter:ArrayAdapter<*>
    var userList:MutableList<User> = ArrayList()
    private var compositeDisposable:CompositeDisposable?=null
    private var userRepository:UserRepository?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compositeDisposable= CompositeDisposable()

        adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,userList)

        registerForContextMenu(list_view)

        list_view!!.adapter = adapter

        val userDatabase=UserDatabase.getInstance(this)

        userRepository= UserRepository.getInstance(UserDataSource.getInstance(userDatabase.userDAO()))



        loadData()

        fab_btn.setOnClickListener({

            val disposable =Observable.create(ObservableOnSubscribe<Any> {e->

                val user=User()

              //  user.name="EDMt"
              //  user.email=UUID.randomUUID().toString()+"@gmial.com"

                user.name=us_name.text.toString()
                user.email=us_email.text.toString()



                userRepository!!.insertUser(user)




                e.onComplete() })

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(Consumer{

                    },
                            Consumer {
                                throwable->Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                            },
                            Action { loadData() }

                    )

            compositeDisposable!!.addAll(disposable)

        })
    }

    private fun loadData() {
        val disposable=userRepository!!.allUsers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({users->onGetAllUserSuccess(users)})
                {

                    throwable->Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()



                }

        compositeDisposable!!.add(disposable)

    }

    private fun onGetAllUserSuccess(users: List<User>) {

        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){


            R.id.clear->deleteAllUsers()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val disposable =Observable.create(ObservableOnSubscribe<Any> {e->

            userRepository!!.deleteAllUsers()

            e.onComplete() })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer{

                },
                        Consumer {
                            throwable->Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                        },
                        Action { loadData() }

                        )

        compositeDisposable!!.addAll(disposable)

    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val  info=menuInfo as AdapterView.AdapterContextMenuInfo

        menu.setHeaderTitle("Select action:")

        menu.add(Menu.NONE,0,Menu.NONE,"Update")

        menu.add(Menu.NONE,1,Menu.NONE,"Delete")
    }


    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info=item!!.menuInfo as AdapterView.AdapterContextMenuInfo

        val user=userList[info.position]
        when (item!!.itemId){

            0
            ->
            {
                val edtName=EditText(this@MainActivity)

                edtName.setText(user.name)

                edtName.hint= "Enter your name"


                AlertDialog.Builder(this@MainActivity)

                        .setTitle("Edit")
                        .setMessage("Edit User Name")
                        .setView(edtName)
                        .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener { dialog, which ->


                            if (TextUtils.isEmpty(edtName.text.toString()))

                                return@OnClickListener

                            else{

                                user.name=edtName.text.toString()
                                updateUser(user)
                            }


                        }).setNegativeButton(android.R.string.cancel){
                    
                    dialog, which ->  dialog.dismiss()
                }.create()
                        .show()
            }

            1
            ->
            {
                AlertDialog.Builder(this@MainActivity)

                        .setMessage("Do you want to Delete?" +user.name)

                        .setPositiveButton(android.R.string.ok,DialogInterface.OnClickListener { dialog, which ->

                                 deleteUser(user)
                           

                        }).setNegativeButton(android.R.string.cancel){

                    dialog, which ->  dialog.dismiss()
                }.create()
                        .show()

            }
        }


        return true

    }

    private fun deleteUser(user: User) {


        val disposable =Observable.create(ObservableOnSubscribe<Any> {e->

            userRepository!!.deletUser(user)

            e.onComplete() })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer{

                },
                        Consumer {
                            throwable->Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                        },
                        Action { loadData() }

                )

        compositeDisposable!!.addAll(disposable)



    }

    private fun updateUser(user: User) {

        val disposable =Observable.create(ObservableOnSubscribe<Any> {e->

            userRepository!!.updateUser(user)

            e.onComplete() })

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer{

                },
                        Consumer {
                            throwable->Toast.makeText(this@MainActivity,""+throwable.message,Toast.LENGTH_SHORT).show()
                        },
                        Action { loadData() }

                )

        compositeDisposable!!.addAll(disposable)


    }


    override fun onDestroy() {

        compositeDisposable!!.clear()
        super.onDestroy()
    }

}
