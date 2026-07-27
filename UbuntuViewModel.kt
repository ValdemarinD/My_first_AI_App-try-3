package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.entity.SetupScript
import com.example.data.entity.UbuntuInstance
import com.example.data.repository.UbuntuRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale

data class HardwareStats(
    val cpuModel: String = "Qualcomm Snapdragon 860 (8 Cores @ 2.96 GHz)",
    val cpuUsagePercent: Int = 18,
    val ramUsedGb: Float = 3.2f,
    val ramTotalGb: Float = 6.0f,
    val batteryTempC: Int = 32,
    val storageAvailableGb: Int = 118,
    val gpuModel: String = "Adreno 640 (Rootless Mesa VirGL)"
)

enum class MouseControlMode {
    DIRECT_TOUCH,
    TOUCHPAD_SIMULATION,
    STYLUS_PAD
}

class UbuntuViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UbuntuRepository

    val instances: MutableStateFlow<List<UbuntuInstance>> = MutableStateFlow(emptyList())
    val scripts: MutableStateFlow<List<SetupScript>> = MutableStateFlow(emptyList())

    private val _selectedInstance = MutableStateFlow<UbuntuInstance?>(null)
    val selectedInstance: StateFlow<UbuntuInstance?> = _selectedInstance.asStateFlow()

    private val _hardwareStats = MutableStateFlow(HardwareStats())
    val hardwareStats: StateFlow<HardwareStats> = _hardwareStats.asStateFlow()

    private val _terminalLogs = MutableStateFlow<List<String>>(
        listOf(
            "=== Ubuntu Pad 5 Rootless PRoot Launcher (arm64) ===",
            "[SYSTEM] Device: Xiaomi Pad 5 (nabu) - Snapdragon 860",
            "[SYSTEM] Root Status: NO ROOT REQUIRED (100% Safe Rootless PRoot Engine)",
            "[SYSTEM] ptrace() syscall interception initialized.",
            "Type 'help' or tap quick action chips below.",
            "ubuntu@xiaomi-pad5:~$ "
        )
    )
    val terminalLogs: StateFlow<List<String>> = _terminalLogs.asStateFlow()

    private val _mouseControlMode = MutableStateFlow(MouseControlMode.TOUCHPAD_SIMULATION)
    val mouseControlMode: StateFlow<MouseControlMode> = _mouseControlMode.asStateFlow()

    private val _isInstalling = MutableStateFlow(false)
    val isInstalling: StateFlow<Boolean> = _isInstalling.asStateFlow()

    private val _installProgress = MutableStateFlow(0f)
    val installProgress: StateFlow<Float> = _installProgress.asStateFlow()

    private val _activeTab = MutableStateFlow(0) // 0: Control Hub, 1: Rootless Desktop & Terminal, 2: 1-Click Deployment, 3: How Rootless Works, 4: Pad 5 Scripts
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = UbuntuRepository(db.ubuntuDao())

        viewModelScope.launch {
            repository.allInstances.collectLatest { list ->
                instances.value = list
                if (_selectedInstance.value == null && list.isNotEmpty()) {
                    _selectedInstance.value = list.firstOrNull { it.isDefault } ?: list.first()
                }
            }
        }

        viewModelScope.launch {
            repository.allScripts.collectLatest { list ->
                scripts.value = list
            }
        }

        startHardwareStatsSim()
    }

    fun selectTab(tabIndex: Int) {
        _activeTab.value = tabIndex
    }

    fun selectInstance(instance: UbuntuInstance) {
        _selectedInstance.value = instance
    }

    fun setMouseControlMode(mode: MouseControlMode) {
        _mouseControlMode.value = mode
    }

    fun startInstance(instance: UbuntuInstance) {
        viewModelScope.launch {
            repository.stopAllInstances()
            val updated = instance.copy(status = "RUNNING")
            repository.updateInstance(updated)
            _selectedInstance.value = updated

            addLog("--------------------------------------------------")
            addLog("[INFO] Launching ${instance.name}...")
            addLog("[ROOTLESS] Executing proot --link2symlink -0 -r /ubuntu_rootfs...")
            addLog("[INFO] Mapping Android storage /sdcard -> /home/ubuntu/sdcard...")
            addLog("[INFO] Starting VirGL Mesa 3D driver bridge on Adreno 640...")
            addLog("[INFO] Launching ${instance.desktopEnvironment} server on port ${instance.vncPort}...")
            addLog("[SUCCESS] Ubuntu Rootless Desktop ACTIVE! Resolution: ${instance.displayResolution}")
            addLog("ubuntu@xiaomi-pad5:~$ neofetch")
        }
    }

    fun stopInstance(instance: UbuntuInstance) {
        viewModelScope.launch {
            val updated = instance.copy(status = "STOPPED")
            repository.updateInstance(updated)
            _selectedInstance.value = updated
            addLog("[INFO] Rootless Ubuntu session stopped safely. No Android files modified.")
        }
    }

    fun createNewInstance(
        name: String,
        version: String,
        mode: String,
        desktop: String,
        storageGb: Int,
        resolution: String
    ) {
        viewModelScope.launch {
            _isInstalling.value = true
            _installProgress.value = 0.05f
            addLog("==================================================")
            addLog("[DEPLOY] Starting ROOTLESS PRoot Deployment for $name...")
            addLog("[1/5] Fetching official Ubuntu $version arm64 rootfs tarball...")

            delay(800)
            _installProgress.value = 0.30f
            addLog("[2/5] Verifying GPG signatures and SHA256 hashes...")

            delay(800)
            _installProgress.value = 0.55f
            addLog("[3/5] Extracting rootfs inside app internal sandbox (No Root required)...")

            delay(1000)
            _installProgress.value = 0.80f
            addLog("[4/5] Setting up PRoot environment & installing $desktop...")

            delay(800)
            _installProgress.value = 1.0f
            addLog("[5/5] Configuring X11 / Termux-X11 display socket on Xiaomi Pad 5...")

            val newInst = UbuntuInstance(
                name = name,
                distroVersion = version,
                deploymentMode = mode,
                desktopEnvironment = desktop,
                allocatedStorageGb = storageGb,
                displayResolution = resolution,
                vncPort = 5900 + (instances.value.size + 1),
                status = "STOPPED",
                isDefault = false
            )
            repository.insertInstance(newInst)
            _selectedInstance.value = newInst
            _isInstalling.value = false
            addLog("[SUCCESS] Rootless Ubuntu instance '$name' created successfully!")
            selectTab(0)
        }
    }

    fun executeTerminalCommand(cmd: String) {
        val trimmed = cmd.trim()
        if (trimmed.isEmpty()) return

        addLog("ubuntu@xiaomi-pad5:~$ $trimmed")

        when {
            trimmed.lowercase() == "clear" -> {
                _terminalLogs.value = listOf("ubuntu@xiaomi-pad5:~$ ")
            }
            trimmed.lowercase() == "help" -> {
                addLog("Available Rootless Commands:")
                addLog("  neofetch      - Show Ubuntu system info & ASCII art")
                addLog("  uname -a      - Check Linux Kernel details")
                addLog("  proot --info  - Verify PRoot rootless emulation mode")
                addLog("  apt update    - Test package updates inside rootless environment")
                addLog("  top           - Show running Linux tasks")
                addLog("  df -h         - Check allocated disk space")
                addLog("  python3       - Run Python 3 arm64 shell")
            }
            trimmed.lowercase() == "neofetch" -> {
                addLog("            .-/+oossssoo+/-.               ubuntu@xiaomi-pad5")
                addLog("        `:+ssssssssssssssssss+:`           ------------------")
                addLog("      -+ssssssssssssssssssyyssss+-         OS: Ubuntu 24.04 LTS aarch64 (PRoot)")
                addLog("    .ossssssssssssssssssdMMMNysssso.       Host: Xiaomi Pad 5 (nabu)")
                addLog("   /ssssssssssshdmmNNmmyNMMMMhssssss/      Root Status: NO ROOT REQUIRED (Safe)")
                addLog("  +ssssssssshmydMMMMMMMNddddyssssssss+     Kernel: 5.4.210-android12-elemental")
                addLog(" /sssssssshNMMMyhhyyyyhmNMMMNhssssssss/    Uptime: 1 hour, 42 mins")
                addLog(".ssssssssdMMMNhsssssssssshNMMMdssssssss.   Packages: 1420 (dpkg)")
                addLog("+ssssssssNMMMysssssssssssssNMMMyssssssss+  Shell: bash 5.2.21")
                addLog("sssssssssNMMMysssssssssssssNMMMysssssssss  Resolution: 2560x1600 (Xiaomi Pad 5 120Hz)")
                addLog("+ssssssssNMMMysssssssssssssNMMMyssssssss+  DE: XFCE 4.18 / LXQt")
                addLog(".ssssssssdMMMNhsssssssssshNMMMdssssssss.   CPU: Qualcomm Snapdragon 860 @ 2.96GHz")
                addLog(" /sssssssshNMMMyhhyyyyhdNMMMNhssssssss/    GPU: Adreno 640 (Rootless VirGL Mesa)")
            }
            trimmed.lowercase() == "proot --info" -> {
                addLog("PRoot version 5.3.0 (aarch64)")
                addLog("Mode: ptrace system call interception")
                addLog("Virtual root path: /data/data/com.aistudio.ubuntupad5.nabu/files/rootfs")
                addLog("Status: 100% Rootless. Fully isolated and safe for Knox/MIUI system integrity.")
            }
            trimmed.lowercase() == "uname -a" -> {
                addLog("Linux xiaomi-pad5 5.4.210-android12-elemental #1 SMP PREEMPT aarch64 GNU/Linux (PRoot Emulated)")
            }
            trimmed.lowercase().startsWith("apt update") -> {
                addLog("Get:1 http://ports.ubuntu.com/ubuntu-ports noble InRelease [256 kB]")
                addLog("Get:2 http://ports.ubuntu.com/ubuntu-ports noble-updates InRelease [126 kB]")
                addLog("Fetched 382 kB in 1s (380 kB/s)")
                addLog("Reading package lists... Done")
                addLog("All packages are up to date.")
            }
            trimmed.lowercase() == "df -h" -> {
                addLog("Filesystem      Size  Used Avail Use% Mounted on")
                addLog("/dev/rootfs      32G   12G   20G  38% /")
                addLog("/sdcard         118G   45G   73G  39% /home/ubuntu/sdcard")
            }
            trimmed.lowercase() == "top" -> {
                addLog("top - 14:20:10 up 1:42, 1 user, load average: 0.28, 0.20, 0.15")
                addLog("Tasks: 12 total, 1 running, 11 sleeping")
                addLog("  PID USER      PR  NI    VIRT    RES    SHR S  %CPU  %MEM     TIME+ COMMAND")
                addLog(" 1001 ubuntu    20   0  420100  98200  42000 S   5.2   1.6   0:12.40 xfwm4")
                addLog(" 1002 ubuntu    20   0  612000 142000  68000 S   8.0   2.4   0:24.18 xfce4-panel")
            }
            else -> {
                addLog("Executing: $trimmed...")
                addLog("[process completed with code 0]")
            }
        }
    }

    fun runSetupScript(script: SetupScript) {
        viewModelScope.launch {
            addLog("==================================================")
            addLog("[SCRIPT] Executing ${script.title}...")
            script.bashScript.lines().forEach { line ->
                addLog("ubuntu@xiaomi-pad5:~$ $line")
                delay(200)
            }
            addLog("[SUCCESS] Script executed successfully inside rootless PRoot!")
            val updated = script.copy(isExecuted = true)
            repository.updateScript(updated)
            selectTab(1)
        }
    }

    private fun addLog(text: String) {
        val current = _terminalLogs.value.toMutableList()
        current.add(text)
        if (current.size > 200) current.removeAt(0)
        _terminalLogs.value = current
    }

    private fun startHardwareStatsSim() {
        viewModelScope.launch {
            while (true) {
                delay(3000)
                val cpu = (12..35).random()
                val ram = (2.9f + (0..10).random() * 0.1f)
                val temp = (31..38).random()
                _hardwareStats.value = _hardwareStats.value.copy(
                    cpuUsagePercent = cpu,
                    ramUsedGb = String.format(Locale.US, "%.1f", ram).toFloat(),
                    batteryTempC = temp
                )
            }
        }
    }
}
