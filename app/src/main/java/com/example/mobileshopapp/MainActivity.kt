package com.example.mobileshopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Product(
    val name: String,
    val price: Double,
    val image: Int,
    val rating: Double
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TechifyApp()
        }
    }
}

@Composable
fun TechifyApp() {
    var showHome by remember { mutableStateOf(true) }

    if (showHome) {
        StartPage {
            showHome = false
        }
    } else {
        ShopScreen()
    }
}

@Composable
fun StartPage(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF071A2C))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "TECHIFY",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00D4FF)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Smart gadgets for smart people",
            fontSize = 18.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF102A43)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "💻",
                    fontSize = 70.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Explore latest gadgets, add favorites, and checkout easily.",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00D4FF),
                        contentColor = Color.Black
                    )
                ) {
                    Text("Start Shopping")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Designed & Developed by Darshan Ramani",
                    fontSize = 13.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun ShopScreen() {

    var darkMode by remember { mutableStateOf(false) }
    var showCart by remember { mutableStateOf(false) }
    var showOrderPopup by remember { mutableStateOf(false) }

    val favoriteItems = remember { mutableStateListOf<String>() }
    val cartItems = remember { mutableStateListOf<CartItem>() }

    val bgColor = if (darkMode) Color(0xFF071A2C) else Color(0xFFF4F8FB)
    val cardColor = if (darkMode) Color(0xFF102A43) else Color.White
    val textColor = if (darkMode) Color.White else Color(0xFF102A43)
    val subTextColor = if (darkMode) Color(0xFFB0BEC5) else Color.Gray
    val accentColor = Color(0xFF00D4FF)

    val products = listOf(
        Product("AirPods", 149.99, R.drawable.airpod, 4.7),
        Product("Gaming Headset", 89.99, R.drawable.heatset, 4.5),
        Product("Laptop", 999.99, R.drawable.laptop, 4.8),
        Product("Speaker", 129.99, R.drawable.speaker, 4.4),
        Product("Smart Watch", 199.99, R.drawable.watch, 4.6),
        Product("Smart TV", 699.99, R.drawable.tv, 4.9)
    )

    val totalItems = cartItems.sumOf { it.quantity }
    val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

    fun addProduct(product: Product) {
        val index = cartItems.indexOfFirst { it.product.name == product.name }
        if (index >= 0) {
            val oldItem = cartItems[index]
            cartItems[index] = oldItem.copy(quantity = oldItem.quantity + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun removeProduct(product: Product) {
        val index = cartItems.indexOfFirst { it.product.name == product.name }
        if (index >= 0) {
            val oldItem = cartItems[index]
            if (oldItem.quantity > 1) {
                cartItems[index] = oldItem.copy(quantity = oldItem.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Techify",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    text = "Smart gadgets collection",
                    fontSize = 14.sp,
                    color = subTextColor
                )
            }

            Text(
                text = if (darkMode) "Dark" else "Light",
                color = textColor,
                fontSize = 12.sp
            )

            Switch(
                checked = darkMode,
                onCheckedChange = { darkMode = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = accentColor
                )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Cart Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Text(
                    text = "Items: $totalItems",
                    color = subTextColor
                )

                Text(
                    text = "Total: $${String.format("%.2f", totalPrice)}",
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showCart = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("View Cart", color = Color.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(products) { product ->

                val quantity = cartItems
                    .find { it.product.name == product.name }
                    ?.quantity ?: 0

                val isFavorite = favoriteItems.contains(product.name)

                ProductCard(
                    product = product,
                    quantity = quantity,
                    isFavorite = isFavorite,
                    cardColor = cardColor,
                    textColor = textColor,
                    subTextColor = subTextColor,
                    accentColor = accentColor,
                    onAdd = { addProduct(product) },
                    onRemove = { removeProduct(product) },
                    onFavoriteClick = {
                        if (isFavorite) {
                            favoriteItems.remove(product.name)
                        } else {
                            favoriteItems.add(product.name)
                        }
                    }
                )
            }
        }
    }

    if (showCart) {
        CartDialog(
            cartItems = cartItems,
            cardColor = cardColor,
            textColor = textColor,
            subTextColor = subTextColor,
            accentColor = accentColor,
            onClose = { showCart = false },
            onAdd = { product -> addProduct(product) },
            onRemove = { product -> removeProduct(product) },
            onCheckout = {
                showCart = false
                showOrderPopup = true
                cartItems.clear()
            }
        )
    }

    if (showOrderPopup) {
        AlertDialog(
            onDismissRequest = { showOrderPopup = false },
            title = { Text("Order Placed") },
            text = { Text("Your Techify order has been placed successfully!") },
            confirmButton = {
                Button(
                    onClick = { showOrderPopup = false },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("OK", color = Color.Black)
                }
            }
        )
    }
}

@Composable
fun ProductCard(
    product: Product,
    quantity: Int,
    isFavorite: Boolean,
    cardColor: Color,
    textColor: Color,
    subTextColor: Color,
    accentColor: Color,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(275.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onFavoriteClick) {
                    Text(
                        text = if (isFavorite) "♥" else "♡",
                        fontSize = 22.sp,
                        color = if (isFavorite) Color.Red else subTextColor
                    )
                }
            }

            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .height(85.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Text(
                text = "$${String.format("%.2f", product.price)}",
                color = subTextColor,
                fontSize = 13.sp
            )

            Text(
                text = "⭐ ${product.rating}",
                color = accentColor,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (quantity == 0) {
                Button(
                    onClick = onAdd,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Add", color = Color.Black)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(accentColor, RoundedCornerShape(12.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = onRemove) {
                        Text("-", color = Color.Black, fontSize = 20.sp)
                    }

                    Text(
                        text = quantity.toString(),
                        color = Color.Black,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = onAdd) {
                        Text("+", color = Color.Black, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CartDialog(
    cartItems: MutableList<CartItem>,
    cardColor: Color,
    textColor: Color,
    subTextColor: Color,
    accentColor: Color,
    onClose: () -> Unit,
    onAdd: (Product) -> Unit,
    onRemove: (Product) -> Unit,
    onCheckout: () -> Unit
) {
    val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Your Cart") },
        text = {
            if (cartItems.isEmpty()) {
                Text("Your cart is empty.")
            } else {
                Column {

                    cartItems.forEach { item ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = cardColor),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = painterResource(id = item.product.image),
                                    contentDescription = item.product.name,
                                    modifier = Modifier.size(50.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.product.name,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )

                                    Text(
                                        text = "$${String.format("%.2f", item.product.price * item.quantity)}",
                                        color = subTextColor
                                    )

                                    Text(
                                        text = "Qty: ${item.quantity}",
                                        fontSize = 12.sp,
                                        color = subTextColor
                                    )
                                }

                                Button(
                                    onClick = { onRemove(item.product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                                ) {
                                    Text("-", color = Color.Black)
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                Button(
                                    onClick = { onAdd(item.product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                                ) {
                                    Text("+", color = Color.Black)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Total: $${String.format("%.2f", totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text("Checkout", color = Color.Black)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Text("Close", color = Color.Black)
            }
        }
    )
}