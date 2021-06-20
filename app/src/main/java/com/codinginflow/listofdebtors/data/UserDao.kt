package com.codinginflow.listofdebtors.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.codinginflow.listofdebtors.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM debtors_table")
    suspend fun deleteAllUsers()

    @Query("SELECT * FROM debtors_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>
}