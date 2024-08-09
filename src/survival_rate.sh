#!/bin/bash

USER_STORE="user-store.txt"
LIFE_EXPECTANCY_FILE="life-expectancy.csv"

# Function to get life expectancy
get_life_expectancy() {
    email="$2"

    # Find the user by email in the user store
    user_data=$(grep "^$email," "$USER_STORE")

    if [ -z "$user_data" ]; then
        echo "FAILURE: User not found"
        return
    fi

    # Extract country ISO code (assuming it's the 12th field in the user_data)
    country_iso_code=$(echo "$user_data" | awk -F ',' '{print $12}')

    if [ -z "$country_iso_code" ]; then
        echo "FAILURE: Country ISO code not found"
        return
    fi
    # Convert the country_iso_code to uppercase
    country_iso_code=$(echo "$country_iso_code" | tr '[:lower:]' '[:upper:]')

    # Search for the life expectancy in the CSV file (extracting the 7th column where life expectancy is stored)
    life_expectancy=$(awk -F ',' -v code="$country_iso_code" '$4 == code {print $7}' $LIFE_EXPECTANCY_FILE)

    if [ -z "$life_expectancy" ]; then
        echo "FAILURE: Country's life expectancy not found"
    else
        echo "SUCCESS: $life_expectancy"
    fi
}

# Main logic
case "$1" in
    country_le)
        get_life_expectancy "$@"
        ;;
    # other cases...
    *)
        echo "FAILURE: Invalid command"
        echo "Usage: $0 {country_le} [email]"
        exit 1
        ;;
esac
