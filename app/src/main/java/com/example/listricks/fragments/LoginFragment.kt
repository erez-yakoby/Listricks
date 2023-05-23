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
import androidx.navigation.fragment.findNavController
import com.example.listricks.R
import com.example.listricks.providers.ListsProvider
import com.example.listricks.providers.UsersProvider
import com.example.listricks.viewModels.ListsViewModel
import com.example.listricks.viewModels.UsersViewModel
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment(R.layout.login_fragment) {

    private lateinit var progressBar: ProgressBar
    private lateinit var userName: EditText
    private lateinit var password: EditText
    private lateinit var loginBtn: Button
    private lateinit var signupBtn: Button
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var listsViewModel: ListsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersViewModel = ViewModelProvider(this,
            UsersViewModel.Factory { UsersProvider() }).get()
        listsViewModel = ViewModelProvider(this,
            ListsViewModel.Factory { ListsProvider() }).get()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setUpViews()
        view.setupObserver()
    }

    private fun View.setUpViews() {
        userName = findViewById(R.id.login_username_editText)
        password = findViewById(R.id.login_password_editText)

        signupBtn = findViewById(R.id.login_signup_btn)
        signupBtn.setOnClickListener { navigateToSignupFragment() }

        progressBar = findViewById(R.id.loginProgressBar)

        loginBtn = findViewById(R.id.login_btn)
        loginBtn.setOnClickListener { loginUser() }

        connectKeyboardToLoginButton()
    }

    private fun View.loginUser() {
        val usr = userName.text.toString()
        val pwd = password.text.toString()
        usersViewModel.loginUser(usr, pwd)
        hideKeyboard()
        progressBar.isVisible = true
    }

    private fun connectKeyboardToLoginButton() {
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginBtn.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun View.setupObserver() {
        usersViewModel.loginLiveData.observe(viewLifecycleOwner) { res ->
            when (res) {
                true -> moveToListGallery()
                false -> {
                    progressBar.isVisible = false

                    Snackbar.make(
                        findViewById(R.id.mainConstraintLayout),
                        R.string.login_failed_msg,
                        Snackbar.LENGTH_LONG
                    ).show()
                    usersViewModel.resetLiveData()
                }
                null -> {}
            }
        }
    }

    private fun moveToListGallery() {
        listsViewModel.setUserName(userName.text.toString())
        navigateToListsFragment()
    }

    private fun navigateToSignupFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        findNavController().navigate(action)
    }

    private fun navigateToListsFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToListsGalleryFragment()
        findNavController().navigate(action)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}