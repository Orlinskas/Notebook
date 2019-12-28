import android.app.Application
import com.orlinskas.notebook.di.DaggerNotificationComponent
import com.orlinskas.notebook.di.NotificationComponent
import com.orlinskas.notebook.di.NotificationModule

class App : Application() {

    lateinit var notificationComponent: NotificationComponent

    override fun onCreate() {
        super.onCreate()

        notificationComponent = buildComponent()
    }

    fun getComponent() : NotificationComponent = notificationComponent

    private fun buildComponent(): NotificationComponent {
        return DaggerNotificationComponent.builder()
                .notificationModule(NotificationModule())
                .build()
    }
}