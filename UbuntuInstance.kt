package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ubuntu_instances")
data class UbuntuInstance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val distroVersion: String, // e.g. "Ubuntu 24.04 LTS (Noble)", "Ubuntu 22.04 LTS (Jammy)", "Ubuntu Touch"
    val deploymentMode: String, // "PROOT" (No Root), "CHROOT" (Root Required), "DUAL_BOOT" (UEFI/EDK2), "VM_QEMU"
    val desktopEnvironment: String, // "XFCE4 (Recommended)", "GNOME 46", "LXQt", "Ubuntu Touch UI", "CLI Only"
    val allocatedStorageGb: Int, // e.g. 16, 32, 64
    val displayResolution: String, // e.g. "2560x1600 (Xiaomi Pad 5 Native)", "1920x1200", "1280x800"
    val vncPort: Int = 5901,
    val status: String = "STOPPED", // "STOPPED", "INSTALLING", "RUNNING", "PAUSED"
    val isDefault: Boolean = false,
    val createdTimestamp: Long = System.currentTimeMillis()
)
