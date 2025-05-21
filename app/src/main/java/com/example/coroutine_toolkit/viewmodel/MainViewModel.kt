package com.example.coroutine_toolkit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutine_toolkit.coroutine.CoroutineTaskManager
import com.example.coroutine_toolkit.coroutine.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author 浩楠
 * @date 2025/5/21 14:06
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 *  描述: TODO
 */
class MainViewModel:ViewModel() {
    val resultState = MutableStateFlow<UiState<MutableList<String>>>(UiState.Idle)
    private val taskManager = CoroutineTaskManager(viewModelScope)

    private val _currentTaskId = MutableStateFlow<String?>(null)
    val currentTaskId: StateFlow<String?> = _currentTaskId

    fun loadData(){
        val taskId = "loadData_${System.currentTimeMillis()}"
        _currentTaskId.value = taskId
        taskManager.launchTask(taskId = taskId, stateFlow = resultState) {
            val url = URL("https://www.wanandroid.com/banner/json")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            try {
                val json = withContext(Dispatchers.IO) {
                    connection.inputStream.bufferedReader().use { it.readText() }
                }
                val titleList = mutableListOf<String>()
                val regex = "\"title\":\"(.*?)\"".toRegex()
                val matches = regex.findAll(json)
                for (match in matches) {
                    titleList.add(match.groupValues[1])
                }
                return@launchTask titleList
            } finally {
                connection.disconnect()
            }
        }
    }

    fun cancelLoading(){
        val id = _currentTaskId.value ?: return
        taskManager.cancel(id)
        _currentTaskId.value = null
        resultState.value = UiState.Canceled
    }
}