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
import com.example.myapplication.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setAccessibilityHeading(binding.signUpHeading, true)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val keyboard = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(bars.left, bars.top, bars.right, maxOf(bars.bottom, keyboard.bottom))
            insets
        }

        binding.nameInput.doAfterTextChanged { binding.nameInputLayout.error = null }
        binding.emailInput.doAfterTextChanged { binding.emailInputLayout.error = null }
        binding.passwordInput.doAfterTextChanged { binding.passwordInputLayout.error = null }
        binding.passwordInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId != EditorInfo.IME_ACTION_DONE) return@setOnEditorActionListener false
            openLocation()
            true
        }
        binding.signUpNextButton.setOnClickListener { openLocation() }
        binding.alreadyHaveAccountButton.setOnClickListener { finish() }
        binding.socialButtons.facebookButton.setOnClickListener { showProviderNotice(R.string.facebook) }
        binding.socialButtons.googleButton.setOnClickListener { showProviderNotice(R.string.google) }
    }

    private fun openLocation() {
        val name = binding.nameInput.text?.toString().orEmpty()
        val identifier = binding.emailInput.text?.toString().orEmpty()
        val password = binding.passwordInput.text?.toString().orEmpty()

        binding.nameInputLayout.error =
            if (name.isBlank()) getString(R.string.error_name_required) else null
        binding.emailInputLayout.error = when {
            identifier.isBlank() -> getString(R.string.error_identifier_required)
            !LoginInputValidator.isValidIdentifier(identifier) -> getString(R.string.error_identifier_invalid)
            else -> null
        }
        binding.passwordInputLayout.error =
            if (password.isBlank()) getString(R.string.error_password_required) else null

        when {
            binding.nameInputLayout.error != null -> binding.nameInput.requestFocus()
            binding.emailInputLayout.error != null -> binding.emailInput.requestFocus()
            binding.passwordInputLayout.error != null -> binding.passwordInput.requestFocus()
            else -> {
                WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
                binding.passwordInput.text?.clear()
                startActivity(Intent(this, LocationActivity::class.java))
            }
        }
    }
}
