package pl.ozodbek.datastorepractice.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import pl.ozodbek.datastorepractice.R
import pl.ozodbek.datastorepractice.databinding.FragmentDataViewerBinding
import pl.ozodbek.profex.util.oneliner_viewbinding.viewBinding


class DataViewerFragment : Fragment(R.layout.fragment_data_viewer) {

    private val binding by viewBinding(FragmentDataViewerBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}