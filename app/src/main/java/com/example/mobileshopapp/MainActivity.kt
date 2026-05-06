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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Navy = Color(0xFF102A43)
val Teal = Color(0xFF00A896)
val Cream = Color(0xFFFFF8E7)
val SoftGray = Color(0xFFF1F5F9)

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
    var showHomePage by remember { mutableStateOf(true) }

    if (showHomePage) {
        StartHomePage {
            showHomePage = false
        }
    } else {
        ShopScreen()
    }
}

@Composable
fun StartHomePage(onStartClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "OneShop",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Navy
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Smart shopping made simple",
            fontSize = 18.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "🛍️",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Trendy products, easy cart, quick checkout.",
                    fontSize = 16.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onStartClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Teal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Explore Products")
                }
            }
        }
    }
}

@Composable
fun ShopScreen() {

    val products = listOf(
        Product("Nike Shoes", 120.00, R.drawable.nike_shoes),
        Product("Adidas Shoes", 95.50, R.drawable.adidas_shoes),
        Product("T-Shirt", 35.00, R.drawable.tshirt),
        Product("Shorts", 28.99, R.drawable.shorts),
        Product("Cap", 18.50, R.drawable.cap)
    )

    val cartItems = remember { mutableStateListOf<CartItem>() }
    var showCart by remember { mutableStateOf(false) }
    var showOrderPopup by remember { mutableStateOf(false) }

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
            .background(SoftGray)
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "OneShop",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Navy
                )

                Text(
                    text = "Fresh picks for you",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = { showCart = true },
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text("Cart $totalItems")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Navy)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Shopping Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Items: $totalItems",
                    color = Color.White
                )

                Text(
                    text = "Total: $${String.format("%.2f", totalPrice)}",
                    fontWeight = FontWeight.Bold,
                    color = Teal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { showCart = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                ) {
                    Text("View Cart")
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

                ProductItem(
                    product = product,
                    quantity = quantity,
                    onAdd = { addProduct(product) },
                    onRemove = { removeProduct(product) }
                )
            }
        }
    }

    if (showCart) {
        CartDialog(
            cartItems = cartItems,
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
            text = { Text("Your order has been placed successfully!") },
            confirmButton = {
                Button(
                    onClick = { showOrderPopup = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun ProductItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                modifier = Modifier
                    .height(95.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Navy
            )

            Text(
                text = "$${String.format("%.2f", product.price)}",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (quantity == 0) {
                Button(
                    onClick = onAdd,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                ) {
                    Text("Add")
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .background(Teal),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = onRemove) {
                        Text("-", color = Color.White, fontSize = 22.sp)
                    }

                    Text(
                        text = quantity.toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    TextButton(onClick = onAdd) {
                        Text("+", color = Color.White, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CartDialog(
    cartItems: MutableList<CartItem>,
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
                            colors = CardDefaults.cardColors(containerColor = SoftGray)
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
                                        color = Navy
                                    )

                                    Text(
                                        text = "$${String.format("%.2f", item.product.price * item.quantity)}",
                                        color = Color.Gray
                                    )

                                    Text(
                                        text = "Qty: ${item.quantity}",
                                        fontSize = 12.sp
                                    )
                                }

                                Button(
                                    onClick = { onRemove(item.product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                                ) {
                                    Text("-")
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                Button(
                                    onClick = { onAdd(item.product) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Teal)
                                ) {
                                    Text("+")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Total: $${String.format("%.2f", totalPrice)}",
                        fontWeight = FontWeight.Bold,
                        color = Teal
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = onCheckout,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Teal)
                    ) {
                        Text("Checkout")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                Text("Close")
            }
        }
    )
}