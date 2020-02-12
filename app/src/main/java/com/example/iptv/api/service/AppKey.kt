package com.example.iptv.api.service

sealed class AppKey {
    class ACTIVITY_KEY() {
        val AUTH_ACT = "AUTH_PHASE"
        val FULLSCREEN_ACT = "FL_ACT"
        val MAIN_ACT = "MAIN_ACT"
        val SEC_ACT = "SECOND_ACT"
        val SPLASH = "SPLASH"
    }
    class FRAGMENT_KEY() {
        val ABOUT_F = "ABOUT_F"
        val CHANG_PASS_F = "CHANGE_PASS"
        val INFO_F = "INFO_FRAGMENT"
        val LOGIN_F = "LOGIN_FRAGMENT"
        val PRODUCT_F = "PRODUCT_FRAGMENT"
        val PRODUCT_DETAIL_F = "PRODUCT_DETAIL"
        val REGISTER_F = "REGISTER_F"
        val SOCIAL_MEDIA_F = "SOSMED_F"
        val STREAMING_F = "STREAMING_F"
    }
}