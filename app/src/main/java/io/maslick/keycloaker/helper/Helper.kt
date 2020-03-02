package io.maslick.keycloaker.helper

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.migcomponents.migbase64.Base64
import io.maslick.keycloaker.di.KeycloakToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

object AsyncHelper {
    @SuppressLint("CheckResult")
    fun <T> asyncRxExecutor(heavyFunction: () -> T, response : (response : T?) -> Unit) {
        val observable = Single.create<T> { e ->
            e.onSuccess(heavyFunction())
        }
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { t: T? ->
                response(t)
            }
    }

    inline fun uiThreadExecutor(crossinline block: () -> Unit) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post{
            block()
        }
    }
}

object Helper {
    fun isTokenExpired(token: KeycloakToken?): Boolean {
        token?.apply {
            if (tokenExpirationDate == null) return true
            return Calendar.getInstance().after(tokenExpirationDate)
        }
        return true
    }

    fun isRefreshTokenExpired(token: KeycloakToken?): Boolean {
        token?.apply {
            if (refreshTokenExpirationDate == null) return true
            return Calendar.getInstance().after(refreshTokenExpirationDate)
        }
        return true
    }

    @SuppressLint("SimpleDateFormat")
    fun Calendar.formatDate(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        return formatter.format(this.time)
    }

    fun parseJwtToken(jwtToken: String?): Principal {
        jwtToken ?: return Principal()
        jwtToken.apply {
            val splitString = split(".")
            val base64EncodedBody = splitString[1]

            val body = String(Base64.decodeFast(base64EncodedBody))
            val jsonBody = Gson().fromJson(body, JsonObject::class.java)

            val id = jsonBody.get("id")?.asString
            val name = jsonBody.get("name")?.asString ?: "n/a"
            val email = jsonBody.get("email")?.asString ?: "n/a"
            val username = jsonBody.get("username")?.asString ?: "n/a"
            val avatar = jsonBody.get("avatar")?.asString ?: "n/a"
            val address = jsonBody.get("address")?.asString ?: "n/a"
            val dob = jsonBody.get("dob")?.asString ?: "n/a"
            val time_zone = jsonBody.get("time_zone")?.asString ?: "n/a"
            val website_url = jsonBody.get("website_url")?.asString ?: "n/a"
            val person_id = jsonBody.get("person_id")?.asString ?: "n/a"
            val gender = jsonBody.get("gender")?.asString ?: "n/a"

            return Principal(id, email, name, username, avatar, address, dob, time_zone, website_url, person_id, gender)
        }
    }
}

data class Principal(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val username: String? = null,
    val avatar: String? = null,
    val address: String? = null,
    val dob: String? = null,
    val time_zone: String? = null,
    val website_url: String? = null,
    val person_id:  String? = null,
    val gender: String? = null
)