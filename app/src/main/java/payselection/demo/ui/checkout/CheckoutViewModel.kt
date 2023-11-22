package payselection.demo.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import payselection.demo.models.Card
import payselection.demo.ui.checkout.common.ActionState
import payselection.demo.utils.ADD_ITEM_INDEX
import payselection.demo.utils.CARD_CVV_LENGTH
import payselection.demo.utils.CARD_DATE_LENGTH
import payselection.demo.utils.CARD_NUMBER_LENGTH
import payselection.demo.utils.CombineTripleLiveData
import payselection.demo.utils.isValidCardDate
import payselection.demo.utils.isValidCardNumber
import payselection.demo.utils.isValidCvv

class CheckoutViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    var currentPosition = MutableLiveData<Pair<Int?, Boolean>>(Pair(null, false))

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNumberValid = MutableLiveData<Boolean>()
    val isNumberValid: LiveData<Boolean> = _isNumberValid

    private val _isDataValid = MutableLiveData<Boolean>()
    val isDataValid: LiveData<Boolean> = _isDataValid

    private val _isCvvValid = MutableLiveData<Boolean>()
    val isCvvValid: LiveData<Boolean> = _isCvvValid

    var cardDate = ""
    var cardNumber = ""
    var cardCvv = ""

    private val _checkoutButtonEnable = MutableLiveData<Boolean>()
    val checkoutButtonEnable: LiveData<Boolean> = _checkoutButtonEnable

    val isEnable = CombineTripleLiveData(_isDataValid, _isNumberValid, _isCvvValid) { isDataValid, isNumberValid, isCvvValid ->
        isDataValid == true && isNumberValid == true && isCvvValid == true && cardDate.length == CARD_DATE_LENGTH && cardNumber.length == CARD_NUMBER_LENGTH
                && cardCvv.length == CARD_CVV_LENGTH
    }

    val actionState: LiveData<ActionState> = Transformations.map(currentPosition) {
        if (it.first == null || it.first == ADD_ITEM_INDEX) ActionState.ADD else ActionState.PAY
    }

    fun onCardSelected(cardIndex: Int) {
        currentPosition.postValue(
            Pair(
                if (cardIndex == (cards.value?.size ?: 0)) ADD_ITEM_INDEX else cardIndex,
                false
            )
        )
    }

    fun addCard(number: String, date: String) {
        val card = Card(number, date)
        val cards = _cards.value ?: emptyList()
        _cards.value = cards.toMutableList().apply { add(card) }
        currentPosition.postValue(Pair(cards.size, true))
    }

    fun putCardDate(date: String) {
        this.cardDate = date
        validCardDate()
    }

    fun putCardCvv(cvv: String) {
        cardCvv = cvv
    }

    fun putCardNumber(cardNumber: String) {
        this.cardNumber = cardNumber
        validCardNumber()
    }

    fun validCardNumber(hasFocus: Boolean = true) {
        if (hasFocus) {
            if (cardNumber.length == CARD_NUMBER_LENGTH) {
                _isNumberValid.postValue(isValidCardNumber(cardNumber))
            } else {
                _isNumberValid.postValue(true)
            }
        } else {
            _isNumberValid.postValue(isValidCardNumber(cardNumber))
        }
    }

    fun validCardDate(hasFocus: Boolean = true) {
        if (hasFocus) {
            if (cardDate.length == CARD_DATE_LENGTH) {
                _isDataValid.postValue(isValidCardDate(cardDate))
            } else {
                _isDataValid.postValue(true)
            }
        } else {
            _isDataValid.postValue(isValidCardDate(cardDate))
        }
    }

    fun validCardCvv(hasFocus: Boolean = true) {
        if (hasFocus) {
            _isCvvValid.postValue(true)
        } else {
            _isCvvValid.postValue(isValidCvv(cardCvv))
        }
    }

    fun replaceCard(newCard: Card) {
        val cards = _cards.value?.toMutableList() ?: return
        val position = currentPosition.value?.first ?: return

        cards[position] = newCard
        _cards.postValue(cards)
    }

    fun updateLoad(isLoad: Boolean) {
        _isLoading.postValue(isLoad)
    }

    fun updateCheckoutButtonEnable(isEnable: Boolean) {
        _checkoutButtonEnable.postValue(isEnable)
    }
}