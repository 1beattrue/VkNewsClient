package edu.mirea.onebeattrue.vknewsclient.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.os.Handler

@Composable
fun SideEffectTest(number: MyNumber) {
    Column {
        LazyColumn {
            items(10) {
                Text(text = "${number.a}")
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Handler().postDelayed({ // рекомпозиции не происходит, хотя без задержки выводит все 5
            number.a = 5
        }, 3000)
        // магия?

        LazyColumn {
            items(10) {
                Text(text = "${number.a}")
            }
        }
    }
}

data class MyNumber(var a: Int)