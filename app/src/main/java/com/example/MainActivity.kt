package com.example

import Przelewy24Delegate
import androidx.activity.ComponentActivity
import pl.przelewy24.p24lib.transfer.request.TrnRequestParams
import java.util.UUID

class MainActivity : ComponentActivity() {

    val przelewy24Delegate = Przelewy24Delegate(this)

    override fun onResume() {
        super.onResume()
        przelewy24Delegate.startPrzelewy24(
            TrnRequestParams.create(UUID.randomUUID().toString()).setSandbox(true)
        )
    }
}