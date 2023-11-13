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
import java.util.Calendar


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

    fun onCardSelected(position: Int) {
        if (position == (cards.value?.size ?: 0)) currentPosition.postValue(-1) else
            currentPosition.postValue(position)
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
        validCardDate(date)
    }

    fun setCardCvv(cvv: String) {
        cardCvv.postValue(cvv)
        validCvv(cvv)
    }

    fun setCardNumber(cardNumber: String) {
        val extractCardNumber = cardNumber.filter { it.isDigit() }
        this.cardNumber.postValue(extractCardNumber)
        validCardNumber(extractCardNumber)
    }


    private fun validCardDate(date: String) {
        val isValid =
            if (date.length == 5 && date.indexOf('/') == 2) {
                val month = date.substring(0, 2).toInt()
                val year = date.substring(3).toInt()
                val allowedDate = Calendar.getInstance()
                allowedDate.set(2022, 1, 1)
                val inputDate = Calendar.getInstance()
                inputDate.set(2000 + year, month, 1)

                month in 1..12 && inputDate >= allowedDate
            } else {
                false
            }
        _isDataValid.postValue(isValid || date.isEmpty())
    }

    private fun validCvv(cvv: String) {
        _isCvvValid.postValue(cvv.length == 3 || cvv.isEmpty())
    }

    private fun validCardNumber(cardNumber: String) {
        var sum = 0
        for ((index, char) in cardNumber.withIndex()) {
            var digit = Character.digit(char, 10)
            if (index % 2 == 0) {
                digit *= 2
                if (digit > 9) {
                    digit -= 9
                }
            }
            sum += digit
        }
        val isNumberValid = sum % 10 == 0 && cardNumber.length == 16 || cardNumber.isEmpty()
        _isNumberValid.postValue(isNumberValid)
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