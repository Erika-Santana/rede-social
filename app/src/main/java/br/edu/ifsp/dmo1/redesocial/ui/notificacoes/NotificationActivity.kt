package br.edu.ifsp.dmo1.redesocial.ui.notificacoes

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import br.edu.ifsp.dmo1.redesocial.R
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityNotificationBinding


class NotificationActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setListeners()
    }

    private fun setListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS), 0)
        }
        //O sistema de notificação do android funciona como canais, na qual cada notificação it has it own channel
        val canalID = "1"
        val notificacaoID = 1
        criarCanalNotificacao(canalID)
        viewBinding.buttonNotification.setOnClickListener{
            /*The builder is where we create the channel, passing the parameters such as the icon
            * The content title, the text and the priority
            * The variable manager is an instance as a manager class which will provide the possibility to
            * create the notification. In the createNotificationChannel it's similar but in that
            * situation we are creating the channel itself*/
            val builder = NotificationCompat.Builder(this, canalID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(viewBinding.titleNotification.text.toString())
                .setContentText(viewBinding.textContentNotification.text.toString())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            val manager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
            manager.notify(notificacaoID, builder.build())
        }
    }

    // This function create the channel, specifying its importance to android SO
    //You have to provide the basics informations such as name, description, importance level, create it with
    //Notification channel passing the parameters as CanalID, name, and importance level. I think it's not
    //obligate to specify the description but is recommended
    private fun criarCanalNotificacao(canalID: String) {
        val nome = "Notificações Gerais"
        val desc = "Categoria para notificações do aplicativo"
        val importancia = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(canalID, nome, importancia)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }


}