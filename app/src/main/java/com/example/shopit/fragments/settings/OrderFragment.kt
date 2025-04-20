package com.example.shopit.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.adapters.OrderAdapter
import com.example.shopit.databinding.FragmentOrdersBinding
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.OrdersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class OrderFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrdersViewModel by viewModels()
    private val orderAdapter by lazy { OrderAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOrderRecyclerView()
        observeOrders()
        binding.imageCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }


        viewModel.getAllOrders()
    }


    private fun setupOrderRecyclerView() {
        binding.rvAllOrders.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }

        orderAdapter.onClick = { order ->
            Toast.makeText(requireContext(), "Clicked on order: ${order.orderId}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeOrders() {
        lifecycleScope.launchWhenStarted {
            viewModel.allOrders.collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding.progressbarAllOrders.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        if (resource.data.isNullOrEmpty()) {
                            binding.tvEmptyOrders.visibility = View.VISIBLE
                        } else {
                            binding.tvEmptyOrders.visibility = View.GONE
                            orderAdapter.differ.submitList(resource.data)
                        }
                    }

                    is Resource.Error -> {
                        binding.progressbarAllOrders.visibility = View.GONE
                        Toast.makeText(requireContext(), resource.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    else -> Unit
                }
            }
        }
    }
}
