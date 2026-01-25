package dev.swabodha.life.features.smoking.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.SmokingRooms
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.smoking.data.entity.SmokingEntryEntity
import dev.swabodha.life.features.smoking.data.entity.SmokingSize
import dev.swabodha.life.ui.components.FeatureHeader
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
                FeatureHeader(
                    title = "Smoking log",
                    description = "Log each smoke mindfully.",
                    icon = Icons.Outlined.SmokingRooms
                )
            }

            // ===== Log Controls =====
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
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

            /* ===== History ===== */
            if (entries.isNotEmpty()) {

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

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = 16.dp,
                                bottom = 8.dp
                            )
                    ) {
                        Text(
                            text = "History",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Your previous smoking entries",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                items(
                    items = entries,
                    key = { it.id }
                ) { entry ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        SmokingHistoryItem(
                            entry = entry,
                            onDelete = {
                                vm.remove(entry)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Smoking entry removed",
                                        actionLabel = "Undo",
                                        withDismissAction = true,
                                        duration = SnackbarDuration.Long
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        vm.restore(entry)
                                    }
                                }
                            }
                        )
                    }
                }

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

@Composable
private fun SmokingHistoryItem(
    entry: SmokingEntryEntity,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 6.dp),
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.large
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 14.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val txt = "${entry.count} × ${entry.size.name.lowercase().replace("_", " ")}"
            Column {
                Text(
//                    text = "${entry.count} × ${entry.size.name.lowercase().replace("_", " ")}",
                    text = txt + if (entry.isMenthol) " • Menthol" else "",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = format(entry.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete entry",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
