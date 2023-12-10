package pl.ozodbek.datastorepractice.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.ozodbek.datastorepractice.datastore.DataStoreRepository
import pl.ozodbek.datastorepractice.util.viewModelScopeOnIOThread
import javax.inject.Inject

@HiltViewModel
class DataViewerViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {


    /** READING DATA */
    val readUserName: LiveData<String?> = dataStoreRepository.readUserName.asLiveData()

    val readPhoneNumber: LiveData<String?> = dataStoreRepository.readPhoneNumber.asLiveData()



    /** SAVING DATA */
    fun saveeUserName(userName: String) = viewModelScopeOnIOThread {
        dataStoreRepository.saveUserName(userName)
    }

    fun saveePhoneNumber(phoneNumber: String) = viewModelScopeOnIOThread {
        dataStoreRepository.savePhoneNumber(phoneNumber)
    }



    /** REMOVING DATA */
    fun removeUserName() = viewModelScopeOnIOThread {
        dataStoreRepository.removeUserName()
    }

    fun removePhoneNumber() = viewModelScopeOnIOThread {
        dataStoreRepository.removePhoneNumber()
    }

}