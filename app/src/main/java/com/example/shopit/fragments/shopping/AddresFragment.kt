package com.example.shopit.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.shopit.data.Address
import com.example.shopit.data.CartProduct
import com.example.shopit.databinding.FragmentAddressBinding
import com.example.shopit.utils.Resource
import com.example.shopit.viewmodel.AddresViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddresFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    val viewModel by viewModels<AddresViewModel>()
    val db = FirebaseFirestore.getInstance()

    private var selectedItems: List<CartProduct> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddres.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE

                    }
                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.error.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedItems = arguments?.getParcelableArrayList<CartProduct>("selected_items") ?: emptyList()

        binding.apply {
            placeorder.setOnClickListener {
                // Get the input data from the address fields
                val titleAddress = edAddressTitle.text.toString()
                val name = edFullName.text.toString()
                val city = edCity.text.toString()
                val phoneNumber = edPhone.text.toString()
                val street = edStreet.text.toString()


                if (titleAddress.isEmpty() || name.isEmpty() || city.isEmpty() || phoneNumber.isEmpty() || street.isEmpty()) {

                    Toast.makeText(requireContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show()
                } else {

                    val address = Address(titleAddress, name, city, phoneNumber, street)
                    viewModel.addAddress(address)

                    showConfirmationDialog(address)
                }
            }
        }
    }

    private fun saveOrderToFirestore(address: Address, selectedItems: List<CartProduct>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = currentUser.uid
        val ordersRef = db.collection("orders")


        val orderData = hashMapOf(
            "userId" to userId,
            "address" to hashMapOf(
                "title" to address.addresTitle,
                "name" to address.name,
                "city" to address.city,
                "phone" to address.phone,
                "street" to address.street
            ),
            "products" to selectedItems.map { cartProduct ->
                mapOf(
                    "productId" to cartProduct.product.id,
                    "name" to cartProduct.product.name,
                    "price" to cartProduct.product.price,
                    "quantity" to cartProduct.quantiy,
                    "images" to cartProduct.product.images
                )
            },
            "orderStatus" to "Pending",
            "orderDate" to System.currentTimeMillis()
        )


        ordersRef.add(orderData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Order placed successfully", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to place order: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showConfirmationDialog(address: Address) {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items")
            setMessage("Do you want to order your cart items?")
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton("Yes") { dialog, _ ->
                if (selectedItems.isNotEmpty()) {
                    saveOrderToFirestore(address, selectedItems)
                    dialog.dismiss() // Ensure dialog is dismissed properly
                } else {
                    Toast.makeText(requireContext(), "No items selected in the cart", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val dialog = alertDialog.create()
        dialog.setOnDismissListener {
            // Ensure we do not encounter lifecycle issues by navigating after dialog closes
            if (selectedItems.isNotEmpty()) {
                findNavController().navigateUp()
            }
        }
        dialog.show()
    }
}
