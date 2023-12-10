package pl.ozodbek.datastorepractice.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import pl.ozodbek.datastorepractice.R
import pl.ozodbek.datastorepractice.databinding.FragmentDataViewerBinding
import pl.ozodbek.datastorepractice.util.launchOnIOThread
import pl.ozodbek.datastorepractice.util.launchOnMainThread
import pl.ozodbek.datastorepractice.util.observeLiveData
import pl.ozodbek.datastorepractice.util.onClick
import pl.ozodbek.datastorepractice.util.oneliner_viewbinding.viewBinding
import pl.ozodbek.datastorepractice.util.popBackStack
import pl.ozodbek.datastorepractice.viewmodels.DataViewerViewModel

@AndroidEntryPoint
class DataViewerFragment : Fragment(R.layout.fragment_data_viewer) {

    private val binding by viewBinding(FragmentDataViewerBinding::bind)
    private val viewModel: DataViewerViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }


    private fun setupUI() {
        setUpActionBar()
        setupUIElements()
        setupClickListeners()
    }

    private fun setUpActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).title = "DataStore show"
        binding.toolbar.setNavigationOnClickListener {
            popBackStack()
        }
    }


    private fun setupClickListeners() {
       binding.removeButton.onClick {
           removeDataFromDataStore()
       }
    }


    private fun setupUIElements() {
        launchOnMainThread {
            observeLiveData(viewModel.readUserName) { userName ->
                observeLiveData(viewModel.readPhoneNumber) { phoneNumber ->
                    userName?.let { name ->
                        phoneNumber?.let { phone ->

                            binding.userNameTextview.text = name
                            binding.phoneNumberTextview.text = phone

                        }
                    }
                }
            }
        }
    }


    private fun removeDataFromDataStore() {
        launchOnIOThread {
            viewModel.removeUserName()
            viewModel.removePhoneNumber()
        }
        clearTextViews()
    }

    private fun clearTextViews() {
        binding.userNameTextview.text = null
        binding.phoneNumberTextview.text = null
    }


}