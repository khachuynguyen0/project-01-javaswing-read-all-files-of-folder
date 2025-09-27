#!/bin/bash

# Build script for Java Swing File Reader Application

echo "Building Java Swing File Reader Application..."

# Create build directory
mkdir -p build/classes

# Compile Java source files
javac -d build/classes src/main/java/com/filereader/*.java

if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "To run the application, execute: ./run.sh"
else
    echo "Build failed!"
    exit 1
fi