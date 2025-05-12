package br.edu.ifsp.dmo1.redesocial.data.entity.repository
import br.edu.ifsp.dmo1.redesocial.data.entity.Post
import br.edu.ifsp.dmo1.redesocial.ui.picture_code.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class PostDatabase {

    fun getAllPosts(callback: CallbackPost){

        val newPosts = mutableListOf<Post>()

        val db = Firebase.firestore
        db.collection("posts").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result
                    for (document in documents.documents) {
                        val imageString = document.data?.get("fotoPostString").toString()
                        val bitmap = Base64Converter.stringToBitmap(imageString)
                        val descricao = document.data?.get("textoPost").toString()
                        var local =  document.data?.get("cidade").toString()
                        if (local.isEmpty() || !(local == "")){
                            local + ", " + document.data?.get("estado") +", "+ document.data?.get("pais")
                        }else{
                            local = ""
                        }

                        newPosts.add(Post(descricao, bitmap, local))
                    }
                }
                callback.callbackAll(newPosts)
            }

    }

    fun findByCity(city: String, callback: CallbackPost){

         val newPosts = mutableListOf<Post>()

         val db = Firebase.firestore
         db.collection("posts").get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     val documents = task.result
                     for (document in documents.documents) {
                         val getCity = document.getString("cidade") ?: ""
                         if (getCity.equals(city, ignoreCase = true)) {
                             val imageString = document.data?.get("fotoPostString").toString()
                             val bitmap = Base64Converter.stringToBitmap(imageString)
                             val descricao = document.data?.get("textoPost").toString()
                             val local =  document.data?.get("cidade").toString()

                                 local + ", " + document.data?.get("estado") +", "+ document.data?.get("pais")

                             newPosts.add(Post(descricao, bitmap, local))
                         }
                     }
                     callback.callbackByCity(newPosts)
                 }
             }

     }
}