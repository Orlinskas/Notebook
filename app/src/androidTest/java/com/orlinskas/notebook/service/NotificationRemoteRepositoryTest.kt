package com.orlinskas.notebook.service

import android.util.Log
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import junit.framework.TestCase
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NotificationRemoteRepositoryTest {
    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.Default + job)
    private val api = NotificationRemoteRepository(ApiFactory.notificationApi)

    @Before
    fun setUp() {

    }

    @Test
    fun findAll() = runBlocking {
        Log.v(javaClass.name, "Начало тестирования findAll функции")
        withContext(scope.coroutineContext) {
            val apiList = api.findAll()

            Log.v(javaClass.name, "Найденно -- ${apiList?.size} -- объектов на сервере")

            if(apiList == null) {
                fail("Не найденно объектов")
            }

            if(apiList!!.isEmpty()) {
                fail("Нет данных, emptyApi")
            }
        }
    }

    @Test
    fun findActual() {
    }

    @Test
    fun insertAll() {
    }

    @Test
    fun delete() {
    }

    @Test
    fun update() {
    }
}