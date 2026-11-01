package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.Pharmacy
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.components.OfflineStatusBanner
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyFinderScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val isOnline by app.connectivityMonitor.isInternetReachable.collectAsState()

    var pharmacies by remember { mutableStateOf<List<Pharmacy>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    fun refreshPharmacies() {
        coroutineScope.launch {
            isLoading = true
            // Default reference coordinate (Paris center 48.8566, 2.3522 or user GPS)
            val list = app.pharmacyFinder.getNearbyPharmacies(
                latitude = 48.8566,
                longitude = 2.3522,
                radiusMeters = 2000,
                isOnline = isOnline
            )
            pharmacies = list
            isLoading = false
        }
    }

    LaunchedEffect(isOnline) {
        refreshPharmacies()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nearby Pharmacies & Prices", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { refreshPharmacies() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OfflineStatusBanner(isOnline = isOnline)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Estimated price benchmark table (as per §9)
                    PriceBenchmarkCard()
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Closest Pharmacies",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        }
                    }
                }

                if (pharmacies.isEmpty() && !isLoading) {
                    item {
                        Text("No pharmacies found. Check connectivity or tap refresh.")
                    }
                } else {
                    items(pharmacies, key = { it.id }) { pharmacy ->
                        PharmacyItemCard(pharmacy)
                    }
                }
            }
        }
    }
}

@Composable
fun PriceBenchmarkCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PriceCheck, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Estimated Drug Price Benchmarks",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Official French benchmark tariffs (Sécurité Sociale / AMO). Prices are estimated.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            PriceRow("Kardegic 75mg (30 sachets)", "~2.45 € (65% covered)")
            PriceRow("Doliprane 1000mg (8 tablets)", "~1.16 € (65% covered)")
            PriceRow("Tahor 20mg (30 tablets)", "~8.72 € (65% covered)")
        }
    }
}

@Composable
fun PriceRow(med: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = med, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Text(text = price, fontWeight = FontWeight.Bold, color = HighContrastGreen, fontSize = 14.sp)
    }
}

@Composable
fun PharmacyItemCard(pharmacy: Pharmacy) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pharmacy.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = pharmacy.address, style = MaterialTheme.typography.bodyMedium)
                pharmacy.phone?.let {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "Tel: $it", style = MaterialTheme.typography.bodyMedium, color = PrimaryBlue)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                pharmacy.distanceMeters?.let {
                    Text(
                        text = "${it}m",
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue,
                        fontSize = 16.sp
                    )
                }
                if (pharmacy.isCached) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = "Cached",
                            fontSize = 11.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

