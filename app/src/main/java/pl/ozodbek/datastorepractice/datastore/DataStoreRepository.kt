package pl.ozodbek.datastorepractice.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.ozodbek.datastorepractice.util.Constants.Companion.PHONE_NUMBER_PREFERENCE
import pl.ozodbek.datastorepractice.util.Constants.Companion.USER_NAME_PREFERENCE
import javax.inject.Inject

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    preferencesDataStore: DataStore<Preferences>
) {

    private object PreferenceKeys {
        val userName = stringPreferencesKey(USER_NAME_PREFERENCE)
        val phoneNumber = stringPreferencesKey(PHONE_NUMBER_PREFERENCE)
    }

    private val dataStore = preferencesDataStore

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.userName] = userName
        }
    }

    suspend fun savePhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.phoneNumber] = phoneNumber
        }
    }

    val readUserName: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.userName]
        }

    val readPhoneNumber: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[PreferenceKeys.phoneNumber]
        }

    suspend fun removeUserName() {
        dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.userName)
        }
    }

    suspend fun removePhoneNumber() {
        dataStore.edit { preferences ->
            preferences.remove(PreferenceKeys.phoneNumber)
        }
    }
}
