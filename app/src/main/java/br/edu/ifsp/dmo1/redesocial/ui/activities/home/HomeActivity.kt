package br.edu.ifsp.dmo1.redesocial.ui.activities.home
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo1.redesocial.data.entity.Post
import br.edu.ifsp.dmo1.redesocial.data.entity.repository.PostDatabase
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityHomeBinding
import br.edu.ifsp.dmo1.redesocial.ui.activities.createPost.CreatePostActivity
import br.edu.ifsp.dmo1.redesocial.ui.picture_code.Base64Converter
import br.edu.ifsp.dmo1.redesocial.ui.adapter.PostAdapter
import br.edu.ifsp.dmo1.redesocial.ui.activities.login.LoginActivity
import br.edu.ifsp.dmo1.redesocial.ui.activities.profile.ProfileActivity
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class HomeActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityHomeBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var adapter: PostAdapter
    private lateinit var repository: PostDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        repository = PostDatabase()

        configAdapter()
        repository.getAllPosts()
        reloadPosts(repository.newPosts)
        reloadInfos()
        setOnClickListener()
    }


    private fun configAdapter() {
        val posts = mutableListOf<Post>()
        adapter = PostAdapter(posts)
        viewBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerView.adapter = adapter

    }

    private fun reloadInfos() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val email = firebaseAuth.currentUser!!.email.toString()
        db.collection("usuarios").document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    val imageString = document.data!!["fotoPerfil"].toString()
                    val bitmap = Base64Converter.stringToBitmap(imageString)
                    Glide.with(this)
                        .load(bitmap)
                        .circleCrop()
                        .into(viewBinding.imageHome)

                    viewBinding.nomePublico.text = document.data!!["username"].toString()
                    viewBinding.name.text = document.data!!["nomeCompleto"].toString()
                }
            }
    }

    private fun reloadPosts(posts: List<Post>) {
        adapter.updatePostsLimit(posts)
    }

    private fun setOnClickListener() {

        viewBinding.sair.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        viewBinding.button.setOnClickListener {
            repository.getAllPosts()
            reloadPosts(repository.newPosts)
        }

        viewBinding.criarPost.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }

        viewBinding.editPerfil.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("editar", true)
            startActivity(intent)
        }

        viewBinding.buttonPesquisar.setOnClickListener {
            val city = viewBinding.editSearch.text.toString()
            if (!city.isEmpty() || !city.isBlank()) {
                repository.findByCity(city)
                reloadPosts(repository.newPosts)

            }

        }
    }
}

