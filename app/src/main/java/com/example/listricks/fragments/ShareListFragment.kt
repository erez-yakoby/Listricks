package com.example.listricks.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.listricks.R
import com.example.listricks.providers.ListsProvider
import com.example.listricks.viewModels.ListsViewModel
import com.google.android.material.snackbar.Snackbar

class ShareListFragment : Fragment(R.layout.share_list_fragment) {
    private lateinit var userET: EditText
    private lateinit var shareBtn: Button
    private lateinit var listName: String
    private lateinit var userName: String
    private lateinit var listsViewModel: ListsViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listsViewModel = ViewModelProvider(this,
            ListsViewModel.Factory { ListsProvider() }).get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setUpViews()
        view.setupObserver()
    }

    private fun View.setUpViews() {
        userName = listsViewModel.getUserName()
        listName = listsViewModel.getCurList()
        userET = findViewById(R.id.email_et)
        progressBar = findViewById(R.id.shareProgressBar)

        shareBtn = findViewById(R.id.share_btn)
        shareBtn.setOnClickListener { shareList() }


        findViewById<TextView>(R.id.share_lst_name_tv).text =
            getString(R.string.share_list_header, listName)
        connectKeyboardToShareButton()
    }

    private fun connectKeyboardToShareButton() {
        userET.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                shareBtn.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun View.shareList() {
        listsViewModel.shareList(userET.text.toString())
        progressBar.isVisible = true
        hideKeyboard()
    }

    private fun View.setupObserver() {
        listsViewModel.shareListLiveData.observe(viewLifecycleOwner) { res ->
            if (res != null) {
                progressBar.isVisible = false
            }
            when (res) {
                true -> {
                    userET.setText("")
                    showSnakeBar(R.string.share_list_succeed_msg)
                }
                false -> {
                    showSnakeBar(R.string.share_list_failed_msg)
                }
            }
        }
    }

    private fun View.showSnakeBar(msgId: Int) {
        Snackbar.make(findViewById(R.id.main_share_layout), msgId, Snackbar.LENGTH_LONG).show()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}