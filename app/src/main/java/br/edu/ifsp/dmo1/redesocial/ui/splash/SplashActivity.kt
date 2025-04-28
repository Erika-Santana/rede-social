package br.edu.ifsp.dmo1.redesocial.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import br.edu.ifsp.dmo1.redesocial.R
import br.edu.ifsp.dmo1.redesocial.ui.home.HomeActivity
import br.edu.ifsp.dmo1.redesocial.ui.login.LoginActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animationLogo = findViewById<ImageView>(R.id.image_splash)

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000
        animationLogo.startAnimation(fadeIn)

        //O handler é uma classe que você utiliza para realizar alguma coisa por um periodo de tempo.
        //O lifecycleScope ele cria uma corrotina na qual vai estar atrelado com o ciclo de vida das
        //Activities. Diferente do Handler de antigamente, quando essa acitivity é cancelada antes de acabar
        //o tempo de delay, É destruida automaticamente. Dessa forma, evita erros.
        lifecycleScope.launch {
            delay(3000) // Espera 2 segundos
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }
    }
}