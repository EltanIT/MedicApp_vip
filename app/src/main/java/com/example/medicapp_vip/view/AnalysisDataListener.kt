package com.example.medicapp_vip.view

import com.example.medicapp_vip.objects.Analysis

interface AnalysisDataListener {
    fun add(analysis: Analysis)
    fun delete(analysis: Analysis)
}