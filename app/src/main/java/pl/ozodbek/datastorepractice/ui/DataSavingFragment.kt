package pl.ozodbek.datastorepractice.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import pl.ozodbek.datastorepractice.R
import pl.ozodbek.datastorepractice.adapters.UserDataAdapter
import pl.ozodbek.datastorepractice.data.UserData
import pl.ozodbek.datastorepractice.databinding.FragmentDataSavingBinding
import pl.ozodbek.datastorepractice.databinding.FragmentDataViewerBinding
import pl.ozodbek.datastorepractice.util.Constants.Companion.MASK_FOR_PHONE_NUMBER_INPUT
import pl.ozodbek.datastorepractice.util.changeFragmentTo
import pl.ozodbek.datastorepractice.util.fullText
import pl.ozodbek.datastorepractice.util.gone
import pl.ozodbek.datastorepractice.util.launchOnIOThread
import pl.ozodbek.datastorepractice.util.launchOnMainThread
import pl.ozodbek.datastorepractice.util.observeLiveData
import pl.ozodbek.datastorepractice.util.onClick
import pl.ozodbek.datastorepractice.util.oneliner_viewbinding.viewBinding
import pl.ozodbek.datastorepractice.util.setMask
import pl.ozodbek.datastorepractice.util.show
import pl.ozodbek.datastorepractice.viewmodels.DataViewerViewModel

@AndroidEntryPoint
class DataSavingFragment : Fragment(R.layout.fragment_data_saving) {

    private val binding by viewBinding(FragmentDataSavingBinding::bind)
    private val viewModel: DataViewerViewModel by viewModels()

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

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).title = "DataStore"
    }


    private fun setupMaskedEditText() {
        binding.phoneNumberEdittext.setMask(MASK_FOR_PHONE_NUMBER_INPUT) {
            unMuskedValueOfPhoneNumber = it?.trim() ?: ""
        }

    }

    private fun setupClickListeners() {

        binding.saveButton.onClick {
            val userNameBefore = binding.usernameEdittext.fullText
            val phoneNumberBefore = unMuskedValueOfPhoneNumber.toString()


            if (areFieldsValid(userNameBefore, phoneNumberBefore)) {
                saveToDataStore(userNameBefore, phoneNumberBefore)
            }
        }

        binding.showButton.onClick {
            changeFragmentTo(DataSavingFragmentDirections.actionDataSavingFragmentToDataViewerFragment())
            clearEditTexts()
        }
    }


    private fun saveToDataStore(userNameBefore: String, phoneNumberBefore: String) {
        launchOnIOThread {
            viewModel.saveeUserName(userNameBefore)
            viewModel.saveePhoneNumber(phoneNumberBefore)
        }
        Toast.makeText(
            requireContext(),
            "$userNameBefore and $phoneNumberBefore saved !",
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun clearEditTexts() {
        binding.usernameEdittext.text = null
        binding.phoneNumberEdittext.text = null
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


}