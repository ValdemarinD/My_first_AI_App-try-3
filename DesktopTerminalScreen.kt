package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import com.example.ui.viewmodel.MouseControlMode
import com.example.ui.viewmodel.UbuntuViewModel

@Composable
fun DesktopTerminalScreen(
    viewModel: UbuntuViewModel,
    modifier: Modifier = Modifier
) {
    val logs by viewModel.terminalLogs.collectAsState()
    val selectedInstance by viewModel.selectedInstance.collectAsState()
    val mouseMode by viewModel.mouseControlMode.collectAsState()
    var commandInput by remember { mutableStateOf("") }
    var selectedSubTab by remember { mutableStateOf(0) } // 0: X11 Desktop Canvas, 1: Bash Terminal Engine

    val listState = rememberLazyListState()

    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Toolbar bar for Mode Switch & Resolution
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (selectedInstance?.status == "RUNNING") Color(0xFF15803D) else Color.Gray)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = selectedInstance?.name ?: "No Instance Selected",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = PolishPrimaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = selectedInstance?.displayResolution ?: "2560x1600",
                            color = PolishOnPrimaryContainer,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // SubTab selector (Desktop vs Terminal)
                Row {
                    Surface(
                        color = if (selectedSubTab == 0) PolishPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                        modifier = Modifier
                            .clickable { selectedSubTab = 0 }
                            .testTag("tab_desktop_view")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Computer, contentDescription = "Desktop", tint = if (selectedSubTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("X11 Canvas", color = if (selectedSubTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Surface(
                        color = if (selectedSubTab == 1) PolishPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        modifier = Modifier
                            .clickable { selectedSubTab = 1 }
                            .testTag("tab_terminal_view")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Terminal, contentDescription = "Terminal", tint = if (selectedSubTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Bash Shell", color = if (selectedSubTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }


        // View Content
        if (selectedSubTab == 0) {
            // Live X11/VNC Desktop Display Simulation Canvas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Interactive Desktop Canvas Area
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("desktop_canvas_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = TerminalBackground)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Desktop Background Image
                        Image(
                            painter = painterResource(id = R.drawable.img_ubuntu_hero_1785186252415),
                            contentDescription = "Ubuntu XFCE Desktop Frame",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Simulated Window Overlay (e.g., VS Code or Terminal on Desktop)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(0.92f)
                                .fillMaxHeight(0.85f)
                                .align(Alignment.Center),
                            color = Color(0xFF1E1E1E).copy(alpha = 0.95f),
                            shape = RoundedCornerShape(8.dp),
                            shadowElevation = 8.dp
                        ) {
                            Column {
                                // XFCE Window Titlebar
                                Surface(
                                    color = Color(0xFF2D2D2D),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Code,
                                                contentDescription = "VSCode",
                                                tint = PolishPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "main.py - Visual Studio Code (arm64 on Xiaomi Pad 5)",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily.Monospace
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFBD2E)))
                                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF27C93F)))
                                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFF5F56)))
                                        }
                                    }
                                }

                                // Code Editor Body
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp)
                                ) {
                                    Text("import torch", color = Color(0xFF569CD6), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                    Text("import numpy as np", color = Color(0xFF569CD6), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                    Text("print('--- Running PyTorch Model on Snapdragon 860 ---')", color = Color(0xFFCE9178), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("# VirGL Mesa Adreno 640 GPU Acceleration Enabled", color = Color(0xFF6A9955), fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                                    Text("device = torch.device('cpu')", color = Color(0xFFDCDCAA), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                    Text("print('Execution Speed: 120 FPS Native Xiaomi Pad 5 Display')", color = Color(0xFFCE9178), fontFamily = FontFamily.Monospace, fontSize = 12.sp)
                                }
                            }
                        }

                        // Status Badge on Canvas
                        Surface(
                            color = PolishPrimary,
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "LIVE VNC / X11 DISPLAY • 120Hz",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Touch & Mouse Input Control Switcher
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Touch Control:", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                        MouseControlMode.values().forEach { mode ->
                            val isSelected = mouseMode == mode
                            Surface(
                                color = if (isSelected) PolishPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .clickable { viewModel.setMouseControlMode(mode) }
                                    .testTag("mouse_mode_${mode.name}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (mode) {
                                            MouseControlMode.DIRECT_TOUCH -> Icons.Default.TouchApp
                                            MouseControlMode.TOUCHPAD_SIMULATION -> Icons.Default.Mouse
                                            MouseControlMode.STYLUS_PAD -> Icons.Default.Edit
                                        },
                                        contentDescription = mode.name,
                                        tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = when (mode) {
                                            MouseControlMode.DIRECT_TOUCH -> "Direct Touch"
                                            MouseControlMode.TOUCHPAD_SIMULATION -> "Trackpad"
                                            MouseControlMode.STYLUS_PAD -> "Xiaomi Pen"
                                        },
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Bash Terminal Engine
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // Terminal Output Area
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("terminal_output_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = TerminalBackground)
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(logs) { line ->
                            Text(
                                text = line,
                                color = when {
                                    line.startsWith("ubuntu@") -> Color(0xFFD0BCFF)
                                    line.contains("[SUCCESS]") -> Color(0xFF81C784)
                                    line.contains("[INFO]") -> Color(0xFF64B5F6)
                                    line.contains("[ROOTLESS]") -> Color(0xFFFFB74D)
                                    line.contains("OS:") || line.contains("Host:") -> Color.LightGray
                                    else -> Color.White
                                },
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quick Linux Command Chips
                ScrollableTabRow(
                    selectedTabIndex = 0,
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    divider = {},
                    indicator = { TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(it[0]), color = Color.Transparent) }
                ) {
                    val quickCmds = listOf("neofetch", "help", "proot --info", "apt update", "df -h", "top", "python3", "clear")
                    quickCmds.forEach { cmd ->
                        Surface(
                            color = PolishPrimaryContainer,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { viewModel.executeTerminalCommand(cmd) }
                                .testTag("quick_cmd_$cmd")
                        ) {
                            Text(
                                text = cmd,
                                color = PolishOnPrimaryContainer,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Command Input Box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = commandInput,
                        onValueChange = { commandInput = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("terminal_input_field"),
                        placeholder = { Text("Enter bash command...", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PolishPrimary,
                            unfocusedBorderColor = PolishOutlineLight,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {
                            viewModel.executeTerminalCommand(commandInput)
                            commandInput = ""
                        }),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            viewModel.executeTerminalCommand(commandInput)
                            commandInput = ""
                        },
                        modifier = Modifier
                            .background(PolishPrimary, RoundedCornerShape(12.dp))
                            .testTag("send_command_button")
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White)
                    }
                }
            }
        }

    }
}
