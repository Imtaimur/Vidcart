package com.example.shopit.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.shopit.adapters.VideosAdpter
import com.example.shopit.data.VideosModel
import com.example.shopit.databinding.FragmentVideosBinding
import com.google.firebase.firestore.FirebaseFirestore

class videosFragment : Fragment() {

    private lateinit var binding: FragmentVideosBinding
    private lateinit var videosAdapter: VideosAdpter
    private val videoList = mutableListOf<VideosModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.recyclerViewVideos.layoutManager = layoutManager
        binding.recyclerViewVideos.setHasFixedSize(true)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerViewVideos)


        videosAdapter = VideosAdpter(requireContext(), videoList)
        binding.recyclerViewVideos.adapter = videosAdapter

        fetchVideosFromFirestore()
    }

    private fun fetchVideosFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                videoList.clear()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val price = document.getLong("price")?.toInt() ?: 0
                    val videos = document.get("videos") as? List<String> ?: listOf()
                    val id = document.id

                    if (videos.isNotEmpty()) {
                        val model = VideosModel(
                            productId = id,
                            productName = name,
                            price = price,
                            videos = videos
                        )
                        videoList.add(model)
                    }
                }
                videosAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to load videos: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
