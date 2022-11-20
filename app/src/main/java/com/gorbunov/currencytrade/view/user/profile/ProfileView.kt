package com.gorbunov.currencytrade.view.user.profile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorbunov.currencytrade.toNo
import com.gorbunov.currencytrade.toYes
import com.gorbunov.currencytrade.view.LoadingView

@Composable
fun ProfileView(vm: ProfileViewModel) {

    val curState = vm.currentState.collectAsState()

    Crossfade(targetState = curState) {
        when (val st = it.value) {
            is ProfileViewModel.State.Data ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Text(
                            text = "Профиль ${st.data.username}",
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        CardRow(f = "Фамилия", s = st.data.last_name)
                        Spacer(modifier = Modifier.padding(8.dp))
                        CardRow(f = "Имя", s = st.data.first_name)
                        Spacer(modifier = Modifier.padding(8.dp))
                        CardRow(f = "Администратор", s = st.data.is_superuser.toYes())
                        Spacer(modifier = Modifier.padding(8.dp))
                        CardRow(f = "Подтвержденный аккаунт", s = st.data.is_approved.toYes())
                        Spacer(modifier = Modifier.padding(8.dp))
                        CardRow(f = "Заблокирован", s = st.data.is_active.toNo())
                    }

                }
            is ProfileViewModel.State.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = st.e.message ?: "Неизвестная ошибка")
                }
            }
            ProfileViewModel.State.Loading -> {
                LoadingView()
            }
        }
    }
}

@Composable
fun CardRow(f: String, s: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$f:",
            modifier = Modifier.fillMaxWidth(0.45f),
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Text(
            text = s,
            modifier = Modifier.fillMaxWidth(0.45f),
            fontSize = 14.sp,
        )
    }
}