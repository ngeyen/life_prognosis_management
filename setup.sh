#!/bin/bash
clear
# Get the absolute path to the root directory
ROOT_DIR=$(dirname "$(readlink -f "$0")")

# Set environment variables using relative paths
export USER_MANAGER_SCRIPT="$ROOT_DIR/resources/scripts/user_manager.sh"
export SURVIVAL_RATE_SCRIPT="$ROOT_DIR/resources/scripts/survival_rate.sh"
export SEARCH_COUNTRIES_SCRIPT="$ROOT_DIR/resources/scripts/search_country.sh"
export AUTH_SCRIPT="$ROOT_DIR/resources/scripts/auth.sh"
export REGISTER_SCRIPT="$ROOT_DIR/resources/scripts/register.sh"

export USER_STORE_PATH="$ROOT_DIR/resources/data/user-store.txt"
export LIFE_EXPECTANCY_PATH="$ROOT_DIR/resources/data/life-expectancy.csv"
export PIN_LENGTH=4  # Example of a non-path environment variable
export PATIENT_DATA_PATH="$ROOT_DIR/downloads/patient_data.csv"


# Make all relevant scripts executable
chmod +x "$USER_MANAGER_SCRIPT"
chmod +x "$SURVIVAL_RATE_SCRIPT"
chmod +x "$AUTH_SCRIPT"
chmod +x "$REGISTER_SCRIPT"
chmod +x "$SEARCH_COUNTRIES_SCRIPT"

echo "Environment variables set and scripts are now executable."

echo "Building your application...."
# Find all .java files in the src directory
JAVA_FILES=$(find "$ROOT_DIR/src" -name "*.java")

# Compile Java files, output to the bin directory
javac -d "$ROOT_DIR/bin" $JAVA_FILES

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "Build Complete!."
    
    echo "Launching app...."
    # Wait for 3 seconds before launching
    sleep 3
    clear
    # Run the Java application
    java -cp "$ROOT_DIR/bin" Main
else
    echo "Build failed. Please check the errors above and fix them."
fi