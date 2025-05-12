package br.edu.ifsp.dmo1.redesocial.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo1.redesocial.R
import br.edu.ifsp.dmo1.redesocial.data.entity.Post
class PostAdapter(private val posts: MutableList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPost: ImageView = view.findViewById(R.id.imagem_card)
        val txtDescricao: TextView = view.findViewById(R.id.descricao)
        val txtCidade: TextView = view.findViewById(R.id.text_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtDescricao.text = posts[position].getDescricao()
        holder.imgPost.setImageBitmap(posts[position].getFoto())
        holder.txtCidade.text = posts[position].getCidade()
    }

    fun updatePosts(newPosts: List<Post>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }

    fun updatePostsLimit(newPosts: List<Post>) {
        var startPosition = newPosts.size
        posts.addAll(newPosts)
        notifyItemRangeInserted(startPosition, newPosts.size)
    }
}
