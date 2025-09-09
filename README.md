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

### Git Hooks Setup

This project uses pre-commit and commit-msg hooks to ensure code quality and conventional commit messages.

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
