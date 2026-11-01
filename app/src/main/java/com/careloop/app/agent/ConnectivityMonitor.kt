package com.careloop.app.agent

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.InetSocketAddress
import java.net.Socket

class ConnectivityMonitor(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isNetworkConnected = MutableStateFlow(false)
    val isNetworkConnected: StateFlow<Boolean> = _isNetworkConnected.asStateFlow()

    private val _isInternetReachable = MutableStateFlow(false)
    val isInternetReachable: StateFlow<Boolean> = _isInternetReachable.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isNetworkConnected.value = true
            scope.launch { checkActualInternetReachability() }
        }

        override fun onLost(network: Network) {
            _isNetworkConnected.value = false
            _isInternetReachable.value = false
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val isValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            _isNetworkConnected.value = hasInternet
            if (hasInternet && isValidated) {
                _isInternetReachable.value = true
            } else {
                scope.launch { checkActualInternetReachability() }
            }
        }
    }

    init {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
        // Initial check
        _isNetworkConnected.value = checkHardwareConnected()
        scope.launch { checkActualInternetReachability() }
    }

    private fun checkHardwareConnected(): Boolean {
        val active = connectivityManager.activeNetwork ?: return false
        val caps = connectivityManager.getNetworkCapabilities(active) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    suspend fun checkActualInternetReachability(): Boolean = withContext(Dispatchers.IO) {
        if (!checkHardwareConnected()) {
            _isInternetReachable.value = false
            return@withContext false
        }
        try {
            // Test reachability against a DNS server or standard port 53 / 80
            Socket().use { socket ->
                socket.connect(InetSocketAddress("1.1.1.1", 53), 1500)
            }
            _isInternetReachable.value = true
            true
        } catch (e: Exception) {
            _isInternetReachable.value = false
            false
        }
    }
}

