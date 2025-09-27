# Java Swing File Reader - Read All Files of Folder

A Java Swing desktop application that allows users to browse and read all files from a selected folder. The application provides a user-friendly GUI with a file tree view and content display area.

## Features

- **Folder Selection**: Browse and select any folder from your file system
- **File Tree View**: Navigate through folder structure with an expandable tree view
- **File Content Display**: View the contents of text files directly in the application
- **File Information**: Display file details including size, path, and last modified date
- **Error Handling**: Graceful handling of binary files and read errors
- **File Size Limit**: Prevents loading of very large files (>1MB) to maintain performance

## Requirements

- Java 8 or higher
- GUI environment (Windows, macOS, or Linux with X11)

## How to Build and Run

### Quick Start (Unix/Linux/macOS)

1. Make the scripts executable:
   ```bash
   chmod +x build.sh run.sh
   ```

2. Build the application:
   ```bash
   ./build.sh
   ```

3. Run the application:
   ```bash
   ./run.sh
   ```

### Manual Build and Run

1. Create build directory:
   ```bash
   mkdir -p build/classes
   ```

2. Compile the Java source:
   ```bash
   javac -d build/classes src/main/java/com/filereader/*.java
   ```

3. Run the application:
   ```bash
   java -cp build/classes com.filereader.FileReaderApp
   ```

### Windows

```cmd
mkdir build\classes
javac -d build\classes src\main\java\com\filereader\*.java
java -cp build\classes com.filereader.FileReaderApp
```

## How to Use

1. **Launch the Application**: Run the application using one of the methods above
2. **Select Folder**: Click the "Select Folder" button to choose a folder to explore
3. **Browse Files**: Use the file tree on the left to navigate through the folder structure
4. **View File Contents**: Click on any file in the tree to view its contents in the text area on the right
5. **File Information**: The content area shows file details including path, size, and last modified date

## Supported File Types

The application can display any text-based file including:
- `.txt` files
- `.java`, `.py`, `.js`, `.html`, `.css` source code files
- `.md`, `.rst` documentation files
- `.json`, `.xml`, `.csv` data files
- Any other text-based files

Binary files will show an error message indicating they cannot be displayed as text.

## Project Structure

```
project-01-javaswing-read-all-files-of-folder/
├── src/main/java/com/filereader/
│   └── FileReaderApp.java          # Main application class
├── build.sh                        # Build script for Unix/Linux/macOS
├── run.sh                          # Run script for Unix/Linux/macOS
└── README.md                       # This file
```

## Technical Details

- **Framework**: Java Swing for GUI components
- **File Handling**: Uses `java.nio.file` for modern file operations
- **Tree Structure**: `JTree` component for folder navigation
- **Content Display**: `JTextArea` with scroll support for file contents
- **Layout**: `BorderLayout` with `JSplitPane` for resizable panels
