package br.edu.ifsp.dmo1.redesocial.data.entity.repository

import br.edu.ifsp.dmo1.redesocial.data.entity.Post

interface CallbackPost {
    fun callbackAll(posts: List<Post>)
    fun callbackByCity(posts: List<Post>)
}