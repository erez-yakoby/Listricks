package com.example.listricks.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.listricks.R
import com.example.listricks.providers.ListsProvider
import com.example.listricks.viewModels.ListsViewModel
import com.google.android.material.snackbar.Snackbar


class AddListFragment : Fragment(R.layout.add_list_fragment) {
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listsViewModel = ViewModelProvider(this,
            ListsViewModel.Factory { ListsProvider() }).get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setupViews()
    }

    private fun View.setupViews() {
        val nameEditText: EditText = findViewById(R.id.newListNameEditText)
        val progressBar: ProgressBar = findViewById(R.id.addListProgressBar)

        val createButton: Button = findViewById(R.id.createListButton)
        createButton.setOnClickListener { createButtonListener(nameEditText, progressBar) }

        val cancelButton: Button = findViewById(R.id.cancelCreateListButton)
        cancelButton.setOnClickListener { cancelButtonListener() }
        setupObserver(progressBar)
    }

    private fun View.setupObserver(progressBar: ProgressBar) {
        listsViewModel.addListLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                true -> navigateWithAction()
                false -> {
                    progressBar.isVisible = false
                    Snackbar.make(
                        findViewById(R.id.addListConstraint),
                        R.string.add_list_failed_msg,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun View.createButtonListener(nameEditText: EditText, progressBar: ProgressBar) {
        listsViewModel.addList(nameEditText.text.toString())
        progressBar.isVisible = true
        hideKeyboard()
    }

    private fun cancelButtonListener() {
        navigateWithAction()
    }

    private fun navigateWithAction() {
        val action =
            AddListFragmentDirections.actionAddListFragmentToListGalleryFragment()
        findNavController().navigate(action)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}
