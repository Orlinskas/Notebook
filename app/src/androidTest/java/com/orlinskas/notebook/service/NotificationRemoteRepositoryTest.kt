package com.orlinskas.notebook.service

import android.util.Log
import com.orlinskas.notebook.MVVM.model.Notification
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationRemoteRepositoryTest {
    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.Default + job)
    private val api = ApiFactory.notificationApi
    private val notification = buildRandomTestNote()

    private fun buildRandomTestNote() : Notification {
        val id = Random().nextInt(2000000000 - 10 + 1) + 10
        val bodyText = "TestRemote"
        val createDateMillis = System.currentTimeMillis() + (60 * 60 * 1000)
        val startDateMillis = System.currentTimeMillis() + 2 *(60 * 60 * 1000)
        return Notification(id, createDateMillis, startDateMillis, bodyText)
    }

    @Test
    fun findAll() = runBlocking {
        Log.v(javaClass.name, "Начало тестирования findAll функции")

        withContext(scope.coroutineContext) {
            val response = api.findAll()

            Log.v(javaClass.name, "Код ответа -- ${response.code} -- (if 200 == done))")
            Log.v(javaClass.name, "Найденно -- ${response.data.size} -- объектов на сервере")

            if(response.data == null) {
                fail("Нет данных")
            }

            assertTrue(javaClass.name, response.code == 200)
        }
    }

    @Test
    fun insertAll() = runBlocking {
        Log.v(javaClass.name, "Начало тестирования insertAll функции")

        withContext(scope.coroutineContext) {
            val response = api.add(notification)

            if(response.code != 201) {
                fail("Ошибка добавления")
            }

            Log.v(javaClass.name, "Попытка добавления -- ${response.data.id} -- объекта на сервер")
            Log.v(javaClass.name, "Код ответа -- ${response.code} -- объект -- ${response.data.id}")

            assertTrue(javaClass.name, response.code == 201)
        }
    }


    @Test
    fun delete() = runBlocking {
        Log.v(javaClass.name, "Начало тестирования delete функции")

        withContext(scope.coroutineContext) {
            val response = api.delete(notification.id)

            Log.v(javaClass.name, "Попытка удаления -- ${notification.id} -- объекта")
            Log.v(javaClass.name, "Код ответа -- ${response.code} -- (200 done), удален ${notification.id}")

            assertTrue(javaClass.name, response.code == 200)
        }
    }
}