package com.example.coroutine_toolkit.coroutine

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * @author 浩楠
 * @date 2025/5/21 13:55
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 *  描述: TODO 通用任务调度器
 */

class CoroutineTaskManager(private val scope: CoroutineScope) {

    private val taskMap = mutableMapOf<String, Job>()

    fun <T> launchTask(
        taskId: String = UUID.randomUUID().toString(),
        stateFlow: MutableStateFlow<UiState<T>>,
        block: suspend () -> T
    ): TaskHandle {
        if (taskMap[taskId]?.isActive == true) return TaskHandle(taskId)
        val job = scope.launch {
            stateFlow.value = UiState.Loading
            try {
                val result = withContext(Dispatchers.IO) { block() }
                stateFlow.value = UiState.Success(result)
            } catch (e: Exception) {
                stateFlow.value = UiState.Error(e)
            }
        }
        taskMap[taskId] = job
        return TaskHandle(taskId)
    }

    suspend fun <T> awaitResult(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        task: suspend () -> T
    ): UiState<T> = withContext(dispatcher) {
        try {
            UiState.Success(task())
        } catch (e: Exception) {
            UiState.Error(e)
        }
    }



    fun cancel(taskId: String) {
        taskMap[taskId]?.cancel()
        taskMap.remove(taskId)
    }

    fun cancelAll() {
        taskMap.values.forEach { it.cancel() }
        taskMap.clear()
    }
}