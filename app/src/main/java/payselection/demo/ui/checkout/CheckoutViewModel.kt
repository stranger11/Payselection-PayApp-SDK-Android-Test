package payselection.demo.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import payselection.demo.R
import payselection.demo.models.Card
import payselection.demo.models.UiCard
import payselection.demo.ui.checkout.common.CardType
import payselection.demo.ui.checkout.common.State
import payselection.demo.utils.CombineLiveData
import payselection.demo.utils.CombineTripleLiveData
import payselection.demo.utils.validCardDate
import payselection.demo.utils.validCardNumber
import payselection.demo.utils.validCvv


class CheckoutViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    var currentPosition = MutableLiveData<Int?>(null)

    val uiCards = CombineLiveData(cards, currentPosition) { cards, position ->
        val cardList = generateUiCardList(cards.orEmpty(), position)
        addAddCardToList(cardList, position)
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isDataValid = MutableLiveData<Boolean>()
    val isDataValid: LiveData<Boolean> = _isDataValid

    private val _isNumberValid = MutableLiveData<Boolean>()
    val isNumberValid: LiveData<Boolean> = _isNumberValid

    private val _isCvvValid = MutableLiveData<Boolean>()
    val isCvvValid: LiveData<Boolean> = _isCvvValid

    val cardDate = MutableLiveData<String>()
    val cardNumber = MutableLiveData<String>()
    val cardCvv = MutableLiveData<String>()

    val isEnable = CombineTripleLiveData(_isDataValid, _isNumberValid, _isCvvValid) { isDataValid, isNumberValid, isCvvValid ->
        isDataValid == true && isNumberValid == true && isCvvValid == true && cardDate.value?.isEmpty() == false && cardNumber.value?.isEmpty() == false
                && cardCvv.value?.isEmpty() == false
    }

    val uiState: LiveData<State> = Transformations.map(currentPosition) {
        if (it == null || it == -1) State.ADD else State.PAY
    }

    fun onCardSelected(cardIndex: Int) {
        currentPosition.postValue(if (cardIndex == (cards.value?.size ?: 0)) -1 else cardIndex)
    }

    private fun generateUiCardList(cards: List<Card>, position: Int?): List<UiCard> {
        return cards.mapIndexed { index, card ->
            UiCard(
                title = "**${card.number.takeLast(4)}",
                cardType = getPaymentSystem(card.number.filter { it.isDigit() })?.image,
                icon = R.drawable.ic_ready,
                backGround = if (index == position) R.drawable.bg_select_card else R.drawable.bg_card,
                textColor = if (index == position) R.color.white else R.color.gray
            )
        }
    }

    private fun addAddCardToList(uiCardList: List<UiCard>, position: Int?): List<UiCard> {
        val isAddSelect = position == -1
        val newUiCardList = uiCardList.toMutableList().apply {
            add(
                UiCard(
                    title = "Добавить карту",
                    cardType = null,
                    icon = if (isAddSelect) R.drawable.ic_ready_blue else R.drawable.ic_plus,
                    backGround = R.drawable.bg_card,
                    textColor = R.color.gray
                )
            )
        }
        return newUiCardList
    }

    fun addCard(number: String, date: String) {
        val card = Card(number, date)
        val cards = _cards.value ?: emptyList()
        _cards.value = cards.toMutableList().apply {
            add(0, card)
        }
        currentPosition.postValue(0)
    }

    fun getPaymentSystem(cardNumber: String): CardType? = when {
        cardNumber.matches(MASTERCARD_REGEX) -> CardType.MASTERCARD
        cardNumber.matches(VISA_REGEX) -> CardType.VISA
        cardNumber.matches(MIR_REGEX) -> CardType.MIR
        else -> null
    }

    fun setCardDate(date: String) {
        cardDate.postValue(date)
        _isDataValid.postValue(validCardDate(date))
    }

    fun setCardCvv(cvv: String) {
        cardCvv.postValue(cvv)
        _isCvvValid.postValue(validCvv(cvv))
    }

    fun setCardNumber(cardNumber: String) {
        val extractCardNumber = cardNumber.filter { it.isDigit() }
        this.cardNumber.postValue(extractCardNumber)
        _isNumberValid.postValue(validCardNumber(extractCardNumber))
    }

    fun updateLoad(isLoad: Boolean) {
        _isLoading.postValue(isLoad)
    }

    companion object {
        val MASTERCARD_REGEX = Regex("^5[1-5][0-9]{14}$")
        val VISA_REGEX = Regex("^4[0-9]{12}(?:[0-9]{3})?$")
        val MIR_REGEX = Regex("^2[0-9]{15}$")
    }
}