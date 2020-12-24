package com.example.movieapp_mvvm.firebase

import com.google.firebase.iid.FirebaseInstanceIdService

class FirebaseInstanceId: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
    }
}