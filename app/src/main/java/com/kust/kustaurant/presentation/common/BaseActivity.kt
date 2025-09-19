package com.kust.kustaurant.presentation.common

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kust.kustaurant.data.network.bus.ServiceDownBus
import com.kust.kustaurant.presentation.common.notify503.GlobalServiceDownDialog
import com.kust.kustaurant.presentation.common.notify503.ServiceDownDialogStateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DIALOG_TAG = "ServiceDownDialog"

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), GlobalServiceDownDialog.Listener {
    @Inject lateinit var serviceDownBus: ServiceDownBus
    private val stateVM: ServiceDownDialogStateViewModel by viewModels()
    private var serviceDialogShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                serviceDownBus.events.collect { event ->
                    if (!shouldHandleGlobal503() || isFinishing || isDestroyed) return@collect
                    if (!stateVM.shouldHandle(event.seq)) return@collect

                    val msg = event.message ?: "현재 서비스 이용이 원활하지 않습니다.\n잠시 후 다시 시도해 주세요."
                    val fm = supportFragmentManager
                    val existing = fm.findFragmentByTag(DIALOG_TAG) as? GlobalServiceDownDialog
                    if (existing?.isAdded == true && existing.isVisible) {
                        existing.updateMessage(msg)
                    } else {
                        existing?.dismissAllowingStateLoss()
                        fm.executePendingTransactions()
                        GlobalServiceDownDialog.newInstance(msg).show(fm, DIALOG_TAG)
                    }
                }
            }
        }
    }

    override fun onServiceDownDialogDismissed() {
        serviceDialogShowing = false
    }

    protected open fun shouldHandleGlobal503(): Boolean = true
}
