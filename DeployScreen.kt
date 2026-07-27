package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.TabletAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PolishOnPrimaryContainer
import com.example.ui.theme.PolishOutlineLight
import com.example.ui.theme.PolishPrimary
import com.example.ui.theme.PolishPrimaryContainer
import com.example.ui.theme.TerminalBackground
import com.example.ui.viewmodel.UbuntuViewModel

@Composable
fun DeployScreen(
    viewModel: UbuntuViewModel,
    modifier: Modifier = Modifier
) {
    val isInstalling by viewModel.isInstalling.collectAsState()
    val installProgress by viewModel.installProgress.collectAsState()

    var nameInput by remember { mutableStateOf("Xiaomi Pad 5 Ubuntu 24.04 Noble") }
    var selectedVersion by remember { mutableStateOf("Ubuntu 24.04 LTS (Noble Numbat)") }
    var selectedDesktop by remember { mutableStateOf("XFCE 4.18 (Recommended)") }
    var selectedStorageGb by remember { mutableIntStateOf(32) }
    var selectedResolution by remember { mutableStateOf("2560x1600 (Xiaomi Pad 5 Native)") }

    val versions = listOf(
        "Ubuntu 24.04 LTS (Noble Numbat)",
        "Ubuntu 22.04 LTS (Jammy Jellyfish)",
        "Ubuntu Touch (Tablet UI)"
    )

    val desktops = listOf(
        "XFCE 4.18 (Recommended)",
        "GNOME 46 (Full Desktop)",
        "LXQt (Ultra Lightweight)",
        "CLI Shell Only"
    )

    val storages = listOf(16, 32, 64)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("deploy_header_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PolishPrimaryContainer)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = PolishPrimary,
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.RocketLaunch,
                                    contentDescription = "Deploy",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "1-Click Rootless Ubuntu Installer",
                                color = PolishOnPrimaryContainer,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Deploy official arm64 Linux environment without rooting your tablet",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        if (isInstalling) {
            // Installation Progress Box matching Professional Polish design
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("installation_progress_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Installation Progress",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { installProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = PolishPrimary,
                            trackColor = PolishPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Unpacking rootfs tarball & PRoot setup...",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "${(installProgress * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PolishPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Simulated Terminal Stream Log
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = TerminalBackground)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .height(110.dp)
                            ) {
                                Text("[INFO] Allocating isolated PRoot filesystem sandbox...", color = Color(0xFFD0BCFF), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                Text("[OK] Rootless ptrace virtualization active", color = Color(0xFF81C784), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                Text("[INFO] Unpacking ubuntu-24.04-base-arm64.tar.xz", color = Color.White, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                Text("> usr/bin/dpkg", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                Text("> usr/share/icons/ubuntu-mono-dark/...", color = Color.Gray, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                                Text("> Configuring VirGL Mesa drivers for Adreno 640...", color = Color(0xFF64B5F6), fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        } else {
            // Configuration Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Instance Details",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Name Field
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Environment Name") },
                            modifier = Modifier.fillMaxWidth().testTag("deploy_name_input"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PolishPrimary,
                                unfocusedBorderColor = PolishOutlineLight
                            )
                        )

                        // Version Selection
                        Text("Ubuntu Version (arm64)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            versions.forEach { ver ->
                                val isSelected = selectedVersion == ver
                                Surface(
                                    color = if (isSelected) PolishPrimaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
                                    border = if (isSelected) CardDefaults.outlinedCardBorder() else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedVersion = ver }
                                        .testTag("version_select_$ver")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Computer,
                                            contentDescription = ver,
                                            tint = if (isSelected) PolishPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = ver,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) PolishOnPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        // Desktop Environment Selection
                        Text("Desktop Environment", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            desktops.forEach { de ->
                                val isSelected = selectedDesktop == de
                                Surface(
                                    color = if (isSelected) PolishPrimaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
                                    border = if (isSelected) CardDefaults.outlinedCardBorder() else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedDesktop = de }
                                        .testTag("de_select_$de")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Laptop,
                                            contentDescription = de,
                                            tint = if (isSelected) PolishPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = de,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) PolishOnPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        // Allocated Storage Size
                        Text("Allocated Storage Sandbox", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            storages.forEach { gb ->
                                val isSelected = selectedStorageGb == gb
                                Surface(
                                    color = if (isSelected) PolishPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedStorageGb = gb }
                                        .testTag("storage_select_${gb}GB")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "$gb GB",
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = "Virtual EXT4",
                                            fontSize = 10.sp,
                                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                viewModel.createNewInstance(
                                    name = nameInput,
                                    version = selectedVersion,
                                    mode = "ROOTLESS_PROOT",
                                    desktop = selectedDesktop,
                                    storageGb = selectedStorageGb,
                                    resolution = selectedResolution
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_deployment_button"),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PolishPrimary)
                        ) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = "Install", modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Rootless Deployment", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
