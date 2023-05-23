package com.example.listricks.fragments

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listricks.R
import com.example.listricks.adapters.ListsAdapter
import com.example.listricks.providers.ListsProvider
import com.example.listricks.viewModels.ListsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListsGalleryFragment : Fragment(R.layout.lists_gallery_fragment) {
    private lateinit var listsViewModel: ListsViewModel
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listsViewModel = ViewModelProvider(this,
            ListsViewModel.Factory { ListsProvider() }).get()
        listsViewModel.loadLists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setupViews()
    }

    private fun View.setupViews() {
        setupAddListButton()
        setupRecycler()
        placeProgressBar()
    }

    private fun View.setupAddListButton() {
        val addButton: FloatingActionButton = findViewById(R.id.addListButton)
        addButton.setOnClickListener {
            navigateToAddList()
        }
    }

    private fun View.setupRecycler() {
        val recycleView: RecyclerView = findViewById(R.id.lists_recycle_view)
        recycleView.layoutManager = LinearLayoutManager(context)
        recycleView.adapter = ListsAdapter().apply {
            listsViewModel.listsLiveData.observe(viewLifecycleOwner) { lst ->
                if (lst != null) {
                    progressBar.isVisible = false
                    this.submitList(lst)
                }
            }
            setCardClickListener { onListPressed(it) }
        }
    }

    private fun View.placeProgressBar() {
        progressBar = findViewById(R.id.listsGalleryProgressBar)
        progressBar.isVisible = true
    }

    private fun onListPressed(listName: String) {
        listsViewModel.setCurList(listName)
        navigateToSelectedList()
    }


    private fun navigateToSelectedList() {
        val action =
            ListsGalleryFragmentDirections.actionListsGalleryFragmentToSingleListFragment()
        findNavController().navigate(action)
    }

    private fun navigateToAddList() {
        val action =
            ListsGalleryFragmentDirections.actionListsGalleryFragmentToAddListFragment()
        findNavController().navigate(action)
    }
}
