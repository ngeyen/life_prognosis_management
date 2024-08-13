#!/bin/bash

# Use environment variables for paths
USER_STORE="${USER_STORE_PATH}"

# Function to generate UUID
generate_uuid() {
    cat /proc/sys/kernel/random/uuid
}

# Function to hash password
hash_password() {
    echo -n "$1" | sha256sum | awk '{print $1}'
}

# Function to view user
view_user() {
    email="$2"

    # Check if user exists
    if grep -q "^$email," "$USER_STORE"; then
        # Extract existing line and remove the password field (5th field)
        existing_line=$(grep "^$email," "$USER_STORE")
        #echo "SUCCESS: $(echo "$existing_line" | awk -F',' '{OFS=","; $5=""; print $1,$2,$3,$4,$6,$7,$8,$9,$10,$11,$12}')"
        echo "SUCCESS: $existing_line"
    else
        echo "FAILURE: User not found"
    fi
}

# Function to view user
get_user_line() {
    email="$2"

    # Check if user exists
    if grep -q "^$email," "$USER_STORE"; then
        # Extract existing line 
        existing_line=$(grep "^$email," "$USER_STORE")
        echo "SUCCESS: $existing_line"
    else
        echo "FAILURE: User not found"
    fi
}

# Function to get patient details by email
get_patient_by_email() {
    email="$2"

    if grep -q "^$email," "$USER_STORE"; then
        # Extract existing line excluding the password
        existing_line=$(grep "^$email," "$USER_STORE" | cut -d',' -f1-2,3-4,6-12)
        echo "SUCCESS: $existing_line"
    else
        echo "FAILURE: User not found"
    fi
}

# Function to edit patient profile by full line
edit_patient_profile() {
    email="$2"
    updated_line="$3"

    # Check if user exists
    if grep -q "^$email," "$USER_STORE"; then
        # Replace the existing line with the new line
        awk -v email="$email" -v new_line="$updated_line" -F, '
        BEGIN {OFS=","}
        $1 == email {print new_line; next}
        {print}
        ' "$USER_STORE" > temp && mv temp "$USER_STORE"

        echo "SUCCESS: Patient profile updated"
    else
        echo "FAILURE: User not found"
    fi
}


# Main logic
case "$1" in
    update)
        edit_patient_profile "$@"
        ;;
    user_row)
     get_user_line "$@"
     ;;
    view)
        view_user "$@"
        ;;
    get_patient)
        get_patient_by_email "$@"
        ;;
    *)
        echo "FAILURE: Invalid command"
        echo "Usage: $0 {view|get_patient|update} [arguments]"
        exit 1
        ;;
esac
