package com.gorbunov.currencytrade.view.admin

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gorbunov.currencytrade.model.AdminUserResponse
import com.gorbunov.currencytrade.view.LoadingView

@Composable
fun AdminBlockListView(vm: AdminBlockListViewModel) {

    val curState = vm.curState.collectAsState()

    Crossfade(targetState = curState) {
        when (val st = it.value) {
            is AdminBlockListViewModel.State.Data ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    st.data.forEach { aItem ->
                        item {
                            AdminBlockItemView(item = aItem) { userId ->
                                vm.blockUserBy(userId)
                            }
                        }
                    }
                }
            is AdminBlockListViewModel.State.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = st.e.message ?: "Неизвестная ошибка")
                }
            }
            AdminBlockListViewModel.State.Loading -> {
                LoadingView()
            }
        }
    }


    BackHandler(curState.value is AdminBlockListViewModel.State.Error) {
        vm.loadList()
    }
}

@Composable
fun AdminBlockItemView(item: AdminUserResponse, onClick: (Long) -> Unit) {
    val msg =
        if (item.is_active) "Пользователь ${item.username} открыт" else "Пользователь ${item.username} заблокирован"
    Row(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(8.dp, shape = RoundedCornerShape(25.dp))
            .background(
                color = Color.White, shape = RoundedCornerShape(24.dp)
            )
            .padding(top = 8.dp, end = 8.dp, bottom = 8.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            msg,
            fontSize = 16.sp,
            color = Color(0xFF686868),
            textAlign = TextAlign.Center,
            maxLines = 3,
            modifier = Modifier.fillMaxWidth(0.5f)
        )

        Button(onClick = { onClick.invoke(item.id) }, modifier = Modifier.wrapContentWidth()) {
            Text(
                if (item.is_active) "Заблокировать" else "Разблокировать",
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.wrapContentWidth()
            )
        }
    }
}