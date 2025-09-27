#!/bin/bash

# Run script for Java Swing File Reader Application

echo "Running Java Swing File Reader Application..."

# Check if build directory exists
if [ ! -d "build/classes" ]; then
    echo "Build directory not found. Running build first..."
    ./build.sh
fi

# Run the application
java -cp build/classes com.filereader.FileReaderApp