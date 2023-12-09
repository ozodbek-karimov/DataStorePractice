package pl.ozodbek.datastorepractice.ui

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import pl.ozodbek.datastorepractice.R
import pl.ozodbek.datastorepractice.databinding.FragmentDataViewerBinding
import pl.ozodbek.datastorepractice.util.Constants.Companion.MASK_FOR_PHONE_NUMBER_INPUT
import pl.ozodbek.datastorepractice.util.fullText
import pl.ozodbek.datastorepractice.util.gone
import pl.ozodbek.datastorepractice.util.onClick
import pl.ozodbek.datastorepractice.util.oneliner_viewbinding.viewBinding
import pl.ozodbek.datastorepractice.util.setMask
import pl.ozodbek.datastorepractice.util.show


class DataViewerFragment : Fragment(R.layout.fragment_data_viewer) {

    private val binding by viewBinding(FragmentDataViewerBinding::bind)

    private var unMuskedValueOfPhoneNumber: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }



    private fun setupUI() {
        setUpActionBar()
        setupMaskedEditText()
        setupClickListeners()
    }

    private fun setupMaskedEditText() {
        binding.phoneNumberEdittext.setMask(MASK_FOR_PHONE_NUMBER_INPUT) {
            unMuskedValueOfPhoneNumber = it?.trim() ?: ""
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setupClickListeners() {
        binding.showButton.onClick {
            val userNameBefore = binding.usernameEdittext.fullText
            val phoneNumberBefore = unMuskedValueOfPhoneNumber.toString()


            if (areFieldsValid(userNameBefore, phoneNumberBefore)) {
                hideInputLayouts()
                showTextViews()
                setTextViewsText()
            }
        }

        binding.clearButton.onClick {
            clearTextViews()
            clearEditTexts()
            showInputLayouts()
        }
    }

    private fun hideInputLayouts() {
        binding.usernameInputLayout.gone()
        binding.phoneNumberInputLayout.gone()
    }

    private fun showTextViews() {
        binding.usernameInputTv.show()
        binding.phoneNumberTv.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setTextViewsText() {
        binding.phoneNumberTv.text = "+998$unMuskedValueOfPhoneNumber"

    }

    private fun clearTextViews() {
        binding.usernameInputTv.text = null
        binding.phoneNumberTv.text = null
    }

    private fun clearEditTexts() {
        binding.phoneNumberEdittext.text = null
    }

    private fun showInputLayouts() {
        binding.usernameInputLayout.show()
        binding.phoneNumberInputLayout.show()
    }

    private fun areFieldsValid(
        usernameBefore: String,
        phoneNumberBefore: String,
    ): Boolean {
        if (usernameBefore.isBlank()) {
            showError(binding.usernameInputLayout, "Please enter Your Name !")
            return false
        }
        releaseView(binding.usernameInputLayout)

        if (phoneNumberBefore.isBlank()) {
            showError(binding.phoneNumberInputLayout, "Please enter PhoneNumber !")
            return false
        }
        releaseView(binding.phoneNumberInputLayout)

        return true
    }

    private fun releaseView(textInputLayout: TextInputLayout) {
        textInputLayout.error = null
        textInputLayout.clearFocus()
    }

    private fun showError(inputLayout: TextInputLayout, errorMessage: String) {
        inputLayout.requestFocus()
        inputLayout.error = errorMessage
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).title = "MaskedEditText"
    }

}