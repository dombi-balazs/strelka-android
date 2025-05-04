package com.aricia.strelka.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.aricia.strelka.android.ui.theme.StrelkaTheme

import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private fun handleAuthDeepLink(intent: Intent?) {
        val data: Uri? = intent?.data
        if (data != null && data.scheme == "myapp" && data.host == "auth") {
            Toast.makeText(
                this, "Auth Deep Link received! Processing...",
                Toast.LENGTH_LONG
            ).show()
            lifecycleScope.launch {
                try {
                    Toast.makeText(
                        this@MainActivity,
                        "Deep Link processed by SDK!",
                        Toast.LENGTH_SHORT
                    ).show()


                } catch (e: Exception) {
                    Toast.makeText(
                        this@MainActivity,
                        "Deep Link error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleAuthDeepLink(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handleAuthDeepLink(intent)
        setContent {

            StrelkaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StrelkaNavHost(modifier = Modifier.padding(innerPadding))
                }

            }
        }
    }
}
