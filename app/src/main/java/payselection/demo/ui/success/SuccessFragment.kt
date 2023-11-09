package payselection.demo.ui.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import payselection.demo.databinding.FSuccessBinding
import payselection.demo.ui.error.ErrorFragment

class SuccessFragment : Fragment() {

    private var _viewBinding: FSuccessBinding? = null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _viewBinding = FSuccessBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.navButton.setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }
}