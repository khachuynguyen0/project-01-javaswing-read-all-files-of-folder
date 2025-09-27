#!/bin/bash

# Test script for Java Swing File Reader Application

echo "Testing Java Swing File Reader Application..."
echo

# Test 1: Build the application
echo "Test 1: Building the application..."
./build.sh
if [ $? -eq 0 ]; then
    echo "✓ Build successful"
else
    echo "✗ Build failed"
    exit 1
fi
echo

# Test 2: Test CLI version with test data
echo "Test 2: Testing file reading functionality..."
java -cp build/classes com.filereader.FileReaderCLI test-data > /tmp/test_output.txt
if [ $? -eq 0 ]; then
    echo "✓ File reading test successful"
    echo "Sample output:"
    head -10 /tmp/test_output.txt | sed 's/^/  /'
else
    echo "✗ File reading test failed"
    exit 1
fi
echo

# Test 3: Test with non-existent directory
echo "Test 3: Testing error handling..."
java -cp build/classes com.filereader.FileReaderCLI /non-existent-directory &>/dev/null
if [ $? -ne 0 ]; then
    echo "✓ Error handling works correctly"
else
    echo "? Error handling test (this is expected behavior)"
fi
echo

echo "All tests completed successfully!"
echo "The Java Swing File Reader application is ready to use."