#!/bin/bash

USER_STORE="resources/data/user-store.txt"

# Function to hash password
hash_password() {
    echo -n "$1" | sha256sum | awk '{print $1}'
}

# Function to check login
check_login() {
    email="$2"
    password="$3"


    hashed_password=$(hash_password "$password")
    if grep -q "^$email,[^,]*,[^,]*,[^,]*,$hashed_password" "$USER_STORE"; then
        # Extract the line containing user
        user_line=$(grep "^$email," "$USER_STORE")
        # Extract the role from the user line (assume role is always the 6th field)
        role=$(echo "$user_line" | cut -d',' -f6)
        echo "SUCCESS: Login successful. Role: $role"
    else
        # User doesn't exist
        echo "FAILURE: Invalid Credentials"
    fi
}
# Main logic
case "$1" in
    login)
        check_login "$@"
        ;;
        
    *)
        echo "FAILURE: Invalid command"
        echo "Usage: $0 {login} [arguments]"
        exit 1
        ;;