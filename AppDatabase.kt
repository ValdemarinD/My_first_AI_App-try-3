package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.UbuntuDao
import com.example.data.entity.SetupScript
import com.example.data.entity.UbuntuInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UbuntuInstance::class, SetupScript::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ubuntuDao(): UbuntuDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ubuntu_pad5_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(context.applicationContext))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.ubuntuDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: UbuntuDao) {
                // Default pre-configured Ubuntu 24.04 PRoot instance for Xiaomi Pad 5 (NO ROOT REQUIRED)
                val defaultPad5Ubuntu = UbuntuInstance(
                    name = "Ubuntu 24.04 LTS Workstation (Rootless PRoot)",
                    distroVersion = "Ubuntu 24.04 LTS (Noble Numbat)",
                    deploymentMode = "ROOTLESS_PROOT",
                    desktopEnvironment = "XFCE4 (Recommended)",
                    allocatedStorageGb = 32,
                    displayResolution = "2560x1600 (Pad 5 Native)",
                    vncPort = 5901,
                    status = "STOPPED",
                    isDefault = true
                )
                dao.insertInstance(defaultPad5Ubuntu)

                // Lightweight profile
                val lxqtUbuntu = UbuntuInstance(
                    name = "Ubuntu 22.04 Lightweight Desktop (Rootless)",
                    distroVersion = "Ubuntu 22.04 LTS (Jammy Jellyfish)",
                    deploymentMode = "ROOTLESS_PROOT",
                    desktopEnvironment = "LXQt / Openbox",
                    allocatedStorageGb = 16,
                    displayResolution = "1920x1200",
                    vncPort = 5902,
                    status = "STOPPED",
                    isDefault = false
                )
                dao.insertInstance(lxqtUbuntu)

                // Populate Pad 5 rootless optimization scripts
                val scripts = listOf(
                    SetupScript(
                        title = "Adreno 640 Mesa VirGL 3D Acceleration (Rootless)",
                        category = "GPU",
                        description = "Enables Mesa VirGL hardware 3D graphics rendering acceleration inside rootless PRoot for Snapdragon 860 Adreno GPU.",
                        bashScript = "sudo apt update && sudo apt install -y mesa-utils libgl1-mesa-dri virglserver\nexport GALLIUM_DRIVER=virpipe\nexport MESA_GL_VERSION_OVERRIDE=4.0\nvirgl_test_server &",
                        isRecommendedForPad5 = true
                    ),
                    SetupScript(
                        title = "Xiaomi Smart Pen & Touchscreen Calibration (Rootless)",
                        category = "STYLUS",
                        description = "Configures pressure sensitivity and palm rejection for Xiaomi Pad 5 stylus inside X11/XFCE desktop.",
                        bashScript = "sudo apt install -y xinput xserver-xorg-input-evdev\nxinput set-prop 'Xiaomi Pen' 'Evdev Wheel Emulation' 1",
                        isRecommendedForPad5 = true
                    ),
                    SetupScript(
                        title = "Xiaomi Quad-Speaker Audio Sync (PulseAudio TCP)",
                        category = "AUDIO",
                        description = "Routines Ubuntu Linux audio output through Android AAudio/OpenSL ES bridge to Xiaomi Pad 5 Dolby speakers.",
                        bashScript = "sudo apt install -y pulseaudio pulseaudio-utils\npulseaudio --start --exit-idle-time=-1\npactl load-module module-native-protocol-tcp auth-anonymous=1",
                        isRecommendedForPad5 = true
                    ),
                    SetupScript(
                        title = "Developer Stack (VS Code + Python 3.12 + Node.js arm64)",
                        category = "DEV",
                        description = "Installs full arm64 development suite including Visual Studio Code, Git, Python3, and Node.js LTS inside PRoot.",
                        bashScript = "sudo apt update && sudo apt install -y git python3 python3-pip nodejs npm curl\ncurl -L https://code.visualstudio.com/sha/download?build=stable&os=linux-deb-arm64 -o vscode.deb\nsudo dpkg -i vscode.deb",
                        isRecommendedForPad5 = true
                    )
                )

                for (script in scripts) {
                    dao.insertScript(script)
                }
            }
        }
    }
}
