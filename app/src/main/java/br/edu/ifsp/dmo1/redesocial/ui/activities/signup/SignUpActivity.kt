package br.edu.ifsp.dmo1.redesocial.ui.activities.signup

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.dmo1.redesocial.R
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityLoginBinding
import br.edu.ifsp.dmo1.redesocial.databinding.ActivitySignUpBinding
import br.edu.ifsp.dmo1.redesocial.ui.activities.home.HomeActivity
import br.edu.ifsp.dmo1.redesocial.ui.activities.profile.ProfileActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivitySignUpBinding
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setListerners()
    }

    private fun setListerners() {

        viewBinding.botao.setOnClickListener{
            val login = viewBinding.login.text.toString()
            val senha = viewBinding.digiteSenha.text.toString()
            val confirmaSenha = viewBinding.confirmeSenha.text.toString()

            if (login.isEmpty() || senha.isEmpty()){
                Toast.makeText(this, "Por favor preencha os campos!", Toast.LENGTH_SHORT).show()
            }else{
                if (senha.equals(confirmaSenha)){
                    firebaseAuth
                        .createUserWithEmailAndPassword(login, senha)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(Intent(this, ProfileActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}