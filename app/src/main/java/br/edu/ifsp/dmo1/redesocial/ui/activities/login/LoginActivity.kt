package br.edu.ifsp.dmo1.redesocial.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityLoginBinding
import br.edu.ifsp.dmo1.redesocial.ui.activities.home.HomeActivity
import br.edu.ifsp.dmo1.redesocial.ui.activities.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityLoginBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (firebaseAuth.currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        setClickListeners()
    }

    private fun setClickListeners() {
        viewBinding.buttonLogar.setOnClickListener{
            val email = viewBinding.login.text.toString()
            val password = viewBinding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Insira os valores!", Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth
                    .signInWithEmailAndPassword(email, password)
                    //Método task aqui é sobre o método que vai autenticar os valores passados pelo usuário (signInWithEmailAndPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Dados inválidos!", Toast.LENGTH_LONG).show()
                        }
                    }
            }

        }
        viewBinding.buttonCadastrar.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
}