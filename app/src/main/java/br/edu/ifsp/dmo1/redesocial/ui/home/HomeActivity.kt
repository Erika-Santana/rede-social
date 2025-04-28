package br.edu.ifsp.dmo1.redesocial.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo1.redesocial.data.entity.Post
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityHomeBinding
import br.edu.ifsp.dmo1.redesocial.ui.picture_code.Base64Converter
import br.edu.ifsp.dmo1.redesocial.ui.adapter.PostAdapter
import br.edu.ifsp.dmo1.redesocial.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class HomeActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityHomeBinding
    private val firebaseAuth =  FirebaseAuth.getInstance()
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        val firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val email = firebaseAuth.currentUser!!.email.toString()
        db.collection("usuarios").document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    val imageString = document.data!!["fotoPerfil"].toString()
                    val bitmap = Base64Converter.stringToBitmap(imageString)
                    viewBinding.imageHome.setImageBitmap(bitmap)
                    viewBinding.nomePublico.text = document.data!!["username"].toString()
                    viewBinding.name.text =
                        document.data!!["nomeCompleto"].toString()
                }
            }

        setOnClickListener()
    }

    private fun setOnClickListener() {
        viewBinding.logoff.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        viewBinding.button.setOnClickListener{
            val db = Firebase.firestore
            db.collection("posts").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        val document = task.result
                        var posts = ArrayList<Post>()
                        for (document in document.documents) {
                            val imageString = document.data!!["imageString"].toString()
                            val bitmap = Base64Converter.stringToBitmap(imageString)
                            val descricao = document.data!!["descricao"].toString()
                            posts.add(Post(descricao, bitmap))
                        }
                        adapter = PostAdapter(posts.toTypedArray())
                        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
                        viewBinding.recyclerView.adapter = adapter
                    }
                }
        }
        }

    }
