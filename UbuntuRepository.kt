package com.example.data.repository

import com.example.data.dao.UbuntuDao
import com.example.data.entity.SetupScript
import com.example.data.entity.UbuntuInstance
import kotlinx.coroutines.flow.Flow

class UbuntuRepository(private val dao: UbuntuDao) {
    val allInstances: Flow<List<UbuntuInstance>> = dao.getAllInstances()
    val allScripts: Flow<List<SetupScript>> = dao.getAllScripts()

    suspend fun getInstanceById(id: Int): UbuntuInstance? = dao.getInstanceById(id)

    suspend fun insertInstance(instance: UbuntuInstance): Long = dao.insertInstance(instance)

    suspend fun updateInstance(instance: UbuntuInstance) = dao.updateInstance(instance)

    suspend fun deleteInstance(instance: UbuntuInstance) = dao.deleteInstance(instance)

    suspend fun stopAllInstances() = dao.stopAllInstances()

    suspend fun updateScript(script: SetupScript) = dao.updateScript(script)
}
