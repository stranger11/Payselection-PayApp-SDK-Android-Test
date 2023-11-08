package payselection.demo.ui.checkout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import payselection.demo.R
import payselection.demo.models.Card
import payselection.demo.models.Product
import payselection.demo.models.UiCard
import payselection.demo.ui.checkout.common.CardType
import payselection.demo.ui.checkout.common.State
import payselection.demo.utils.CombineLiveData
import payselection.demo.utils.CombineTripleLiveData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class CheckoutViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    var currentPosition = MutableLiveData<Int>(-1)

    val uiCards = CombineLiveData(cards, currentPosition) { cards, position ->
        val cardList = generateUiCardList(cards.orEmpty(), position)
        addAddCardToList(cardList, position)
    }

    private val _isDataValid = MutableLiveData<Boolean>()
    val isDataValid: LiveData<Boolean> = _isDataValid

    private val _isNumberValid = MutableLiveData<Boolean>()
    val isNumberValid: LiveData<Boolean> = _isNumberValid

    private val _isCvvValid = MutableLiveData<Boolean>()
    val isCvvValid: LiveData<Boolean> = _isCvvValid

    val _cardDate = MutableLiveData<String>()
    val cardNumber = MutableLiveData<String>()
    val cardCvv = MutableLiveData<String>()

    val isEnable = CombineTripleLiveData(_isDataValid, _isNumberValid, _isCvvValid) { isDataValid, isNumberValid, isCvvValid ->
        isDataValid == true && isNumberValid == true && isCvvValid == true && _cardDate.value?.isEmpty() == false && cardNumber.value?.isEmpty() == false
                && cardCvv.value?.isEmpty() == false
    }

    val uiState: LiveData<State> = Transformations.map(currentPosition) {
        if (it == (cards.value?.size ?: 0) || it == -1) State.ADD else State.PAY
    }

    init {
        _products.postValue(getProducts())
    }

    fun onCardSelected(position: Int) {
        currentPosition.postValue(position)
    }

    private fun generateUiCardList(cards: List<Card>, position: Int?): List<UiCard> {
        return cards.mapIndexed { index, card ->
            UiCard(
                title = "**${card.number.takeLast(4)}",
                cardType = getPaymentSystem(card.number)?.image,
                icon = if (index == position) R.drawable.ic_ready else R.drawable.ic_ready_blue,
                backGround = if (index == position) R.drawable.bg_select_card else R.drawable.bg_card,
                textColor = if (index == position) R.color.white else R.color.gray
            )
        }
    }

    private fun addAddCardToList(uiCardList: List<UiCard>, position: Int?): List<UiCard> {
        val isAddSelect = position == uiCardList.size
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
        var card = Card(number, date)
        val cards = _cards.value ?: emptyList()
        _cards.value = cards + card
    }

    fun getPaymentSystem(cardNumber: String): CardType? {
        val extractCardNumber = cardNumber.filter { it.isDigit() }

        return when {
            extractCardNumber.matches("^5[1-5][0-9]{14}$".toRegex()) -> CardType.MASTERCARD
            extractCardNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$".toRegex()) -> CardType.VISA
            extractCardNumber.matches("^2[0-9]{15}$".toRegex()) -> CardType.MIR
            else -> null
        }
    }

    private fun getProducts(): List<Product> {
        return listOf(
            Product(name = "Белый пончик", description = "Металл, 13 см", price = 99, image = R.drawable.image_card_1),
            Product(name = "Черная сфера", description = "Гранит, 10 см", price = 49, image = R.drawable.image_card_2),
            Product(name = "Стеклянный куб", description = "Стекло, 20 см", price = 149, image = R.drawable.image_card_3)
        )
    }

    fun setCardDate(date: String) {
        _cardDate.postValue(date)
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun validCardDate(date: String) {
        val isValid = try {
            if (date.length == 5 && date.indexOf('/') == 2) {
                val allowedDate = LocalDate.parse("01/01/22", DateTimeFormatter.ofPattern("dd/MM/yy"))
                val inputDate = LocalDate.parse("01/$date", DateTimeFormatter.ofPattern("dd/MM/yy"))
                allowedDate.isEqual(inputDate) || allowedDate.isBefore(inputDate)
            } else {
                false
            }
        } catch (e: DateTimeParseException) {
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
}