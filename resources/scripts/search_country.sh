#!/bin/bash

# Check if an argument is provided
if [ -z "$1" ]; then
    echo "Please provide a search term."
    exit 1
fi

SEARCH_TERM="$1"

# Search for countries matching the input
awk -F',' -v term="$SEARCH_TERM" 'BEGIN { IGNORECASE=1 } 
    $2 ~ term || $3 ~ term || $4 ~ term || $5 ~ term {
        printf "Country: %s, ISO Code: %s\n", $2, $4
    }' "$LIFE_EXPECTANCY_PATH"

