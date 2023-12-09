package pl.ozodbek.datastorepractice.util

import android.app.Activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.textfield.TextInputEditText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ozodbek.datastorepractice.R

fun Fragment.changeFragmentTo(destination: Any) {
    val navController = this.findNavController()

    when (destination) {
        is Int -> {
            navController.safeNavigate(destination)
        }

        is NavDirections -> {
            navController.safeNavigate(destination)
        }
    }
}

fun Fragment.changeFragmentTo(destination: Any, navController: NavController?) {

    when (destination) {
        is Int -> {
            navController?.safeNavigate(destination)
        }

        is NavDirections -> {
            navController?.safeNavigate(destination)
        }
    }
}

fun NavController.safeNavigate(direction: Any) {
    when (direction) {
        is Int -> {
            if (currentDestination?.id == direction) {
                return
            }
            navigate(direction)
        }

        is NavDirections -> {
            var isNavigationEnabled = true
            CoroutineScope(Dispatchers.Main).launch {
                if (isNavigationEnabled) {
                    isNavigationEnabled = false
                    currentDestination?.getAction(direction.actionId)?.run {
                        navigate(direction)
                    }
                    delay(1000)
                    isNavigationEnabled = true
                }
            }
        }

        else -> {
        }
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.onClick(clickListener: (View) -> Unit) {
    setOnClickListener(clickListener)
}


fun Fragment.onBackPressed(onBackPressed: OnBackPressedCallback.() -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this) {
        onBackPressed()
    }
}

fun Fragment.popBackStack() {
    findNavController().popBackStack()
}


fun View.showSoftKeyboard() {
    if (this.requestFocus()) {
        val imm = this.context.getSystemService(InputMethodManager::class.java)
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.hideSoftKeyboard() {
    val inputMethodManager = requireActivity().getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            observer.onChanged(value)
            removeObserver(this)
        }

    })
}

val EditText.fullText: String
    get() = this.text.toString().trim()

fun <T : ViewBinding> ViewGroup.viewBinding(viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> T) =
    viewBindingFactory.invoke(LayoutInflater.from(this.context), this, false)


fun <T : ViewBinding> Fragment.inflateViewBinding(
    bindingInflater: (LayoutInflater) -> T,
): T {
    return bindingInflater.invoke(LayoutInflater.from(requireContext()))
}

fun ViewModel.viewModelScopeOnIOThread(
    block: suspend CoroutineScope.() -> Unit,
) = viewModelScope.launch(Dispatchers.IO) {
    block()
}

fun ViewModel.viewModelScopeOnDefaultThread(
    block: suspend CoroutineScope.() -> Unit,
) = viewModelScope.launch {
    block()
}


fun TextInputEditText.setMask(mask: String, onUnmaskedString: (String?) -> Unit) {
    val listener = MaskedTextChangedListener(
        mask,
        false,
        this@setMask,
        null,
        object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(
                maskFilled: Boolean,
                extractedValue: String,
                formattedValue: String,
                tailPlaceholder: String,
            ) {
                onUnmaskedString(extractedValue)
            }
        }
    )

    this@setMask.addTextChangedListener(listener)
    this@setMask.setHintTextColor(getColor(R.color.lightMediumGray))
    this@setMask.setText("")
    listener.autoskip = true
}


fun View.getColor(color: Int): Int {
    return ContextCompat.getColor(this.context, color)
}






