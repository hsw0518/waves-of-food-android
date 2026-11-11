package com.example.myapplication

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

internal fun AppCompatActivity.showProviderNotice(@StringRes providerName: Int) {
    val provider = getString(providerName)
    MaterialAlertDialogBuilder(this)
        .setTitle(getString(R.string.provider_title, provider))
        .setMessage(getString(R.string.provider_unavailable, provider))
        .setPositiveButton(R.string.got_it, null)
        .show()
}
