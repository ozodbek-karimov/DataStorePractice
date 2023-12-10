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
import pl.ozodbek.datastorepractice.databinding.FragmentDataViewerBinding
import pl.ozodbek.datastorepractice.util.Constants.Companion.MASK_FOR_PHONE_NUMBER_INPUT
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
class DataViewerFragment : Fragment(R.layout.fragment_data_viewer) {

    private val binding by viewBinding(FragmentDataViewerBinding::bind)
    private val adapterUserData: UserDataAdapter by lazy { UserDataAdapter() }
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
        setupRecyclerview()
    }

    private fun setupRecyclerview() {
        binding.recyclerview.adapter = adapterUserData
        adapterUserData.setItemClickListener { userData ->
            Toast.makeText(requireContext(), "$userData", Toast.LENGTH_SHORT).show()
            clearTextViews()
        }
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
                saveToDataStore(userNameBefore, phoneNumberBefore)
                readFromDataStore()
            }
        }

        binding.backButton.onClick {
            showInputLayouts()
        }
    }

    private fun readFromDataStore() {
        launchOnMainThread {
            observeLiveData(viewModel.readUserName) { userName ->
                observeLiveData(viewModel.readPhoneNumber) { phoneNumber ->
                    userName?.let { name ->
                        phoneNumber?.let { phone ->
                            val userDataList = listOf(UserData(name, phone))
                            adapterUserData.submitList(userDataList)
                        }
                    }
                }
            }
        }
    }

    private fun saveToDataStore(userNameBefore: String, phoneNumberBefore: String) {
        launchOnIOThread {
            viewModel.saveeUserName(userNameBefore)
            viewModel.saveePhoneNumber(phoneNumberBefore)
        }
    }

    private fun hideInputLayouts() {
        binding.usernameInputLayout.gone()
        binding.phoneNumberInputLayout.gone()
    }

    private fun showTextViews() {
        clearEditTexts()
        binding.recyclerview.show()
    }


    private fun clearTextViews() {
        launchOnIOThread {
            viewModel.removeUserName()
            viewModel.removePhoneNumber()
        }
    }

    private fun clearEditTexts() {
        binding.usernameEdittext.text = null
        binding.phoneNumberEdittext.text = null
    }

    private fun showInputLayouts() {
        binding.recyclerview.gone()
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