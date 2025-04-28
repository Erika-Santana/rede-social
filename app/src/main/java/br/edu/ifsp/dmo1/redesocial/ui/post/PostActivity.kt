package br.edu.ifsp.dmo1.redesocial.ui.post

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo1.redesocial.databinding.ActivityPostBinding
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        solicitaJSON()
    }

    fun solicitaJSON(){
        val queue = Volley.newRequestQueue(this)
        val url = "http://192.168.16.1:8080/posts/1"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                binding.txtDescricao.text = response.getString("descricao")
                val queue = Volley.newRequestQueue(this)
                val urlImage = "http:// 192.168.16.1:8080/images/" +
                        response.getString("foto")
                val imageRequest = ImageRequest(urlImage,
                    { response ->
                        binding.imagePost.setImageBitmap(response)
                    },
                    0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565,
                    { error ->
                        error.printStackTrace()
                    })
                queue.add(imageRequest)
            },
            { error ->
                error.printStackTrace()
            }
        )
        queue.add(jsonRequest)
    }
}