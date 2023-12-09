package pl.ozodbek.datastorepractice.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.ozodbek.datastorepractice.databinding.ActivityMainBinding
import pl.ozodbek.datastorepractice.util.oneliner_viewbinding.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}