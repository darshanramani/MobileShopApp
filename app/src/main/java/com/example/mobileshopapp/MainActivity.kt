package com.example.mobileshopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Product(
    val name: String,
    val price: Double,
    val image: Int
)

data class CartItem(
    val product: Product,
    val quantity: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileShopApp()
        }
    }
}

@Composable
fun MobileShopApp() {

    val products = listOf(
        Product("Nike Shoes", 120.00, R.drawable.nike_shoes),
        Product("Adidas Shoes", 95.50, R.drawable.adidas_shoes),
        Product("T-Shirt", 35.00, R.drawable.tshirt),
        Product("Shorts", 28.99, R.drawable.shorts),
        Product("Cap", 18.50, R.drawable.cap)
    )

    val cartItems = remember { mutableStateListOf<CartItem>() }
    var showCart by remember { mutableStateOf(false) }

    val totalItems = cartItems.sumOf { it.quantity }
    val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {

        Text(
            text = "Mobile Shop",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Shopping Bag",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(text = "Items: $totalItems")

                Text(
                    text = "Total: $${String.format("%.2f", totalPrice)}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { showCart = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("See Cart")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(products) { product ->
                ProductItem(product = product) {

                    val index = cartItems.indexOfFirst {
                        it.product.name == product.name
                    }

                    if (index >= 0) {
                        val oldItem = cartItems[index]
                        cartItems[index] = oldItem.copy(
                            quantity = oldItem.quantity + 1
                        )
                    } else {
                        cartItems.add(CartItem(product, 1))
                    }
                }
            }
        }
    }

    if (showCart) {
        CartDialog(
            cartItems = cartItems,
            onClose = { showCart = false }
        )
    }
}

@Composable
fun ProductItem(
    product: Product,
    onAdd: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 10.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$${String.format("%.2f", product.price)}",
                    fontSize = 14.sp
                )
            }

            Button(onClick = onAdd) {
                Text("Add")
            }
        }
    }
}

@Composable
fun CartDialog(
    cartItems: MutableList<CartItem>,
    onClose: () -> Unit
) {
    val totalPrice = cartItems.sumOf { it.product.price * it.quantity }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text("Your Cart")
        },
        text = {
            if (cartItems.isEmpty()) {
                Text("Your cart is empty.")
            } else {
                Column {

                    cartItems.forEach { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = item.product.name,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )

                            Button(onClick = {
                                val index = cartItems.indexOf(item)
                                if (index >= 0) {
                                    cartItems[index] = item.copy(
                                        quantity = item.quantity + 1
                                    )
                                }
                            }) {
                                Text("+")
                            }

                            Text(
                                text = " ${item.quantity} ",
                                fontWeight = FontWeight.Bold
                            )

                            Button(onClick = {
                                val index = cartItems.indexOf(item)
                                if (index >= 0) {
                                    if (item.quantity > 1) {
                                        cartItems[index] = item.copy(
                                            quantity = item.quantity - 1
                                        )
                                    } else {
                                        cartItems.remove(item)
                                    }
                                }
                            }) {
                                Text("-")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Total: $${String.format("%.2f", totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onClose) {
                Text("Close")
            }
        }
    )
}