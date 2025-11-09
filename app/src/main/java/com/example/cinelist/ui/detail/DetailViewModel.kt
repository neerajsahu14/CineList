package com.example.cinelist.ui.detail

import androidx.lifecycle.ViewModel
import com.example.cinelist.data.repository.MediaRepository
import com.example.cinelist.util.DetailUiState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DetailViewModel(
    private val mediaRepository: MediaRepository,
    private val titleId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    private val disposables = CompositeDisposable()

    init {
        loadTitleDetails()
    }

    fun loadTitleDetails() {
        _uiState.update { it.copy(isLoading = true, error = null) }

        val subscription = mediaRepository.getTitleDetails(titleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { titleDetail ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            titleDetail = titleDetail,
                            error = null
                        )
                    }
                },
                { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "An unexpected error occurred"
                        )
                    }
                }
            )

        disposables.add(subscription)
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
