package br.edu.ifsp.dmo1.redesocial.ui.activities.createPost

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityCreatePostBinding
import br.edu.ifsp.dmo1.redesocial.ui.activities.home.HomeActivity
import br.edu.ifsp.dmo1.redesocial.ui.activities.location.LocalizacaoHelper
import br.edu.ifsp.dmo1.redesocial.ui.picture_code.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class CreatePostActivity : AppCompatActivity(), LocalizacaoHelper.Callback {

    private lateinit var viewBinding: ActivityCreatePostBinding
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var endereco: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        val galeria = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()) {
                uri ->
            if (uri != null) {
                viewBinding.imagemProfile.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_LONG).show()
            }
        }

        viewBinding.alterarFoto.setOnClickListener {
            galeria.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        viewBinding.botaoPostar.setOnClickListener{

            val firebaseAuth = FirebaseAuth.getInstance()
            val usuario = firebaseAuth.currentUser?.email

            if (usuario != null){
                val textoPost = viewBinding.postText.text.toString()
                val fotoPostString = Base64Converter.drawableToString(viewBinding.imagemProfile.
                drawable)

                val db = Firebase.firestore

                if (endereco != null){

                    val dados = hashMapOf(
                        "textoPost" to textoPost,
                        "fotoPostString" to fotoPostString,
                        "cidade" to (endereco?.subAdminArea ?: ""),
                        "estado" to (endereco?.adminArea ?: ""),
                        "pais" to (endereco?.countryName ?: ""))

                        db.collection("posts")
                        .add(dados)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                }else{
                    val dados = hashMapOf(
                        "textoPost" to textoPost,
                        "fotoPostString" to fotoPostString,
                        "cidade" to (endereco?.subAdminArea ?: ""),
                        "estado" to (endereco?.adminArea ?: ""),
                        "pais" to (endereco?.countryName ?: ""))

                    db.collection("posts")
                        .add(dados)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                }

            }
        }

        viewBinding.location.setOnClickListener{
            solicitarLocalizacao()
        }

    }

    private fun solicitarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
        else {
            val localizacaoHelper = LocalizacaoHelper(applicationContext)
            localizacaoHelper.obterLocalizacaoAtual(this)
        }
    }

    override fun onLocalizacaoRecebida(endereco: Address, latitude: Double, longitude: Double) {
        runOnUiThread {
            this.endereco = endereco

           var infos = endereco.locality
            infos += "\n" + endereco.subLocality
            infos += "\n" + endereco.adminArea
            infos += "\n" + endereco.subAdminArea
            infos += "\n" + endereco.postalCode
            infos += "\n" + endereco.countryName + ", " + endereco.countryCode
            infos += "\n" + endereco.getAddressLine(0)

        }
    }

    override fun onErro(mensagem: String) {
        System.out.println(mensagem)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions,
            grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            solicitarLocalizacao()
        } else {
            Toast.makeText(this, "Permissão de localização negada",
                Toast.LENGTH_SHORT).show()
        }
    }
}