# Dev Container Configuration

This directory contains the development container configuration for the Weather Application project.

## What is included

The `devcontainer.json` file configures a development environment with:

- **Java 21 JDK** - Microsoft build of OpenJDK 21
- **Maven** - Latest version for dependency management  
- **VS Code Extensions**:
  - Java Extension Pack - Complete Java development support
  - Red Hat Java support - Enhanced Java language support
  - Maven for Java - Maven integration
  - Quarkus support - Framework-specific tools

## Features

- **Automatic Setup**: Java 21 and Maven are installed automatically
- **Port Forwarding**: Port 8080 is forwarded for Quarkus dev mode
- **Post-Create Command**: Automatically runs `mvn clean compile` after container creation
- **IDE Integration**: Pre-configured with Java development tools

## Usage

1. Open the project in VS Code
2. Install the "Dev Containers" extension if not already installed
3. Press `Ctrl+Shift+P` (or `Cmd+Shift+P` on Mac)
4. Select "Dev Containers: Reopen in Container"
5. Wait for the container to build and start
6. Start developing with `mvn quarkus:dev`

The container ensures all developers use the same Java 21 environment, eliminating "works on my machine" issues.