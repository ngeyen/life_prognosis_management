#!/bin/bash

USER_STORE="${USER_STORE_PATH}"

# Function to generate UUID
generate_uuid() {
    cat /proc/sys/kernel/random/uuid
}

# Function to hash password
hash_password() {
    echo -n "$1" | sha256sum | awk '{print $1}'
}

# Function to initialize patient registration
init_registration() {
    email="$2"
    uuid=$(generate_uuid)
    echo "$email,$uuid" >> "$USER_STORE"
    echo "SUCCESS:$uuid"
}

# Check if the UUID exists in the user-store.txt
is_uuid_valid() {
    uuid="$1"
    if grep -q ",$uuid," "$USER_STORE"; then
        echo "SUCCESS: UUID exists"
    else
        echo "FAILURE: UUID does not exist"
    fi
}

# Function to register a new user
register_user() {
    first_name="$2"
    last_name="$3"
    email="$4"
    password="$5"
    role="$6"

    # Hash the password
    hashed_password=$(hash_password "$password")

    # Check if the email already exists (excluding the initialization entry)
    if grep -q "^$email,[^,]*$" "$USER_STORE"; then
        existing_line=$(grep "^$email," "$USER_STORE")
        uuid=$(echo "$existing_line" | cut -d',' -f2)
    else
        echo "FAILURE: User not initialized"
        exit 1
    fi

    if [ "$role" == "patient" ]; then
        date_of_birth="$7"
        hiv_positive="$8"
        diagnosis_date="$9"
        on_art="${10}"
        art_start_date="${11}"
        country_iso_code="${12}"

        # Update the existing line with full information
        sed -i "s|^$email,$uuid$|$email,$uuid,$first_name,$last_name,$hashed_password,$role,$date_of_birth,$hiv_positive,$diagnosis_date,$on_art,$art_start_date,$country_iso_code|" "$USER_STORE"
    else
        # For admin, just update with basic info
        sed -i "s|^$email,$uuid$|$email,$uuid,$first_name,$last_name,$hashed_password,$role|" "$USER_STORE"
    fi

    echo "SUCCESS: User registered successfully"
}

# Main logic
case "$1" in
    initialize)
        init_registration "$@"
        ;;
    register)
        register_user "$@"
        ;;

    search_uuid)
        is_uuid_valid "$@"
        ;;
    *)
        echo "FAILURE: Invalid command"
        echo "Usage: $0 {initialize|register|login|update|search_uuid} [arguments]"
        exit 1
        ;;
esac