#!/bin/bash

USER_STORE="user-store.txt"
LIFE_EXPECTANCY_FILE="life-expectancy.csv"

# Function to get life expectancy
get_life_expectancy() {
    email="$2"

    # Check if the user exists in the USER_STORE
    if grep -q "^$email," "$USER_STORE"; then
        # Extract the user's country ISO code
        country_iso=$(grep "^$email," "$USER_STORE" | awk -F',' '{print $12}')

        # Search for the country ISO code in the life-expectancy.csv file
        life_expectancy=$(awk -F',' -v iso="$country_iso" '$5 == iso {print $7}' "$LIFE_EXPECTANCY_FILE")

        if [ -z "$life_expectancy" ]; then
            echo "FAILURE: Country's life expectancy not found"
        else
            echo "SUCCESS: $life_expectancy"
        fi
    else
        echo "FAILURE: User not found"
    fi
}

# Main logic
case "$1" in
    get_life_expectancy)
        get_life_expectancy "$@"
        ;;
    # other cases...
    *)
        echo "FAILURE: Invalid command"
        echo "Usage: $0 {get_life_expectancy} [email]"
        exit 1
        ;;
esac
