package com.example.coroutine_toolkit.coroutine

/**
 * @author 浩楠
 * @date 2025/5/21 13:52
 *      _              _           _     _   ____  _             _ _
 *     / \   _ __   __| |_ __ ___ (_) __| | / ___|| |_ _   _  __| (_) ___
 *    / _ \ | '_ \ / _` | '__/ _ \| |/ _` | \___ \| __| | | |/ _` | |/ _ \
 *   / ___ \| | | | (_| | | | (_) | | (_| |  ___) | |_| |_| | (_| | | (_) |
 *  /_/   \_\_| |_|\__,_|_|  \___/|_|\__,_| |____/ \__|\__,_|\__,_|_|\___/
 *  描述: TODO 统一的状态管理
 */

sealed class UiState<out T> {

    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    object Canceled : UiState<Nothing>()

    data class Success<T>(val data: T) : UiState<T>()

    data class Error(val throwable: Throwable) : UiState<Nothing>()


}