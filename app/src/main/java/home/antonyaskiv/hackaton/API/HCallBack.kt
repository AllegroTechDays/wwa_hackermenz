package home.antonyaskiv.hackaton.API

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by AntonYaskiv on 16.06.2018.
 */
class HCallBack<T>(var m: CallBackResponse<T>) : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        m.onFail();
    }

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if (response != null) {
            if (response.body() != null) {
                m.onSuccess(response.body()!!)
            } else {
                m.onError(response.code())
            }
        } else
            m.onFail()
    }
}