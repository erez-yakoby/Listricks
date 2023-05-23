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
import com.example.listricks.providers.UsersProvider
import com.example.listricks.viewModels.UsersViewModel
import com.google.android.material.snackbar.Snackbar

class SignUpFragment : Fragment(R.layout.signup_fragment) {
    private lateinit var userName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signupBtn: Button
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usersViewModel = ViewModelProvider(this,
            UsersViewModel.Factory { UsersProvider() }).get()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setUpViews()
        setupObserver()
    }

    private fun setupObserver() {
        usersViewModel.signUpLiveData.observe(viewLifecycleOwner) { res ->
            progressBar.isVisible = false
            when (res) {
                true -> navigateToLogInFragment()
                false -> {
                    Snackbar.make(
                        requireView().findViewById(R.id.signUpMainConstraintLayout),
                        R.string.signup_failed_msg,
                        Snackbar.LENGTH_LONG
                    ).show()
                    usersViewModel.resetLiveData()
                }
                null -> {}
            }
        }
    }

    private fun View.setUpViews() {
        userName = findViewById(R.id.signup_username_editText)
        email = findViewById(R.id.signup_email_editText)
        password = findViewById(R.id.signup_password_editText)
        progressBar = findViewById(R.id.signupProgressBar)
        signupBtn = findViewById(R.id.signup_signup_btn)
        signupBtn.setOnClickListener {signUpNewUser()}

        connectKeyboardToSignupButton()
    }

    private fun View.signUpNewUser() {
        val usr = userName.text.toString()
        val mail = email.text.toString()
        val pwd = password.text.toString()
        usersViewModel.signUpNewUser(usr, mail, pwd)
        hideKeyboard()
        progressBar.isVisible = true

    }

    private fun connectKeyboardToSignupButton() {
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                signupBtn.performClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun navigateToLogInFragment() {
        val action = SignUpFragmentDirections.actionSignupFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
