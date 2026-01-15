package dev.swabodha.life.features.smoking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SmokingRooms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.smoking.data.entity.SmokingSize
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmokingScreen(
    vm: SmokingViewModel = viewModel()
) {
    val entries by vm.entries.collectAsState()

    var selectedCount by rememberSaveable { mutableStateOf(1) }
    var selectedSize by rememberSaveable { mutableStateOf(SmokingSize.CHOTU) }
    var isMenthol by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            item {
                Column(Modifier.padding(24.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.SmokingRooms,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Smoking log",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Log each smoke mindfully.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        thickness = 1.dp
                    )
                }
            }

            // ===== Log Controls =====
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth()
                ) {

                    // ---- Count ----
                    Text(
                        text = "Count",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        (1..4).forEach { count ->
                            FilterChip(
                                selected = selectedCount == count,
                                onClick = { selectedCount = count },
                                label = {
                                    Text(
                                        text = count.toString(),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // ---- Size ----
                    Text(
                        text = "Size",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    // Row 1: Chotu | King
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SizeChipFullWidth(
                            size = SmokingSize.CHOTU,
                            selectedSize = selectedSize,
                            onSelect = { selectedSize = it },
                            modifier = Modifier.weight(1f)
                        )
                        SizeChipFullWidth(
                            size = SmokingSize.KING,
                            selectedSize = selectedSize,
                            onSelect = { selectedSize = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    // Row 2: Chotu cigar | Mota cigar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SizeChipFullWidth(
                            size = SmokingSize.CHOTU_CIGAR,
                            selectedSize = selectedSize,
                            onSelect = { selectedSize = it },
                            modifier = Modifier.weight(1f)
                        )
                        SizeChipFullWidth(
                            size = SmokingSize.MOTA_CIGAR,
                            selectedSize = selectedSize,
                            onSelect = { selectedSize = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(Modifier.height(24.dp))

                    // ---- Menthol toggle ----
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Menthol cigarette",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Toggle if the cigarette contains menthol",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(Modifier.weight(1f))

                        Switch(
                            checked = isMenthol,
                            onCheckedChange = { isMenthol = it }
                        )
                    }

                    Spacer(Modifier.height(28.dp))

                    Button(
                        onClick = {
                            vm.log(
                                count = selectedCount,
                                size = selectedSize,
                                isMenthol = isMenthol
                            )

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Smoking logged",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Long
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Log")
                    }
                }
            }

            // ===== History =====
            item {
                Spacer(Modifier.height(24.dp))
            }

            items(entries) { entry ->
                Text(
                    text = "${format(entry.timestamp)} → ${entry.count} × ${entry.size} (${if (entry.isMenthol) "menthol" else "regular"})",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SizeChipFullWidth(
    size: SmokingSize,
    selectedSize: SmokingSize,
    onSelect: (SmokingSize) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selectedSize == size,
        onClick = { onSelect(size) },
        label = {
            Text(
                text = size.name
                    .lowercase()
                    .replace("_", " ")
                    .replaceFirstChar { it.uppercase() },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    )
}

private fun format(millis: Long): String =
    SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date(millis))
