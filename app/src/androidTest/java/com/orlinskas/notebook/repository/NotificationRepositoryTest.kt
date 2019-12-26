package com.orlinskas.notebook.repository

import android.util.Log
import com.orlinskas.notebook.entity.Notification
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationRepositoryTest {
//    private val repository = NotificationRepository()
//    private var job: Job = Job()
//    private var scope = CoroutineScope(Dispatchers.Default + job)
//    private val AYOUSHURE = false
//
//    @Test
//    fun runTestRepository() {
//        runBlocking(scope.coroutineContext) {
//            val notification = randomNotification()
//            var isInsert = false
//
//            repository.insert(notification)
//            var notifications = repository.findAll()
//            if(notifications.contains(notification)) isInsert = true
//
//            assertTrue("Insert fail" , isInsert)
//
//            var isDelete = false
//
//            repository.delete(notification)
//            notifications = repository.findAll()
//
//            if(!notifications.contains(notification)) {
//                isDelete = true
//            }
//            for (note in notifications) {
//                if(note.is_deleted) {
//                    isDelete = true
//                    Log.v("Remote service fail ", "Local deleted")
//                }
//            }
//
//            assertTrue("Delete fail" , isDelete)
//        }
//    }
//
//    private fun randomNotification() : Notification {
//        val id = Random().nextInt(2000000000 - 10 + 1) + 10
//        val bodyText = "Test"
//        val createDateMillis = System.currentTimeMillis() + (60 * 60 * 1000)
//        val startDateMillis = System.currentTimeMillis() + 2 *(60 * 60 * 1000)
//        return Notification(id, createDateMillis, startDateMillis, bodyText)
//    }
//
//    fun clearRepo() = runBlocking(scope.coroutineContext) {
//        val notifications = repository.findAll()
//        var count = 0
//
//        if(notifications.isEmpty()) {
//            Log.v("Очистка репозитория -- ", "Репозиторий пуст")
//        }
//
//        if(AYOUSHURE) {
//            for(notification in notifications) {
//                repository.delete(notification)
//                count++
//            }
//            Log.v("Очистка репозитория -- ", "Удалено -- $count -- объектов")
//        }
//        else {
//            Log.v("Очистка репозитория -- ", "Нужно разрешить")
//        }
//    }
}

