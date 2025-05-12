package br.edu.ifsp.dmo1.redesocial.data.entity.repository


import android.util.Log
import br.edu.ifsp.dmo1.redesocial.data.entity.Post
import br.edu.ifsp.dmo1.redesocial.ui.picture_code.Base64Converter
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore


class PostDatabase {

    var newPosts = mutableListOf<Post>()

    var lastDocument :DocumentSnapshot? = null

    fun getAllPosts(){

        val db = Firebase.firestore

        if (lastDocument != null){
            getAllLimit()
        }

        db.collection("posts")
            .limit(5)
            .orderBy("cidade", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {  task ->
                    val documents = task
                    val novosPosts = mutableListOf<Post>()

                    for (document in documents.documents) {
                        val imageString = document.data?.get("fotoPostString").toString()
                        val bitmap = Base64Converter.stringToBitmap(imageString)
                        val descricao = document.data?.get("textoPost").toString()
                        var local = document.data?.get("cidade").toString()
                        local += " " + document.data?.get("estado") + " " + document.data?.get("pais")

                        val post = Post(descricao, bitmap, local)
                        novosPosts.add(post)
                    }
                    lastDocument = documents.documents.last()
                    newPosts = novosPosts
                    Log.v("Lista: ", newPosts.toString())
                }


    }

    fun getAllLimit(){

        val db = Firebase.firestore
        db.collection("posts")
            .limit(5)

            .orderBy("cidade", Query.Direction.DESCENDING)
            .startAfter(lastDocument)
            .get()
            .addOnSuccessListener {  task ->
                val documents = task
                val novosPosts = mutableListOf<Post>()

                for (document in documents.documents) {
                    val imageString = document.data?.get("fotoPostString").toString()
                    val bitmap = Base64Converter.stringToBitmap(imageString)
                    val descricao = document.data?.get("textoPost").toString()
                    var local = document.data?.get("cidade").toString()
                    local += " " + document.data?.get("estado") + " " + document.data?.get("pais")

                    val post = Post(descricao, bitmap, local)
                    novosPosts.add(post)
                }
                lastDocument = documents.documents.last()
                newPosts = novosPosts
                Log.v("Lista: ", newPosts.toString())
            }


    }

     fun findByCity(city: String) {

         val db = Firebase.firestore

         db.collection("posts")
             .whereEqualTo("cidade", city)
             .get()
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     val documents = task.result
                     val novosPosts = mutableListOf<Post>()

                     for (document in documents.documents) {
                         val imageString = document.data?.get("fotoPostString").toString()
                         val bitmap = Base64Converter.stringToBitmap(imageString)
                         val descricao = document.data?.get("textoPost").toString()
                         var local = document.data?.get("cidade").toString()
                         local += " " + document.data?.get("estado") + " " + document.data?.get("pais")

                         novosPosts.add(Post(descricao, bitmap, local))
                     }

                    newPosts = novosPosts

                 }
             }
     }

}