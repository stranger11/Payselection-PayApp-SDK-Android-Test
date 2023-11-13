package payselection.demo.ui.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import payselection.demo.R
import payselection.demo.databinding.FSuccessBinding
import payselection.demo.sdk.PaymentService

class SuccessFragment : Fragment() {

    private lateinit var viewBinding: FSuccessBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FSuccessBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configurePaymentUI()
    }

    private fun configurePaymentUI(){
        val paymentHelper = PaymentService.getInstance()
        viewBinding.payCard.text = resources.getString(R.string.paid_card_format, paymentHelper.card?.number?.takeLast(4))
        viewBinding.navButton.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.popBackStack()
        }
    }
}