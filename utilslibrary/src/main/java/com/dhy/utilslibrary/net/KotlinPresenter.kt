package com.dhy.utilslibrary.net

import com.dhy.utilslibrary.EmptyUtils
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by dhy
 * Date: 2020/10/28
 * Time: 16:06
 * describe:
 */
class KotlinPresenter {

    private val presenterScope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    /**
     * 当页面被销户时协程要取消
     */
    @ExperimentalCoroutinesApi
    fun detachView() {
        presenterScope.cancel()
    }

    fun launchOnUI(block: suspend CoroutineScope.() -> Unit) {
        presenterScope.launch { block() }
    }

    suspend fun <T> launchIO(block: suspend CoroutineScope.() -> T) {
        withContext(Dispatchers.IO) {
            block
        }
    }

    fun launch(tryBlock: suspend CoroutineScope.() -> Unit) {
        launchOnUI {
            tryCatch(tryBlock, {}, {})
        }
    }

    fun launchWithTryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(String?) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        launchOnUI {
            tryCatch(tryBlock, catchBlock, finallyBlock)
        }
    }

    private suspend fun tryCatch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(String?) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                tryBlock()
            } catch (e: Throwable) {
                catchBlock(e.message)
            } finally {
                finallyBlock()
            }
        }
    }

    /**
     * 网络请求
     * @param tryBlock 请求模块
     * @param successBlock 成功回调
     * @param catchBlock 异常错误
     * @param finallyBlock 结束，类似onComplate
     */
    fun <T> launchRequest(
        tryBlock: suspend CoroutineScope.() -> ResultBean<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        catchBlock: suspend CoroutineScope.(String?) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        launchOnUI {
            requestTryCatch(tryBlock, successBlock, catchBlock, finallyBlock)
        }
    }

    /**
     * 网络请求
     * @param tryBlock 请求模块
     * @param successBlock 成功回调
     * @param catchBlock 异常错误
     */
    fun <T> launchRequest(
        tryBlock: suspend CoroutineScope.() -> ResultBean<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        catchBlock: suspend CoroutineScope.(String?) -> Unit
    ) {
        launchRequest(tryBlock, successBlock, catchBlock, {})
    }


    private suspend fun <T> requestTryCatch(
        tryBlock: suspend CoroutineScope.() -> ResultBean<T>?,
        successBlock: suspend CoroutineScope.(T?) -> Unit,
        catchBlock: suspend CoroutineScope.(String?) -> Unit,
        finallyBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                var response = tryBlock()
                callResponse(
                    response,
                    {
                        successBlock(response?.data)
                    },
                    {
                        catchBlock("200|${response?.code}=${response?.msg}")
                    }
                )
            } catch (e: Throwable) {
                var errMsg = ""
                errMsg = when (e) {
                    is UnknownHostException -> {
                        "No network..."
                    }
                    is SocketTimeoutException -> {
                        "Request timeout..."
                    }
                    is NumberFormatException -> {
                        "Request failed, type conversion exception"
                    }
                    else ->
                        e.message.toString()
                }
                catchBlock(errMsg)
            } finally {
                finallyBlock()
            }
        }
    }

    /**
     * 主要用于处理返回的response是否请求成功
     */
    suspend fun <T> callResponse(
        response: ResultBean<T>?, successBlock: suspend CoroutineScope.() -> Unit,
        errorBlock: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            when {
                response == null || EmptyUtils.isEmpty(response) -> errorBlock()
                response.code == 0 -> successBlock()
                else -> errorBlock()
            }
        }
    }
}