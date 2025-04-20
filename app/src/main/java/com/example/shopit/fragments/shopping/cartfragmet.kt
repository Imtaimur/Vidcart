package com.example.shopit.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopit.R
import com.example.shopit.adapters.CartProductAdapter
import com.example.shopit.data.CartProduct
import com.example.shopit.databinding.FragmentCartBinding
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.OrderViewModel
import com.example.shopit.viewmodel.cartViewmodel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class cartfragmet : Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val cartAdpter by lazy { CartProductAdapter() }
    private val cartViewmodell by activityViewModels<cartViewmodel>()
    private val orderViewModel by viewModels<OrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()
        binding.imageclose.setOnClickListener {
            findNavController().navigateUp()
        }

        lifecycleScope.launchWhenStarted {
            cartViewmodell.productlprice.collectLatest { price ->
                price?.let {
                    binding.tvprice.text = "RS $price"
                }
            }
        }

        cartAdpter.OnProductclick = {
            val b = Bundle().apply { putParcelable("product", it.product) }
            findNavController().navigate(R.id.action_cartfragmet_to_productdetailfragment2, b)
        }

        cartAdpter.Onminusclick = { cartProduct ->
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Delete it From Cart?")
                setMessage("Do You Want to Delete This Item From Cart")
                setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                setPositiveButton("Yes") { dialog, _ ->
                    cartViewmodell.deleteitemfromcart(cartProduct)
                    dialog.dismiss()
                }
            }
            alertDialog.create()
            alertDialog.show()
        }


        binding.ProceedtoPlaceOrder.setOnClickListener {
            val selectedItems: List<CartProduct> = cartAdpter.getSelectedItems()
            if (selectedItems.isEmpty()) {
                Snackbar.make(requireView(), "Please select at least one item", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val bundle = Bundle().apply {
                putParcelableArrayList("selected_items", ArrayList(selectedItems))
            }

            findNavController().navigate(R.id.action_cartfragmet_to_addresFragment, bundle)
        }

        lifecycleScope.launchWhenStarted {
            cartViewmodell.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> binding.progressbarcart.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.progressbarcart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {
                            showempty()
                            hideother()
                        } else {
                            cartAdpter.difer.submitList(it.data)
                            hideemptycart()
                            showother()
                        }
                    }
                    is Resource.Error -> {
                        binding.progressbarcart.visibility = View.INVISIBLE
                        Snackbar.make(requireContext(), view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideother() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalbox.visibility = View.GONE
            ProceedtoPlaceOrder.visibility = View.GONE
        }
    }

    private fun showother() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalbox.visibility = View.VISIBLE
            ProceedtoPlaceOrder.visibility = View.VISIBLE
        }
    }

    private fun hideemptycart() {
        binding.emptycart.visibility = View.INVISIBLE
    }

    private fun showempty() {
        binding.emptycart.visibility = View.VISIBLE
    }

    private fun setupCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdpter
        }
    }
}
