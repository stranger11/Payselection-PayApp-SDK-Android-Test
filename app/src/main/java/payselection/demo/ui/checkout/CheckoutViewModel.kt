package payselection.demo.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import payselection.demo.models.Card
import payselection.demo.ui.checkout.common.ActionState
import payselection.demo.utils.ADD_ITEM_INDEX
import payselection.demo.utils.CombineTripleLiveData
import payselection.demo.utils.validCardDate
import payselection.demo.utils.validCardNumber
import payselection.demo.utils.validCvv


class CheckoutViewModel : ViewModel() {

    private val _cards = MutableLiveData<List<Card>>()
    val cards: LiveData<List<Card>> = _cards

    var currentPosition = MutableLiveData<Int?>(null)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isNumberValid = MutableLiveData<Boolean>()
    val isNumberValid: LiveData<Boolean> = _isNumberValid

    private val _isDataValid = MutableLiveData<Boolean>()
    val isDataValid: LiveData<Boolean> = _isDataValid

    private val _isCvvValid = MutableLiveData<Boolean>()
    val isCvvValid: LiveData<Boolean> = _isCvvValid

    val cardDate = MutableLiveData<String>()
    val cardNumber = MutableLiveData<String>()
    val cardCvv = MutableLiveData<String>()

    private val _checkoutButtonEnable = MutableLiveData<Boolean>()
    val checkoutButtonEnable: LiveData<Boolean> = _checkoutButtonEnable

    val isEnable = CombineTripleLiveData(_isDataValid, _isNumberValid, _isCvvValid) { isDataValid, isNumberValid, isCvvValid ->
        isDataValid == true && isNumberValid == true && isCvvValid == true && cardDate.value?.isEmpty() == false && cardNumber.value?.isEmpty() == false
                && cardCvv.value?.isEmpty() == false
    }

    val actionState: LiveData<ActionState> = Transformations.map(currentPosition) {
        if (it == null || it == ADD_ITEM_INDEX) ActionState.ADD else ActionState.PAY
    }

    fun onCardSelected(cardIndex: Int) {
        currentPosition.postValue(if (cardIndex == (cards.value?.size ?: 0)) ADD_ITEM_INDEX else cardIndex)
    }

    fun addCard(number: String, date: String) {
        val card = Card(number, date)
        val cards = _cards.value ?: emptyList()
        _cards.value = cards.toMutableList().apply { add(card) }
        currentPosition.postValue(cards.size)
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

    fun updateCheckoutButtonEnable(isEnable: Boolean) {
        _checkoutButtonEnable.postValue(isEnable)
    }
}