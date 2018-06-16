package home.antonyaskiv.hackaton.API

/**
 * Created by AntonYaskiv on 16.06.2018.
 */
abstract class CallBackResponse<T> {
    abstract fun onSuccess(t: T)
    abstract fun onError(code: Int)
    abstract fun onFail()
}