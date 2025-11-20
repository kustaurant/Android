package com.kust.kustaurant.presentation.common

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kust.kustaurant.domain.common.appEvent.AppEvents
import com.kust.kustaurant.presentation.common.notify503.GlobalServiceDownDialog
import com.kust.kustaurant.presentation.common.notify503.StateVM
import com.kust.kustaurant.presentation.ui.splash.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

const val DIALOG_TAG = "ServiceDownDialog"

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), GlobalServiceDownDialog.Listener {
    @Inject lateinit var appEvents: AppEvents
    private val stateVM: StateVM by viewModels()
    private var serviceDialogShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appEvents.serviceDown.collect { event ->
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appEvents.session.collect { event ->
                    if (!shouldHandleSessionEvents()) return@collect
                    if (isFinishing || isDestroyed) return@collect
                    if (!stateVM.shouldHandle(event.seq)) return@collect
                    navigateToLoginAndClearTask()
                }
            }
        }
    }

    private fun navigateToLoginAndClearTask() {
        if (isFinishing || isDestroyed) return
        startActivity(Intent(this, StartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onServiceDownDialogDismissed() {
        serviceDialogShowing = false
    }

    protected open fun shouldHandleGlobal503(): Boolean = true

    protected open fun shouldHandleSessionEvents(): Boolean = true
}