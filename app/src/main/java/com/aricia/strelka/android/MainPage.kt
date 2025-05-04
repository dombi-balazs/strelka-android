package com.aricia.strelka.android

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aricia.strelka.android.DataBase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock.System
import java.util.UUID

@Composable
fun Foods(onFoodSelected: (FoodItem) -> Unit, refreshTrigger: Int) {
    var foods by remember { mutableStateOf<List<FoodItem>>(listOf()) }
    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(refreshTrigger) {
        Log.d("Foods", "Fetching food list, trigger: $refreshTrigger")
        try {
            withContext(Dispatchers.IO) {
                foods = supabase.from("food_items")
                    .select().decodeList<FoodItem>()
            }
        }
        catch (e: Exception) {Log.e("MainPage", "Food list error: ${e.message}\", e")}
    }

    Box {
        IconButton(onClick = { expanded = !expanded }, modifier = Modifier.padding(20.dp)) {
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            foods.forEach { food ->
                DropdownMenuItem(

                    text = {
                        Text("${food.name}, ${food.calories_per_unit} kcal / ${food.unit}")
                    },
                    onClick = { onFoodSelected(food)
                        expanded = false}
                )
            }

        }
    }
}


@Composable
fun MainPage(navController: NavController, onLogOutSuccess: () -> Unit){
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var foodName by remember {mutableStateOf("")}
    var foodEnergy by remember {mutableStateOf("")}
    var foodUnit by remember {mutableStateOf("")}
    val context = LocalContext.current
    var foodListVersion by remember { mutableIntStateOf(0) }

    Column {
        Button(
            modifier = Modifier.padding(20.dp)
                .verticalScroll(rememberScrollState()),
            onClick = { navController.navigate(Screens.Authentication.route) }
        )
        { Text("back (for testing purpose)") }
        Foods(onFoodSelected = { food ->
            selectedFood = food
        },
            refreshTrigger = foodListVersion)
        selectedFood?.let { food ->
            Text(
                text = "Chosen: ${food.name}, ${food.calories_per_unit} kcal / ${food.unit}"
            )
        }
        Button(modifier = Modifier.padding(20.dp), onClick = {
            coroutineScope.launch {
                try {
                    supabase.auth.signOut()
                    onLogOutSuccess()
                }
                catch (e: Exception){
                    Log.e("MainPage", "User log out error: ${e.message}\", e")
                }
            }
        }){Text("Log out")}

        Spacer(modifier = Modifier.height(60.dp))

        Text("Add new food item:", style = androidx.compose.ui.text.TextStyle(fontSize = 18.sp), modifier = Modifier.padding(20.dp))

        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Food name") },
            modifier = Modifier.padding(20.dp)
        )

        OutlinedTextField(
            value = foodEnergy,
            onValueChange = { foodEnergy = it },
            label = { Text("Calories per unit") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(20.dp)
        )

        OutlinedTextField(
            value = foodUnit,
            onValueChange = { foodUnit = it },
            label = { Text("Unit (e.g., g, kg, pcs, ml)") },
            modifier = Modifier.padding(20.dp)
        )

        Button(modifier = Modifier.padding(20.dp), onClick = {
            coroutineScope.launch {
                val name = foodName.trim()
                val energyString = foodEnergy.trim()
                val unit = foodUnit.trim()

                if (name.isEmpty() || energyString.isEmpty() || unit.isEmpty()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val energy = try {
                    energyString.toDouble()
                } catch (e: NumberFormatException) {
                    Log.e("MainPage", "An unexpected error occurred: ${e.message}", e)
                    Toast.makeText(context, "Invalid calorie value", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val currentUser = supabase.auth.currentUserOrNull()
                if (currentUser == null) {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                val newFoodItem = FoodItem(
                    id = UUID.randomUUID().toString(),
                    user_id = currentUser.id,
                    name = name,
                    unit = unit,
                    calories_per_unit = energy,
                    created_at = System.now().toString()
                )

                try {
                    supabase.from("food_items").insert(newFoodItem)
                    Toast.makeText(context, "Food item added successfully!", Toast.LENGTH_SHORT).show()
                    foodName = ""
                    foodEnergy = ""
                    foodUnit = ""
                    foodListVersion++
                } catch (e: Exception) {
                    Log.e("MainPage", "Error adding food item: ${e.message}", e)
                    Toast.makeText(context, "Error adding food item: ${e.message}", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("MainPage", "An unexpected error occurred: ${e.message}", e)
                    Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            Text("Add food item")
        }
    }
}