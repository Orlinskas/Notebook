package com.orlinskas.notebook.repository

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import com.orlinskas.notebook.entity.Notification
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationRepositoryTest {
    private val repo = NotificationRepository()
    private val context = InstrumentationRegistry.getInstrumentation().context
    //private var mockNotification : Notification = Mockito.mock(Notification::class.java)
    private var job: Job = Job()
    private var scope = CoroutineScope(Dispatchers.Default + job)

    private fun buildRandomTestNote() : Notification {
        val id = Random().nextInt(2000000000 - 10 + 1) + 10
        val bodyText = "Test"
        val createDateMillis = System.currentTimeMillis() + (60 * 60 * 1000)
        val startDateMillis = System.currentTimeMillis() + 2 *(60 * 60 * 1000)
        return Notification(id, createDateMillis, startDateMillis, bodyText)
    }

    @Before
    fun setUp() {
        //val currentDate = DateCurrent.getLine(DateFormater.YYYY_MM_DD_HH_MM)
        //val datePlusHour = DateCalculator().plusHours(currentDate, 1, DateFormater.YYYY_MM_DD_HH_MM)
        //val notification = NotificationBuilder(context).build("Test", datePlusHour)
        //Mockito.`when`(mockNotification .id).thenReturn(Random().nextInt(2000000000 - 10 + 1) + 10)
        //Mockito.`when`(mockNotification .bodyText).thenReturn("Test")
        //Mockito.`when`(mockNotification .createDateMillis).thenReturn(System.currentTimeMillis() + (60 * 60 * 1000))
        //Mockito.`when`(mockNotification .startDateMillis).thenReturn(System.currentTimeMillis() + 2 *(60 * 60 * 1000))
        //Mockito.`when`(mockNotification .deleted_at).thenReturn(null)
        //Mockito.`when`(mockNotification .isSynchronized).thenReturn(true)
    }

    @Test
    fun findAll() = runBlocking {
        val mockNotification = buildRandomTestNote()
        var isContain = false

        withContext(Dispatchers.Default) {
            try {
                repo.insertAll(mockNotification)
            } catch (e: Exception) {
                Log.e("Ошибка подключения", e.message.toString())
            }

            for(notification in repo.findAll()) {
                if(notification.id == mockNotification.id) {
                    isContain = true
                }
            }

            assertTrue("Is not contain", isContain)
        }
    }

    @Test
    fun findActual() {

    }

    @Test
    fun insertAll() {

    }

    @Test
    fun delete() = runBlocking {
        val mockNotification = buildRandomTestNote()
        var count = 0

        withContext(scope.coroutineContext) {
            repo.insertAll(mockNotification)

            for (note in repo.findAll()) {
                if (note.bodyText == mockNotification.bodyText) {
                    repo.delete(note)
                    count++
                }
            }

            Log.v(javaClass.name, "Удаленно $count объектов")

            for (note in repo.findAll()) {
                if (note.bodyText == mockNotification.bodyText) {
                    fail()
                }
            }

            assertTrue(true)
        }
    }

    @Test
    fun update() {

    }

    fun deleteTestNotes() {
        Log.v(javaClass.name, "Запущен метод удаления всех тестовых заметок")
    }
}