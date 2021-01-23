package com.qr

abstract class Callback<T>() {

    abstract fun onSuccess(response: T?)

    abstract fun onFailure(error: T?)
}