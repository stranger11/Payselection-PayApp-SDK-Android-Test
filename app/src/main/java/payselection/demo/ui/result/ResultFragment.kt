package payselection.demo.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import payselection.demo.R
import payselection.demo.databinding.FResultBinding
import payselection.demo.sdk.PaymentService

class ResultFragment : Fragment() {
    private lateinit var viewBinding: FResultBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FResultBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureUI()
    }

    private fun configureUI() {
        val success = arguments?.getBoolean(ARG_IS_SUCCESS) ?: false
        if (success) configureSuccessUI() else configureErrorUI()

        viewBinding.navButtonError.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }

        viewBinding.navButtonSuccess.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }

    private fun configureSuccessUI() {
        with(viewBinding) {
            success.visibility = View.VISIBLE
            error.visibility = View.GONE
            configurePaymentUI()
        }
    }

    private fun configureErrorUI() {
        with(viewBinding) {
            success.visibility = View.GONE
            error.visibility = View.VISIBLE
            close.setOnClickListener {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun configurePaymentUI() {
        val paymentHelper = PaymentService.getInstance()
        viewBinding.payCard.text = resources.getString(R.string.paid_card_format, paymentHelper.card?.number?.takeLast(4))
    }

    companion object {
        fun createInstance(bundle: Bundle): ResultFragment {
            val fragment = ResultFragment()
            fragment.arguments = bundle
            return fragment
        }

        const val ARG_IS_SUCCESS = "is_success"
    }
}