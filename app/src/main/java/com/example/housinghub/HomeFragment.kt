package com.example.housinghub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.housinghub.SharedViewModel.Viewmodel.SharedViewModel
import com.example.housinghub.databinding.FragmentHomeBinding
import com.example.housinghub.model.Property
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment(), BookmarkClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var propertyAdapter: PropertyAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        propertyAdapter = PropertyAdapter(sharedViewModel, this)
        binding.propertyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.propertyRecyclerView.adapter = propertyAdapter

        fetchProperties()
    }

    override fun onResume() {
        super.onResume()
        fetchProperties()
    }

    private fun fetchProperties() {
        // Get all owners' properties from the Available collection
        firestore.collection("Properties")
            .get()
            .addOnSuccessListener { ownerDocs ->
                val allProperties = mutableListOf<Property>()
                var completedQueries = 0
                val totalQueries = ownerDocs.size()

                if (totalQueries == 0) {
                    propertyAdapter.updateData(emptyList())
                    return@addOnSuccessListener
                }

                // For each owner, fetch only Available properties
                ownerDocs.forEach { ownerDoc ->
                    val ownerEmail = ownerDoc.id
                    
                    // Only fetch Available properties for tenant view
                    firestore.collection("Properties")
                        .document(ownerEmail)
                        .collection("Available")
                        .get()
                        .addOnSuccessListener { availableProps ->
                            val properties = availableProps.mapNotNull { 
                                it.toObject(Property::class.java)?.apply {
                                    id = it.id
                                    ownerId = ownerEmail
                                    isAvailable = true
                                }
                            }
                            allProperties.addAll(properties)
                            
                            completedQueries++
                            if (completedQueries == totalQueries) {
                                propertyAdapter.updateData(allProperties.sortedByDescending { it.timestamp })
                            }
                        }
                        .addOnFailureListener { e ->
                            completedQueries++
                            if (completedQueries == totalQueries) {
                                propertyAdapter.updateData(allProperties.sortedByDescending { it.timestamp })
                            }
                        }
                }
            }
            .addOnFailureListener { e ->
                propertyAdapter.updateData(emptyList())
            }
    }

    override fun onBookmarkClicked(property: Property, position: Int) {
        property.isBookmarked = !property.isBookmarked
        sharedViewModel.toggleBookmark(property)
        propertyAdapter.notifyItemChanged(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
