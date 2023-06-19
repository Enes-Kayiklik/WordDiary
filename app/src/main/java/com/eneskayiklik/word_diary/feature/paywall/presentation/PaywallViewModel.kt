package com.eneskayiklik.word_diary.feature.paywall.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.WordDiaryApp
import com.eneskayiklik.word_diary.core.data.Result
import com.eneskayiklik.word_diary.core.util.UiEvent
import com.eneskayiklik.word_diary.feature.paywall.domain.PaywallRepository
import com.eneskayiklik.word_diary.feature.paywall.domain.model.WordDiaryProduct
import com.eneskayiklik.word_diary.util.extensions.findActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaywallViewModel @Inject constructor(
    private val paywallRepository: PaywallRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PaywallState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<UiEvent>()
    val event = _event.asSharedFlow()

    init {
        getProducts()
    }

    fun onEvent(event: UiEvent) = viewModelScope.launch {
        _event.emit(event)
    }

    fun onEvent(event: PaywallEvent) = viewModelScope.launch {
        when (event) {
            PaywallEvent.OnRestore -> restorePurchase()
            is PaywallEvent.OnMakePurchase -> makePurchase(event.context)
            is PaywallEvent.OnSelectProduct -> selectProduct(event.product)
            is PaywallEvent.OnShowDialog -> _state.update { it.copy(dialogType = event.type) }
        }
    }

    private fun selectProduct(product: WordDiaryProduct) = viewModelScope.launch {
        _state.update {
            it.copy(
                selectedProduct = product
            )
        }
    }

    private fun getProducts() = viewModelScope.launch(Dispatchers.IO) {
        paywallRepository.collectProducts().collectLatest { result ->
            when (result) {
                Result.Loading -> _state.update { it.copy(isLoading = true) }
                is Result.Error -> onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            products = result.result,
                            selectedProduct = result.result.firstOrNull()
                        )
                    }
                }
            }
        }
    }

    private fun makePurchase(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        val product = _state.value.selectedProduct ?: return@launch

        val result = try {
            paywallRepository.makePurchase(context.findActivity(), product.adaptyProduct)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

        when (result) {
            is Result.Success -> {
                WordDiaryApp.hasPremium = true
                _state.update { it.copy(dialogType = PaywallDialog.SubscriptionSuccess) }
            }
            else -> Unit
        }
    }

    private fun restorePurchase() = viewModelScope.launch(Dispatchers.IO) {
        val result = try {
            paywallRepository.restorePurchase()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

        when (result) {
            is Result.Success -> {
                WordDiaryApp.hasPremium = true
                _state.update { it.copy(dialogType = PaywallDialog.SubscriptionSuccess) }
            }
            is Result.Error -> onEvent(UiEvent.ShowToast(textRes = R.string.uncaught_error))
            else -> Unit
        }
    }
}