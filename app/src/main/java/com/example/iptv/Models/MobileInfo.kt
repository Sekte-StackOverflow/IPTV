package com.example.iptv.Models

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager

class MobileInfo( private val mContext: Context) {
    val MAC_ADDRESS = myMacAddress()
    val IP_LOCAL = myIpAddress()
    val DEVICE_ID = myDeviceID()
    val DEVICE_MODEL = myDeviceModel()

    companion object {
        fun newIntance(context: Context) = MobileInfo(context)
    }

    @SuppressLint("HardwareIds")
    private fun myMacAddress(): String {
        val wifiManager =
            mContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val macAddress = wifiManager.connectionInfo.macAddress
        return macAddress ?: "Device don't have mac address of wifi is disable"
    }

    private fun myIpAddress(): String {
        val wifiManager =
            mContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo.ipAddress.toString() ?: "Device don't have ip Address"
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun myDeviceID(): String {
        val telephonyManager =
            mContext.applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.deviceId
    }

    private fun myDeviceModel(): String {
        val manufacture = Build.MANUFACTURER
        val model = Build.MODEL
        val version = Build.VERSION.RELEASE
        return "$manufacture $model $version"
    }

}