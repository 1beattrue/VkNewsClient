package edu.mirea.onebeattrue.vknewsclient.ui

fun sum(a: Int, b: Int): Int { // "чистая" функция
    return a + b
}

var result = 0
fun sumWithSideEffects(a: Int, b: Int): Int { // функция с "side effect'ом"
    result = a + b // действие за пределами scope'а своей ответственности
    return result // теперь мы не можем быть уверены в результате
}