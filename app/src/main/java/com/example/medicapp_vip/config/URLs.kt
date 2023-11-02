package com.example.medicapp_vip.config

import java.net.URL

class URLs {
    private val mainUrl = "http://192.168.144.66:8080/api/"

    //Analysis
    val getAnalysisUrl = URL(mainUrl+"catalog")
    val postAnalysisUrl = URL(mainUrl+"analysis")

    //Auth
    val postEmailUrl = URL(mainUrl+"signin")
    val postCodeUrl = URL(mainUrl+"confirm")

    //News
    val getNewsUrl = URL(mainUrl+"news")
    val postNewsUrl = URL(mainUrl+"news")

    //Order
    val postOrderUrl = URL(mainUrl+"order")
    val getOrdersUrl = URL(mainUrl+"orders")


    //Profile
    val postProfileUrl = URL(mainUrl+"profile")
    val putProfileUrl = URL(mainUrl+"profile")
    val getProfileUrl = URL(mainUrl+"profile")
    val postProfileImageUrl = URL(mainUrl+"profileIcon")
    val getProfileImageUrl = URL(mainUrl+"profileIcon")

}