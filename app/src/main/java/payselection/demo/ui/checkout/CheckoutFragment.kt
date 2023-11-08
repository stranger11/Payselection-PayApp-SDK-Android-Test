package payselection.demo.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import payselection.demo.databinding.FCheckoutBinding
import payselection.demo.ui.checkout.adapter.ProductAdapter
import payselection.demo.ui.checkout.buttomSheet.BottomSheetPay


class CheckoutFragment : Fragment() {
    private val viewModel: CheckoutViewModel by viewModels()

    private var _viewBinding: FCheckoutBinding? = null
    private val viewBinding get() = _viewBinding!!

    private lateinit var productsAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FCheckoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureProducts()
        configureButton()
    }

    private fun configureProducts() {
        productsAdapter = ProductAdapter()
        viewBinding.cards.adapter = productsAdapter
        viewBinding.cards.layoutManager = LinearLayoutManager(requireContext())
        viewModel.products.observe(viewLifecycleOwner) {
            productsAdapter.updateData(it)
        }
    }

    private fun configureButton() {
        viewBinding.pay.setOnClickListener {
            showBottomDialog()
        }
    }

    private fun showBottomDialog() {
        BottomSheetPay().show(requireActivity().supportFragmentManager, BottomSheetPay::class.java.canonicalName)
    }

}