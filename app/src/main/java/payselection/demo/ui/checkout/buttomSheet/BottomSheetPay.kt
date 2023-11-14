package payselection.demo.ui.checkout.buttomSheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import payselection.demo.R
import payselection.demo.databinding.ButtomSheetBinding
import payselection.demo.models.Card
import payselection.demo.sdk.PaymentService
import payselection.demo.ui.checkout.CheckoutViewModel
import payselection.demo.ui.checkout.adapter.CardAdapter
import payselection.demo.ui.checkout.common.CardListener
import payselection.demo.ui.checkout.common.PaymentResultListener
import payselection.demo.ui.checkout.common.ActionState
import payselection.demo.ui.result.ResultFragment
import payselection.demo.ui.result.ResultFragment.Companion.ARG_IS_SUCCESS
import payselection.demo.utils.ADD_ITEM_INDEX
import payselection.demo.utils.EMPTY_STRING
import payselection.demo.utils.ExpiryDateTextWatcher
import payselection.demo.utils.FourDigitCardFormatWatcher
import payselection.demo.utils.ThreeDigitWatcher
import payselection.demo.utils.getPaymentSystem
import payselection.demo.utils.updateColor
import payselection.payments.sdk.configuration.SdkConfiguration
import payselection.payments.sdk.models.results.pay.PaymentResult
import payselection.payments.sdk.ui.ThreeDsDialogFragment


class BottomSheetPay : BottomSheetDialogFragment(), CardListener, PaymentResultListener {

    private lateinit var binding: ButtomSheetBinding
    private val viewModel: CheckoutViewModel by viewModels()

    private lateinit var cardsAdapter: CardAdapter

    private lateinit var paymentService: PaymentService

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureCardAdapter()
        configureError()
        configureButton()
        configureAnother()
    }

    private fun configureCardAdapter() = with(binding) {
        cardsAdapter = CardAdapter(this@BottomSheetPay)
        cardsPager.adapter = cardsAdapter
        cardsPager.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        viewModel.cards.observe(viewLifecycleOwner) {
            cardsAdapter.updateList(it)
        }
    }

    private fun configureButton() {
        with(binding) {
            viewModel.isEnable.observe(viewLifecycleOwner) {
                pay.isEnabled = it
            }
            viewModel.actionState.observe(viewLifecycleOwner) { state ->
                pay.text =
                    if (state == ActionState.PAY) requireContext().getString(R.string.pay_card) else requireContext().getString(R.string.save_card)
                pay.setOnClickListener {
                    when (state) {
                        ActionState.ADD -> viewModel.addCard(editCardNumber.text.toString(), editCardData.text.toString())
                        else -> pay(Card(viewModel.cardNumber.value.orEmpty(), viewModel.cardDate.value.orEmpty(), viewModel.cardCvv.value.orEmpty()))
                    }
                }
            }
        }
    }

    private fun configureAnother() {
        with(binding) {
            editCardNumber.addTextChangedListener(FourDigitCardFormatWatcher())
            editCardData.addTextChangedListener(ExpiryDateTextWatcher())
            editCardCvv.addTextChangedListener(ThreeDigitWatcher())

            viewModel.currentPosition.observe(viewLifecycleOwner) { currentPosition ->
                if (currentPosition == ADD_ITEM_INDEX || currentPosition == null) {
                    editCardNumber.setText(EMPTY_STRING)
                    editCardData.setText(EMPTY_STRING)
                    editCardCvv.setText(EMPTY_STRING)
                } else {
                    val cards = viewModel.cards.value
                    editCardNumber.setText(cards?.get(currentPosition)?.number.orEmpty())
                    editCardData.setText(cards?.get(currentPosition)?.date.orEmpty())
                    if (currentPosition != 0) editCardCvv.setText(EMPTY_STRING)
                }
                requireView().findFocus()?.clearFocus()
                cardsAdapter.updatePosition(currentPosition)
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                pay.isEnabled = isLoading.not()
            }
        }
    }

    private fun configureError() {
        with(binding) {
            editCardCvv.doOnTextChanged { text, start, before, count ->
                viewModel.setCardCvv(text.toString())
            }

            editCardData.doOnTextChanged { text, start, before, count ->
                viewModel.setCardDate(text.toString())
            }

            editCardNumber.doOnTextChanged { text, start, before, count ->
                viewModel.setCardNumber(text.toString())
                if (text?.length == 19) {
                    binding.cardNumber.endIconDrawable = getPaymentSystem(text.filter { it.isDigit() }.toString())
                        ?.let { ContextCompat.getDrawable(requireContext(), it.imageWithLine) }
                }
            }

            viewModel.isCvvValid.observe(viewLifecycleOwner) { isValid ->
                cardCvv.updateColor(
                    requireContext(),
                    !isValid,
                    requireContext().getString(R.string.cvv),
                    requireContext().getString(R.string.error_cvv)
                )
                editCardCvv.updateColor(requireContext(), !isValid)
            }

            viewModel.isDataValid.observe(viewLifecycleOwner) { isValid ->
                cardData.updateColor(
                    requireContext(),
                    !isValid,
                    requireContext().getString(R.string.dd_mm),
                    requireContext().getString(R.string.error_date)
                )
                editCardData.updateColor(requireContext(), !isValid)
            }

            viewModel.isNumberValid.observe(viewLifecycleOwner) { isValid ->
                cardNumber.updateColor(
                    requireContext(),
                    !isValid,
                    requireContext().getString(R.string.card_number),
                    requireContext().getString(R.string.error_number)
                )
                editCardNumber.updateColor(requireContext(), !isValid)
                cardNumber.isEndIconVisible = isValid && !viewModel.cardNumber.value.isNullOrEmpty()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        binding.editCardNumber.setText(EMPTY_STRING)
        binding.editCardData.setText(EMPTY_STRING)
        binding.editCardCvv.setText(EMPTY_STRING)
    }

    private fun pay(card: Card) {
        viewModel.updateLoad(true)
        paymentService = PaymentService.getInstance(this)
        paymentService.init(
            SdkConfiguration(
                "04bd07d3547bd1f90ddbd985feaaec59420cabd082ff5215f34fd1c89c5d8562e8f5e97a5df87d7c99bc6f16a946319f61f9eb3ef7cf355d62469edb96c8bea09e",
                "21044",
                true
            )
        )
        paymentService.pay(card)
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

    override fun onPaymentResult(result: PaymentResult?) {
        dismiss()
        viewModel.updateLoad(true)
        if (result != null) {
            show3DS(result.redirectUrl)
        }else {
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_SUCCESS, false)
            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragment_container, ResultFragment.createInstance(bundle))
                .addToBackStack(ResultFragment::class.java.canonicalName)
            fragmentTransaction.commit()
        }
    }
}