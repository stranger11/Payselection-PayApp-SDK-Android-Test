package payselection.demo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombineTripleLiveData<F, S, T, R>(first: LiveData<F>, second: LiveData<S>, third: LiveData<T>, combine: (F?, S?, T?) -> R) : MediatorLiveData<R>() {

    init {
        addSource(first) {
            value = combine(it, second.value, third.value)
        }
        addSource(second) {
            value = combine(first.value, it, third.value)
        }
        addSource(third) {
            value = combine(first.value, second.value, it)
        }
    }
}