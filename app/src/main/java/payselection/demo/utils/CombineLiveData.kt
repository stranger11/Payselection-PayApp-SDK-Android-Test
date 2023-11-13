package payselection.demo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class CombineLiveData<F, S, R>(first: LiveData<F>, second: LiveData<S>, combine: (F?, S?) -> R) :
    MediatorLiveData<R>() {

    init {
        addSource(first) {
            value = combine(it, second.value)
        }
        addSource(second) {
            value = combine(first.value, it)
        }
    }
}

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