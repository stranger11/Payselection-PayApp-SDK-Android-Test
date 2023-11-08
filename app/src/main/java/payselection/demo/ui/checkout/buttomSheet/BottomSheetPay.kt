package payselection.demo.ui.checkout.buttomSheet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import payselection.demo.R
import payselection.demo.databinding.ButtomSheetBinding
import payselection.demo.models.Card
import payselection.demo.sdk.PaymentHelper
import payselection.demo.ui.checkout.CheckoutViewModel
import payselection.demo.ui.checkout.adapter.CardAdapter
import payselection.demo.ui.checkout.common.CardListener
import payselection.demo.ui.checkout.common.State
import payselection.demo.utils.ExpiryDateTextWatcher
import payselection.demo.utils.FourDigitCardFormatWatcher
import payselection.demo.utils.updateColor
import payselection.payments.sdk.PaySelectionPaymentsSdk
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.requests.pay.CardDetails
import payselection.payments.sdk.models.requests.pay.CustomerInfo
import payselection.payments.sdk.models.requests.pay.PaymentData
import payselection.payments.sdk.models.requests.pay.TransactionDetails
import payselection.payments.sdk.ui.ThreeDsDialogFragment


class BottomSheetPay : BottomSheetDialogFragment(), CardListener, ThreeDsDialogFragment.ThreeDSDialogListener {
    private lateinit var binding: ButtomSheetBinding
    private val viewModel: CheckoutViewModel by viewModels()

    private lateinit var cardsAdapter: CardAdapter

    private lateinit var sdk: PaySelectionPaymentsSdk

    private val handler = CoroutineExceptionHandler { context, exception ->
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), "Caught $exception", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ButtomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureError()
        configureViewPager()
        configureAnother()
    }

    private fun configureViewPager() = with(binding) {
        cardsAdapter = CardAdapter(this@BottomSheetPay, requireContext())
        cardsPager.adapter = cardsAdapter
        cardsPager.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.uiCards.observe(viewLifecycleOwner) {
            cardsAdapter.updateData(it)
        }
    }

    private fun configureAnother() {
        binding.editCardNumber.addTextChangedListener(FourDigitCardFormatWatcher())
        binding.editCardData.addTextChangedListener(ExpiryDateTextWatcher())
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.pay.text =
                if (state == State.PAY) requireContext().getString(R.string.pay_card) else requireContext().getString(R.string.save_card)
            binding.pay.setOnClickListener {
                if (state == State.ADD) viewModel.addCard(binding.editCardNumber.text.toString(), binding.editCardData.text.toString())
                else pay(Card(viewModel.cardNumber.value.orEmpty(), viewModel._cardDate.value.orEmpty(), viewModel.cardCvv.value.orEmpty()))
            }
        }
        viewModel.currentPosition.observe(viewLifecycleOwner) {
            if (it == viewModel.cards.value?.size || it == null) {
                binding.editCardNumber.setText("")
                binding.editCardData.setText("")
            } else {
                binding.editCardNumber.setText(viewModel.cards.value?.get(it)?.number.orEmpty())
                binding.editCardData.setText(viewModel.cards.value?.get(it)?.date.orEmpty())
            }
            binding.editCardCvv.setText("")
            requireView().findFocus()?.clearFocus()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun configureError() {
        binding.editCardCvv.doOnTextChanged { text, start, before, count ->
            viewModel.setCardCvv(text.toString())
        }

        binding.editCardData.doOnTextChanged { text, start, before, count ->
            viewModel.setCardDate(text.toString())
        }

        binding.editCardNumber.doOnTextChanged { text, start, before, count ->
            viewModel.setCardNumber(text.toString())
            if (text?.length == 19) {
                binding.cardNumber.endIconDrawable = viewModel.getPaymentSystem(text.toString())
                    ?.let { ContextCompat.getDrawable(requireContext(), it.imageWithLine) }
            }
        }
        viewModel.isCvvValid.observe(viewLifecycleOwner) { isValid ->
            binding.cardCvv.updateColor(
                requireContext(),
                !isValid,
                requireContext().getString(R.string.cvv),
                requireContext().getString(R.string.error_cvv)
            )
            binding.editCardCvv.updateColor(requireContext(), !isValid)
        }

        viewModel.isDataValid.observe(viewLifecycleOwner) { isValid ->
            binding.cardData.updateColor(
                requireContext(),
                !isValid,
                requireContext().getString(R.string.dd_mm),
                requireContext().getString(R.string.error_date)
            )
            binding.editCardData.updateColor(requireContext(), !isValid)
        }

        viewModel.isNumberValid.observe(viewLifecycleOwner) { isValid ->
            binding.cardNumber.updateColor(
                requireContext(),
                !isValid,
                requireContext().getString(R.string.card_number),
                requireContext().getString(R.string.error_number)
            )
            binding.editCardNumber.updateColor(requireContext(), !isValid)
            binding.cardNumber.isEndIconVisible = isValid && !viewModel.cardNumber.value.isNullOrEmpty()
        }

        viewModel.isEnable.observe(viewLifecycleOwner) {
            binding.pay.isEnabled = it
        }
    }

    private fun pay(card: Card) {
        sdk = PaySelectionPaymentsSdk.getInstance(
            SdkConfiguration(
                "04bd07d3547bd1f90ddbd985feaaec59420cabd082ff5215f34fd1c89c5d8562e8f5e97a5df87d7c99bc6f16a946319f61f9eb3ef7cf355d62469edb96c8bea09e",
                "21044",
                true
            )
        )
        makePay(card)
    }

    private fun makePay(card: Card) {
        GlobalScope.launch(handler) {
            val orderId = "SAM_SDK_3"
            testPay(orderId, card)
        }
    }

    private suspend fun testPay(orderId: String, card: Card) {
        val dateParts = card.date.split('/')
        sdk.pay(
            orderId = orderId,
            description = "test payment",
            paymentData = PaymentData.create(
                transactionDetails = TransactionDetails(
                    amount = "10",
                    currency = "RUB"
                ),
                cardDetails = CardDetails(
                    cardholderName = "TEST CARD",
                    cardNumber = card.number,
                    cvc = card.cvv.orEmpty(),
                    expMonth = dateParts[0],
                    expYear = dateParts[1]
                )
            ),
            customerInfo = CustomerInfo(
                email = "user@example.com",
                phone = "+19991231212",
                language = "en",
                address = "string",
                town = "string",
                zip = "string",
                country = "USA"
            ),
            rebillFlag = false
        ).proceedResult(
            success = {
                println("Result $it")
                show3DS(it.redirectUrl)
            },
            error = {
                it.printStackTrace()
            }
        )
    }

    private fun show3DS(url: String) {
        // Открываем 3ds форму
        ThreeDsDialogFragment
            .newInstance(url)
            .show(requireActivity().supportFragmentManager, "3DS")
    }

    override fun onSelect(position: Int) {
        viewModel.onCardSelected(position)
    }

    override fun onAuthorizationCompleted() {
        Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
    }

    override fun onAuthorizationFailed() {
        Toast.makeText(requireContext(), "Fail", Toast.LENGTH_LONG).show()
    }
}