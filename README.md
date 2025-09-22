# Keyla CLI

A cross-platform command-line typing test application built with Kotlin Multiplatform.

## Overview

Keyla CLI is a modern terminal-based typing speed and accuracy testing tool that runs on JVM, Linux, and Windows. It provides real-time performance metrics, user profile management, and comprehensive test history tracking.

## Features

- **Cross-platform compatibility** (JVM, Linux, Windows)
- **Real-time typing metrics** (WPM, accuracy, error tracking)
- **User profile management**
- **Test history and statistics**
- **Backend API integration**
- **Modern terminal UI** with color coding

## Quick Start

### Build
```bash
./gradlew build
```

### Run
```bash
./gradlew run
```

## Getting Started Tutorial

### Step 0: Installation (Download Pre-built Release)

Download the pre-built executable for your platform from the [GitHub Releases](https://github.com/Keyla-TTT/Keyla-CLI/releases) page:

#### Download and Install

**Linux (x64):**
```bash
# Download the latest release
wget https://github.com/Keyla-TTT/Keyla-CLI/releases/latest/download/keyla-linux

# Make it executable
chmod +x keyla-linux

# Copy to system directory
sudo cp keyla-linux /usr/local/bin/keyla

# Verify installation
keyla settings
```

**Note**: The Linux executable requires the `libcurl` library, which is typically pre-installed on most Linux distributions. If you encounter issues, install it with:
```bash
# Ubuntu/Debian
sudo apt-get install libcurl4-openssl-dev

# Fedora/RHEL/CentOS
sudo dnf install libcurl-devel

# Arch Linux
sudo pacman -S curl
```

**macOS (Apple Silicon):**
```bash
# Download the latest release
curl -L -o keyla-macos-arm64 https://github.com/Keyla-TTT/Keyla-CLI/releases/latest/download/keyla-macos-arm64

# Make it executable
chmod +x keyla-macos-arm64

# Copy to system directory
sudo cp keyla-macos-arm64 /usr/local/bin/keyla

# Verify installation
keyla settings
```

**Windows:**
```cmd
# Download the latest release
curl -L -o keyla-windows.exe https://github.com/Keyla-TTT/Keyla-CLI/releases/latest/download/keyla-windows.exe

# Copy to system directory (e.g., C:\Program Files\Keyla\)
mkdir "C:\Program Files\Keyla" 2>nul
copy keyla-windows.exe "C:\Program Files\Keyla\keyla.exe"

# Add to PATH environment variable:
# 1. Open System Properties > Environment Variables
# 2. Add "C:\Program Files\Keyla" to your PATH
# 3. Restart your terminal

# Verify installation
keyla settings
```

#### Alternative: Direct Download

You can also download the executable directly from the GitHub Releases page:

1. Go to [GitHub Releases](https://github.com/Keyla-TTT/Keyla-CLI/releases)
2. Download the appropriate file for your platform:
   - `keyla-linux` - Linux x64
   - `keyla-macos-arm64` - macOS Apple Silicon
   - `keyla-windows.exe` - Windows x64
3. Move the executable to a directory in your PATH
4. Make it executable (Linux/macOS only)


#### Manual Installation (No PATH)

If you prefer not to add to PATH, you can run Keyla directly:

**Linux:**
```bash
# Download executable
wget https://github.com/Keyla-TTT/Keyla-CLI/releases/latest/download/keyla-linux

# Make it executable
chmod +x keyla-linux

# Run directly (use full path)
./keyla-linux --version
./keyla-linux test
```

**macOS:**
```bash
# Download executable
curl -L -o keyla-macos-arm64 https://github.com/Keyla-TTT/Keyla-CLI/releases/latest/download/keyla-macos-arm64

# Make it executable
chmod +x keyla-macos-arm64

# Run directly (use full path)
./keyla-macos-arm64 --version
./keyla-macos-arm64 test
```

**Windows:**
```cmd
# Download executable
curl -L -o keyla-windows.exe https://github.com/Keyla-TTT/Keyla-CLI/releases/latest/download/keyla-windows.exe

# Run directly (use full path)
.\keyla-windows.exe --version
.\keyla-windows.exe test
```

### Step 1: Initial Setup

When you first run Keyla CLI, the application will automatically set up the default configuration:

- **Dictionaries folder**: `~/keyla/dictionaries` (your home directory)
- **Backend URL**: Default API endpoint
- **User profiles**: Empty (you'll create your first profile)

### Step 2: Configure Backend Settings (Optional)

If you need to change the backend URL or other API settings:

```bash
keyla settings
```

This will open the settings menu where you can:
- Change the backend API URL
- Select the active profile
- Test the connection to your backend

**Note**: Make sure your backend server is running before proceeding to the next step.

### Step 3: Configure Dictionaries Folder

To change where Keyla looks for dictionary files:

```bash
keyla config
```

This opens the configuration menu where you can:
- Set the dictionaries folder path (default: `~/keyla/dictionaries`)
- Browse and select a different folder
- Verify that dictionary files are accessible

**Default dictionaries location**: `~/keyla/dictionaries`

### Step 4: Start Using the App

Once your backend is running and configured, you can start using Keyla:

```bash
# Start a typing test (default mode)
keyla test
```

### Step 5: Create Your First Profile

Before taking your first test, you'll need to create a user profile:

```bash
keyla profile
```

Follow the prompts to:
- Enter your name and email

Now run again to select your profile:

```bash
keyla settings
```

Check that your profile is selected as active.

### Step 6: Take Your First Test

Now you're ready to start typing:

```bash
keyla test
```
## Usage

```bash
keyla [mode]

# Available modes:
keyla test      # Start typing test (default)
keyla config    # Manage dictionaries folder and backend configuration
keyla settings  # Application settings (backend URL, timeouts, etc.)
keyla history   # View test history
keyla profile   # Manage user profiles
keyla stats     # View statistics
```

### Configuration Workflow

1**Backend configuration**: Run `keyla settings` to configure API
2**Dictionaries setup**: Run `keyla config` to set dictionaries folder
3**Profile creation**: Run `keyla profile` to create your user profile
4**Start testing**: Run `keyla test` to begin typing tests

### Troubleshooting

**Backend connection issues**:
- Run `keyla settings` to verify your backend URL
- Ensure your backend server is running
- Check network connectivity

**Dictionary not found**:
- Run `keyla config` to verify dictionaries folder path
- Ensure dictionary files are in the correct location and format
- Check file permissions

**Profile issues**:
- Run `keyla profile` to create or manage profiles
- Ensure you have an active profile before taking tests

**Linux dependency issues**:
- If you get library errors on Linux, ensure `libcurl` is installed:
  ```bash
  # Ubuntu/Debian
  sudo apt-get install libcurl4-openssl-dev

  # Fedora/RHEL/CentOS
  sudo dnf install libcurl-devel

  # Arch Linux
  sudo pacman -S curl
  ```

## Development

### Prerequisites
- Java 17+
- Gradle 8.0+
- **Linux**: `libcurl4-openssl-dev` and `zlib1g-dev` (for native builds)
- **macOS**: `curl` (via Homebrew)

### Running the Application for Development

#### JVM (Cross-platform)
The easiest way to run the application during development is using the JVM target:

```bash
# Run the JVM application
./gradlew run

# Run with specific arguments
./gradlew runJvm -PappArgs="test"
./gradlew runJvm -PappArgs="settings"
./gradlew runJvm -PappArgs="config"
```

#### Native Executables

**Build and run debug executables:**
```bash
# Linux
./gradlew runDebugExecutableLinuxX64

# macOS (Apple Silicon)
./gradlew runDebugExecutableMacosArm64

# macOS (Intel)
./gradlew runDebugExecutableMacosX64

# Windows
./gradlew runDebugExecutableMingwX64
```

**Build and run release executables:**
```bash
# Linux
./gradlew runReleaseExecutableLinuxX64

# macOS (Apple Silicon)
./gradlew runReleaseExecutableMacosArm64

# macOS (Intel)
./gradlew runReleaseExecutableMacosX64

# Windows
./gradlew runReleaseExecutableMingwX64
```

**Build only (without running):**
```bash
# Build debug executables
./gradlew linkDebugExecutableLinuxX64
./gradlew linkDebugExecutableMacosArm64
./gradlew linkDebugExecutableMacosX64
./gradlew linkDebugExecutableMingwX64

# Build release executables
./gradlew linkReleaseExecutableLinuxX64
./gradlew linkReleaseExecutableMacosArm64
./gradlew linkReleaseExecutableMacosX64
./gradlew linkReleaseExecutableMingwX64
```

#### Executable Locations
Built executables are located in:
- **Debug**: `build/bin/{platform}/debugExecutable/Keyla.kexe` (or `.exe` on Windows)
- **Release**: `build/bin/{platform}/releaseExecutable/Keyla.kexe` (or `.exe` on Windows)

For example:
- Linux debug: `build/bin/linuxX64/debugExecutable/Keyla.kexe`
- macOS release: `build/bin/macosArm64/releaseExecutable/Keyla.kexe`

### Project Structure
```
src/
├── commonMain/     # Shared code
├── jvmMain/        # JVM-specific code
├── linuxX64Main/   # Linux-specific code
└── mingwX64Main/   # Windows-specific code
```

### Git Hooks Setup

This project uses pre-commit and
commit-msg hooks to ensure code quality and conventional commit messages.

#### Install Git Hooks
```bash
./gradlew installGitHooks
```

This command will:
- Copy the pre-commit hook from `scripts/pre-commit` to `.git/hooks/pre-commit`
- Copy the commit-msg hook from `scripts/commit-msg` to `.git/hooks/commit-msg`
- Make both hooks executable

#### What the Hooks Do

**Pre-commit Hook:**
1. Runs code quality checks (ktlint + detekt)
2. Auto-formats code if issues are found
3. Runs all tests across all platforms
4. Prevents commits if any checks fail

**Commit-msg Hook:**
- Validates commit messages follow conventional commit format
- Accepts formats like: `feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, `chore:`, etc.
- Example: `feat: add user authentication` or `fix(api): resolve timeout issue`

#### Manual Testing

You can run the same checks as the pre-commit hook manually:
```bash
# Run all pre-commit checks
./gradlew preCommitCheck

# Format code
./gradlew ktlintFormat

# Run code quality checks
./gradlew checkCode
```

#### Available Gradle Tasks

```bash
# Code Quality
./gradlew ktlintCheck      # Check code style
./gradlew ktlintFormat     # Auto-format code
./gradlew detekt           # Static analysis
./gradlew checkCode        # Run all quality checks
./gradlew fixCode          # Auto-fix code issues

# Git Hooks
./gradlew installGitHooks  # Install/update git hooks
./gradlew preCommitCheck   # Run pre-commit checks manually
```

## License

MIT License
