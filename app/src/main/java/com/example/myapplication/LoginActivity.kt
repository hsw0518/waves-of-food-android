package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.doAfterTextChanged
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setAccessibilityHeading(binding.loginHeading, true)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, keyboard.bottom))
            insets
        }
        binding.emailInput.doAfterTextChanged { binding.emailInputLayout.error = null }
        binding.passwordInput.doAfterTextChanged { binding.passwordInputLayout.error = null }
        binding.passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitLogin()
                true
            } else false
        }
        binding.loginButton.setOnClickListener { submitLogin() }
        binding.socialButtons.facebookButton.setOnClickListener { showProviderNotice(R.string.facebook) }
        binding.socialButtons.googleButton.setOnClickListener { showProviderNotice(R.string.google) }
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun submitLogin() {
        val identifier = binding.emailInput.text?.toString().orEmpty().trim()
        val password = binding.passwordInput.text?.toString().orEmpty()
        binding.emailInputLayout.error = when {
            identifier.isBlank() -> getString(R.string.error_identifier_required)
            !LoginInputValidator.isValidIdentifier(identifier) -> getString(R.string.error_identifier_invalid)
            else -> null
        }
        binding.passwordInputLayout.error =
            if (password.isBlank()) getString(R.string.error_password_required) else null
        when {
            binding.emailInputLayout.error != null -> binding.emailInput.requestFocus()
            binding.passwordInputLayout.error != null -> binding.passwordInput.requestFocus()
            else -> {
                WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
                // Local validation is not authentication. Connect a repository here later.
                showNotice(R.string.login_unavailable_title, getString(R.string.login_unavailable))
            }
        }
    }

    private fun showNotice(title: Int, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.got_it, null)
            .show()
    }
}
