# SMS Agent


SMS Agent is a React Native based application what enables the mobile device to send SMS messages based on real-time updates received via Server-Sent Events (SSE). The app establishes a continuous connection with a server, listens for incoming SMS data, and sends messages to the specified recipients using the mobile device's native SMS functionality.

> [!Note]
> This application is currently under development. If you encounter any issues, please report them in the [Issues](../../issues) section.


## Prerequisites
* ADB (Android Debug Bridge) installed and configured.
* Java installed for running bundletool.
* USB Debugging enabled on your Android device.

## Installation

1. Install `adb`
   - **Windows:**  
     Download the [Android SDK Platform-Tools](https://developer.android.com/studio/releases/platform-tools) and extract the contents. Add the folder to your system's PATH.  
   - **macOS/Linux:**  
     Install `adb` using a package manager:  
     ```bash
     # macOS
     brew install android-platform-tools
     
     # Linux (Debian/Ubuntu)
     sudo apt update
     sudo apt install adb
     ```
   - Verify the installation:  
     ```bash
     adb version
     ```

2. Download the `.aab` file
   The `.aab` file is located in the [Releases](../../releases) section of this repository. Download and save it to your computer

3. Generate an APK from the `.aab` file
   Use `bundletool` to create an APK that can be installed on your device:
   ```bash
   java -jar bundletool.jar build-apks --bundle=<path-to-your-app>.aab --output=<output-path>.apks --mode=universal
   ```

4. Enable Developer Mode on Your Phone
   * Go to your phone's settings.
   * Enable Developer Options and USB Debugging.

5. Connect Your Phone to the Computer
   Use a USB cable to connect your Android device to your computer. Ensure adb recognizes your device:
   ```bash
   adb devices
   ```

6. Install the App Directly Using `bundletool`
   Use `bundletool` to install the generated `.apks` package on your connected device:  
   ```bash
   java -jar bundletool.jar install-apks --apks=<path-to-apks>.apks
   ```
