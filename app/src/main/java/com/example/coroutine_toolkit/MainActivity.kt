package com.example.coroutine_toolkit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coroutine_toolkit.coroutine.UiState
import com.example.coroutine_toolkit.databinding.ActivityMainBinding
import com.example.coroutine_toolkit.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModels()

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        this.initClick()
        this.initObserver()
    }

    fun initClick() {
        mBinding.btnLoad.setOnClickListener {
            mViewModel.loadData()
        }

        mBinding.btnDeleteTaskId.setOnClickListener {
            mViewModel.cancelLoading()
        }
    }

    fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.resultState.collect { state ->
                    when (state) {
                        is UiState.Loading -> showLoading()
                        is UiState.Success -> {
                            val titles = state.data.joinToString(separator = "/n")
                            mBinding.btnLoad.text = titles
                        }

                        is UiState.Error -> showToast("错误: ${state.throwable.message}")
                        else -> {}
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.currentTaskId.collect { id ->
                    mBinding.btnShowTaskId.text = id ?: "无任务"
                }
            }
        }
    }

    private fun showLoading() {
        mBinding.btnLoad.text = "加载中..."
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}