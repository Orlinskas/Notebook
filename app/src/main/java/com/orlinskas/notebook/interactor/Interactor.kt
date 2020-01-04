package com.orlinskas.notebook.interactor

interface Interactor {
    fun execute()
    fun <T> callBack(): Any<>
}