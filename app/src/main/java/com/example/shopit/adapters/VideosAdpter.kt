package com.example.shopit.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.data.VideosModel
import com.example.shopit.databinding.VideoItemBinding
import com.example.shopit.fragments.shopping.productdetailfragment
import com.google.firebase.storage.FirebaseStorage

class VideosAdpter(
    private val context: Context,
    private val videoList: List<VideosModel>
) : RecyclerView.Adapter<VideosAdpter.VideoAdapterViewHolder>() {

    inner class VideoAdapterViewHolder(val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var player: ExoPlayer? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapterViewHolder {
        val binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapterViewHolder, position: Int) {
        val item = videoList[position]
        with(holder.binding) {
            productName.text = item.productName
            price.text = "Price: ${item.price}"

            val player = ExoPlayer.Builder(context).build()
            holder.player = player
            playerView.player = player

            val firstVideoPath = item.videos.firstOrNull()
            if (firstVideoPath != null) {
                if (firstVideoPath.startsWith("http")) {
                    // Direct URL
                    val mediaItem = MediaItem.fromUri(firstVideoPath)
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.playWhenReady = true
                } else {

                    val storageReference = FirebaseStorage.getInstance().getReference(firstVideoPath)
                    storageReference.downloadUrl.addOnSuccessListener { uri ->

                        val mediaItem = MediaItem.fromUri(uri)
                        player.setMediaItem(mediaItem)
                        player.prepare()
                        player.playWhenReady = true
                    }.addOnFailureListener { exception ->

                        Toast.makeText(context, "Failed to load video", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.w("VideosAdapter", "No video path found in model")
            }

            buyNow.setOnClickListener {
                try {
                    val intent = Intent(context, productdetailfragment::class.java)
                    intent.putExtra("productId", item.productId)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Unable to open product", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun getItemCount(): Int = videoList.size

    override fun onViewDetachedFromWindow(holder: VideoAdapterViewHolder) {
        holder.player?.release()
        holder.player = null
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: VideoAdapterViewHolder) {
        holder.player?.release()
        holder.player = null
        super.onViewRecycled(holder)
    }
}
