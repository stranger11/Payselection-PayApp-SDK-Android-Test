package payselection.demo.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import payselection.demo.R
import payselection.demo.databinding.FCheckoutBinding
import payselection.demo.models.Product
import payselection.demo.ui.checkout.adapter.ProductAdapter
import payselection.demo.ui.checkout.buttomSheet.BottomSheetPay

class CheckoutFragment : Fragment() {

    private lateinit var viewBinding: FCheckoutBinding
    private lateinit var productsAdapter: ProductAdapter

    private val viewModel: CheckoutViewModel by activityViewModels()

    private val products by lazy {
        listOf(
            Product(
                name = resources.getString(R.string.product_1_name),
                description = resources.getString(R.string.product_1_desc),
                price = resources.getString(R.string.product_1_price),
                image = R.drawable.image_card_1
            ),
            Product(
                name = resources.getString(R.string.product_2_name),
                description = resources.getString(R.string.product_2_desc),
                price = resources.getString(R.string.product_2_price),
                image = R.drawable.image_card_2
            ),
            Product(
                name = resources.getString(R.string.product_3_name),
                description = resources.getString(R.string.product_3_desc),
                price = resources.getString(R.string.price_149),
                image = R.drawable.image_card_3
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FCheckoutBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureProducts()
        configureButton()
    }

    override fun onResume() {
        super.onResume()
        viewBinding.pay.isEnabled = true
    }

    private fun configureProducts() {
        productsAdapter = ProductAdapter(products)
        viewBinding.cards.adapter = productsAdapter
        viewBinding.cards.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun configureButton() {
        viewModel.checkoutButtonEnable.observe(viewLifecycleOwner){
            viewBinding.pay.isEnabled = it
        }
        viewBinding.pay.setOnClickListener {
            BottomSheetPay().show(requireActivity().supportFragmentManager, BottomSheetPay::class.java.canonicalName)
            viewModel.updateCheckoutButtonEnable(false)
        }
    }

    override fun onPause() {
        super.onPause()
        println("on pause")
    }
}