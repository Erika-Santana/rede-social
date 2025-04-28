package br.edu.ifsp.dmo1.redesocial.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityProfileBinding
import br.edu.ifsp.dmo1.redesocial.ui.picture_code.Base64Converter
import br.edu.ifsp.dmo1.redesocial.ui.home.HomeActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var viewBiding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBiding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(viewBiding.root)

        setListeners()
    }

    private fun setListeners() {

        val galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) {
                uri ->
            if (uri != null) {
                viewBiding.imagemProfile.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
            }
        }

        viewBiding.alterarFoto.setOnClickListener {
               galeria.launch(
                   PickVisualMediaRequest(
                   ActivityResultContracts.PickVisualMedia.ImageOnly)
               )
           }

        viewBiding.salvar.setOnClickListener{
            val firebaseAuth = FirebaseAuth.getInstance()

            if (firebaseAuth.currentUser != null){
                val email = firebaseAuth.currentUser!!.email.toString()
                val username = viewBiding.nomePublico.text.toString()
                val nomeCompleto = viewBiding.nomeCompleto.text.toString()
                val fotoPerfilString = Base64Converter.drawableToString(viewBiding.imagemProfile.
                drawable)
                val db = Firebase.firestore

                val dados = hashMapOf(
                    "nomeCompleto" to nomeCompleto,
                    "username" to username,
                    "fotoPerfil" to fotoPerfilString
                )
                db.collection("usuarios").document(email)
                    .set(dados)
                    .addOnSuccessListener {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }

        }

    }
}