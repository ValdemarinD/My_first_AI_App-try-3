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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.UbuntuViewModel

@Composable
fun NewDeploymentScreen(
    viewModel: UbuntuViewModel,
    modifier: Modifier = Modifier
) {
    val isInstalling by viewModel.isInstalling.collectAsState()
    val progress by viewModel.installProgress.collectAsState()

    var instanceName by remember { mutableStateOf("Xiaomi Pad 5 Ubuntu Workstation") }
    var selectedVersion by remember { mutableStateOf("Ubuntu 24.04 LTS (Noble Numbat)") }
    var selectedMode by remember { mutableStateOf("ROOTLESS_PROOT") }
    var selectedDesktop by remember { mutableStateOf("XFCE4 (Recommended)") }
    var selectedStorageGb by remember { mutableStateOf(32) }
    var selectedResolution by remember { mutableStateOf("2560x1600 (Pad 5 Native)") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deployment_wizard_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UbuntuAubergine)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFF388E3C),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = "Safe",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("100% ROOTLESS PROOT INSTALLER", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "1-Click Ubuntu Deployment Wizard",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Set up a clean Ubuntu Linux distribution on Xiaomi Pad 5 without rooting or altering bootloader.",
                        color = UbuntuWarmGray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        if (isInstalling) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UbuntuDarkSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Downloading",
                            tint = UbuntuOrange,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Extracting & Configuring Ubuntu Rootfs...",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Downloading official arm64 Noble rootfs and setting up PRoot ptrace environment.",
                            color = UbuntuWarmGray,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = UbuntuOrange,
                            trackColor = UbuntuCardDark,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(progress * 100).toInt()}% Completed",
                            color = UbuntuOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            // Configuration Form
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = UbuntuDarkSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("1. Environment Name", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        OutlinedTextField(
                            value = instanceName,
                            onValueChange = { instanceName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("instance_name_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UbuntuOrange,
                                unfocusedBorderColor = UbuntuCardDark,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            singleLine = true
                        )

                        Text("2. Select Ubuntu Distro Version (arm64)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        val versions = listOf(
                            "Ubuntu 24.04 LTS (Noble Numbat)",
                            "Ubuntu 22.04 LTS (Jammy Jellyfish)",
                            "Ubuntu 20.04 LTS (Focal Fossa)"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            versions.forEach { ver ->
                                val isSel = selectedVersion == ver
                                Surface(
                                    color = if (isSel) UbuntuCardDark else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedVersion = ver }
                                        .border(1.dp, if (isSel) UbuntuOrange else UbuntuCardDark, RoundedCornerShape(8.dp))
                                        .testTag("version_option_$ver")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(if (isSel) UbuntuOrange else UbuntuCardDark),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSel) Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(ver, color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        Text("3. Deployment Architecture", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        Surface(
                            color = UbuntuCardDark,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, UbuntuOrange, RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.Security, contentDescription = "Safe", tint = Color(0xFF81C784))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text("ROOTLESS PROOT (No Root Required - Recommended)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Uses ptrace syscall translation. Works on all stock Xiaomi Pad 5 devices safely.", color = UbuntuWarmGray, fontSize = 11.sp)
                                }
                            }
                        }

                        Text("4. Desktop Environment UI", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        val desktops = listOf(
                            "XFCE4 (Recommended - Lightweight & Fast)",
                            "LXQt / Openbox (Ultra Low RAM)",
                            "GNOME 46 (Full Ubuntu Visual Desktop)",
                            "CLI Only (Terminal Headless)"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            desktops.forEach { de ->
                                val isSel = selectedDesktop == de
                                Surface(
                                    color = if (isSel) UbuntuCardDark else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedDesktop = de }
                                        .border(1.dp, if (isSel) UbuntuOrange else UbuntuCardDark, RoundedCornerShape(8.dp))
                                        .testTag("de_option_$de")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(18.dp)
                                                .clip(CircleShape)
                                                .background(if (isSel) UbuntuOrange else UbuntuCardDark),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isSel) Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(de, color = Color.White, fontSize = 13.sp)
                                    }
                                }
                            }
                        }

                        Text("5. Allocated Storage Space", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(16, 32, 64).forEach { size ->
                                val isSel = selectedStorageGb == size
                                Surface(
                                    color = if (isSel) UbuntuOrange else UbuntuCardDark,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedStorageGb = size }
                                ) {
                                    Text(
                                        text = "$size GB",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.createNewInstance(
                                    name = instanceName,
                                    version = selectedVersion,
                                    mode = selectedMode,
                                    desktop = selectedDesktop,
                                    storageGb = selectedStorageGb,
                                    resolution = selectedResolution
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("start_deployment_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = UbuntuOrange),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Deploy")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Rootless Deployment", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
