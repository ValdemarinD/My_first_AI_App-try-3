package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.UbuntuAubergine
import com.example.ui.theme.UbuntuCardDark
import com.example.ui.theme.UbuntuDarkSurface
import com.example.ui.theme.UbuntuOrange
import com.example.ui.theme.UbuntuWarmGray
import com.example.ui.viewmodel.UbuntuViewModel

@Composable
fun RootlessGuideScreen(
    viewModel: UbuntuViewModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("rootless_guide_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UbuntuAubergine)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = "Shield", tint = Color(0xFF81C784))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "100% ROOTLESS ARCHITECTURE EXPLAINED",
                            color = Color(0xFF81C784),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "How Ubuntu Runs Without Root on Xiaomi Pad 5",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Learn how PRoot syscall translation provides full Linux userspace without unlocking bootloader or losing MIUI warranty.",
                        color = UbuntuWarmGray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // Section 1: Comparison
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UbuntuDarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Deployment Mode Comparison",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Mode 1: PRoot
                    Surface(
                        color = UbuntuCardDark,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFF388E3C), RoundedCornerShape(8.dp))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("1. PRoot (This App - No Root Required)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Surface(color = Color(0xFF388E3C), shape = RoundedCornerShape(4.dp)) {
                                    Text("RECOMMENDED", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("• Root Needed: NO (Zero Root)", color = Color(0xFF81C784), fontSize = 12.sp)
                            Text("• Uses Android 'ptrace()' syscall interception to fake file system paths.", color = UbuntuWarmGray, fontSize = 12.sp)
                            Text("• Safety: 100% Safe. Installed inside app storage sandbox.", color = UbuntuWarmGray, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mode 2: Chroot
                    Surface(
                        color = UbuntuCardDark,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("2. Rooted Chroot", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Root Needed: YES (Magisk / KernelSU)", color = Color(0xFFE57373), fontSize = 12.sp)
                            Text("• Direct Linux loop mount into Android kernel /dev namespace.", color = UbuntuWarmGray, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Mode 3: UEFI Dual-Boot
                    Surface(
                        color = UbuntuCardDark,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("3. EDK2 UEFI Native Dual-Boot", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("• Root & Unlocked Bootloader Needed: YES", color = Color(0xFFE57373), fontSize = 12.sp)
                            Text("• Replaces Android boot partition with EDK2 UEFI firmware to boot Linux raw.", color = UbuntuWarmGray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section 2: Step-by-Step
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UbuntuDarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quickstart: Running Rootless Ubuntu in 3 Steps",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val steps = listOf(
                        "Step 1: Open 'Control Hub' tab and tap 'Launch Rootless Session'.",
                        "Step 2: Switch to 'X11 Canvas' tab to view live Ubuntu XFCE4 desktop.",
                        "Step 3: Connect Xiaomi Smart Pen or Bluetooth Keyboard/Mouse for desktop productivity!"
                    )

                    steps.forEachIndexed { index, step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(UbuntuOrange),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("${index + 1}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(step, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Section 3: Hardware Support Matrix
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = UbuntuDarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Xiaomi Pad 5 (nabu) Hardware Features in PRoot",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val hwItems = listOf(
                        Triple("Snapdragon 860 CPU", "8 Cores @ 2.96GHz Fully Active", true),
                        Triple("Adreno 640 GPU", "Mesa VirGL 3D Acceleration Enabled", true),
                        Triple("2560x1600 WQHD+ Display", "120Hz Native Resolution Supported", true),
                        Triple("Xiaomi Smart Pen", "X11 Evdev Mouse Emulation", true),
                        Triple("Quad Dolby Speakers", "PulseAudio TCP Output Bridge", true),
                        Triple("Storage Mount", "Direct access to /sdcard & Downloads", true)
                    )

                    hwItems.forEach { (name, desc, supported) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(desc, color = UbuntuWarmGray, fontSize = 11.sp)
                            }
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Supported",
                                tint = Color(0xFF81C784),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
