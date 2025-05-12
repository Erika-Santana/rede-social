package br.edu.ifsp.dmo1.redesocial.data.entity

import android.graphics.Bitmap

class Post (private val descricao: String, private val foto: Bitmap?,  private val cidade: String = ""){
    fun getDescricao() : String{
        return descricao
    }
    fun getFoto() : Bitmap? {
        return foto
    }
    fun getCidade(): String{
        return cidade
    }

}
