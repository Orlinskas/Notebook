package com.orlinskas.notebook.repository

import androidx.test.platform.app.InstrumentationRegistry
import com.orlinskas.notebook.date.DateCalculator
import com.orlinskas.notebook.date.DateCurrent
import com.orlinskas.notebook.date.DateFormater
import com.orlinskas.notebook.entity.Notification
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class NotificationRepositoryTest {
    private val repo = NotificationRepository()
    private val context = InstrumentationRegistry.getInstrumentation().context
    private val mockNotification = Mockito.mock(Notification::class.java)

    @Before
    fun setUp() {
        val currentDate = DateCurrent.getLine(DateFormater.YYYY_MM_DD_HH_MM)
        val datePlusHour = DateCalculator().plusHours(currentDate, 1, DateFormater.YYYY_MM_DD_HH_MM)
        //val notification = NotificationBuilder(context).build("Test", datePlusHour)
        Mockito.`when`(mockNotification .id).thenReturn(Random().nextInt(2000000000 - 10 + 1) + 10)
        Mockito.`when`(mockNotification .bodyText).thenReturn("Test")
        Mockito.`when`(mockNotification .createDateMillis).thenReturn(System.currentTimeMillis() + (60 * 60 * 1000))
        Mockito.`when`(mockNotification .startDateMillis).thenReturn(System.currentTimeMillis() + 2 *(60 * 60 * 1000))
        Mockito.`when`(mockNotification .deleted_at).thenReturn(null)
        Mockito.`when`(mockNotification .isSynchronized).thenReturn(true)
    }

    @Test
    fun findAll() {
        var isContain = false
        repo.insertAll(mockNotification)

        for(notification in repo.findAll().get()) {
            if(notification.id == mockNotification.id) {
                isContain = true
            }
        }

        assertTrue("Is not contain", isContain)
    }

    @Test
    fun findActual() {

    }

    @Test
    fun insertAll() {

    }

    @Test
    fun delete() {
        repo.insertAll(mockNotification)

        for (note in repo.findAll().get()) {
            if (note.bodyText == mockNotification.bodyText) {
                repo.delete(note)
            }
        }

        for (note in repo.findAll().get()) {
            if (note.bodyText == mockNotification.bodyText) {
                fail()
            }
        }

        assertTrue(true)
    }

    @Test
    fun update() {

    }
}