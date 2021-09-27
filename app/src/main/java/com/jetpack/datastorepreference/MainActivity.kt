package com.jetpack.datastorepreference

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.datastorepreference.repository.DataStorePreferenceRepository
import com.jetpack.datastorepreference.ui.theme.DataStorePreferenceTheme
import com.jetpack.datastorepreference.ui.theme.Purple500
import com.jetpack.datastorepreference.viewmodel.DataStoreViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataStorePreferenceTheme {
                Surface(color = MaterialTheme.colors.background) {
                    DataStoreInput()
                }
            }
        }
    }
}

@Composable
fun DataStoreInput(
    viewModel: DataStoreViewModel = viewModel(
        factory = DataStoreViewModelFactory(DataStorePreferenceRepository(LocalContext.current))
    )
) {
    val context = LocalContext.current
    val dataStorePreferenceRepository: DataStorePreferenceRepository = DataStorePreferenceRepository.getInstance(context)
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val getUserName = viewModel.userName.observeAsState().value
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Purple500)
                .height(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "DataStore Preference",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = textState.value,
                onValueChange = { textState.value = it },
                label = { Text(text = "Enter User Name", fontSize = 15.sp) },
                modifier = Modifier.fillMaxWidth(0.7f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        viewModel.saveUserName(textState.value.text)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                ),
                modifier = Modifier.padding(5.dp)
            ) {
                Text(
                    text = "Submit",
                    modifier = Modifier.padding(6.dp)
                )
            }

            //Showing the datastore value
            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = getUserName!!,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 20.sp
            )
        }
    }
}

class DataStoreViewModelFactory(private val dataStorePreferenceRepository: DataStorePreferenceRepository) :
    ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DataStoreViewModel::class.java)) {
                return DataStoreViewModel(dataStorePreferenceRepository) as T
            }
            throw IllegalStateException()
        }
    }

















