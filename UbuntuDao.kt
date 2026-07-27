package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.SetupScript
import com.example.data.entity.UbuntuInstance
import kotlinx.coroutines.flow.Flow

@Dao
interface UbuntuDao {
    @Query("SELECT * FROM ubuntu_instances ORDER BY createdTimestamp DESC")
    fun getAllInstances(): Flow<List<UbuntuInstance>>

    @Query("SELECT * FROM ubuntu_instances WHERE id = :id LIMIT 1")
    suspend fun getInstanceById(id: Int): UbuntuInstance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstance(instance: UbuntuInstance): Long

    @Update
    suspend fun updateInstance(instance: UbuntuInstance)

    @Delete
    suspend fun deleteInstance(instance: UbuntuInstance)

    @Query("UPDATE ubuntu_instances SET status = 'STOPPED'")
    suspend fun stopAllInstances()

    @Query("SELECT * FROM setup_scripts ORDER BY id ASC")
    fun getAllScripts(): Flow<List<SetupScript>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScript(script: SetupScript)

    @Update
    suspend fun updateScript(script: SetupScript)
}
