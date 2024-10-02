package com.kust.kustaurant

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallException
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallErrorCode
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.android.play.core.ktx.startUpdateFlowForResult
import kotlinx.coroutines.launch

class AppUpdateManager(private val activity: Activity) {
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(activity)
    private val MY_REQUEST_CODE = 100

    fun checkForUpdates() {
        (activity as LifecycleOwner).lifecycleScope.launch {
            try {
                val appUpdateInfo = appUpdateManager.requestAppUpdateInfo()
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        MY_REQUEST_CODE)
                }
            } catch (e: Exception) {
                if (e is InstallException && e.errorCode == InstallErrorCode.ERROR_INSTALL_NOT_ALLOWED) {
                    Toast.makeText(activity, "업데이트를 진행할 수 없습니다. 장치 상태를 확인해주세요 (예: 배터리, 저장 공간)", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(activity, "업데이트 중 예기치 않은 오류가 발생했습니다: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}