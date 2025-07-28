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

## Usage

```bash
keyla [mode]

# Available modes:
keyla test      # Start typing test (default)
keyla config    # Manage backend configuration
keyla settings  # Application settings
keyla history   # View test history
keyla profile   # Manage user profiles
keyla stats     # View statistics
```

## Architecture

Built with Kotlin Multiplatform using:
- **Common code**: Shared business logic and UI components
- **Platform-specific**: Native implementations for JVM, Linux, and Windows
- **API services**: HTTP client communication with backend
- **Configuration**: Persistent settings management

## Development

### Prerequisites
- Java 17+
- Gradle 8.0+

### Project Structure
```
src/
├── commonMain/     # Shared code
├── jvmMain/        # JVM-specific code
├── linuxX64Main/   # Linux-specific code
└── mingwX64Main/   # Windows-specific code
```

## License

MIT License