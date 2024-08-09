#!/bin/bash

USER_STORE="user-store.txt"

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
# Function to view user
view_user() {
    email="$2"

    # Check if user exists
    if grep -q "^$email," "$USER_STORE"; then
        # Extract existing line and remove the password field (5th field)
        existing_line=$(grep "^$email," "$USER_STORE")
        echo "SUCCESS: $(echo "$existing_line" | awk -F',' '{OFS=","; $5=""; print $1,$2,$3,$4,$6,$7,$8,$9,$10,$11,$12}')"
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

#Function to edit patient profile
edit_patient_profile() {
    email="$2"
    first_name="$3"
    last_name="$4"
    date_of_birth="$5"
    hiv_positive="$6"
    diagnosis_date="$7"
    on_art="$8"
    art_start_date="$9"
    country_iso_code="${10}"

    # Check if user exists
    if grep -q "^$email," "$USER_STORE"; then
        # Extract existing line
        existing_line=$(grep "^$email," "$USER_STORE")
        uuid=$(echo "$existing_line" | cut -d',' -f2)
        password=$(echo "$existing_line" | cut -d',' -f5)

        # Replace 'keep_current' with the current value from the file
        [ "$first_name" == "keep_current" ] && first_name=$(echo "$existing_line" | cut -d',' -f3)
        [ "$last_name" == "keep_current" ] && last_name=$(echo "$existing_line" | cut -d',' -f4)
        [ "$date_of_birth" == "keep_current" ] && date_of_birth=$(echo "$existing_line" | cut -d',' -f7)
        [ "$hiv_positive" == "keep_current" ] && hiv_positive=$(echo "$existing_line" | cut -d',' -f8)
        [ "$diagnosis_date" == "keep_current" ] && diagnosis_date=$(echo "$existing_line" | cut -d',' -f9)
        [ "$on_art" == "keep_current" ] && on_art=$(echo "$existing_line" | cut -d',' -f10)
        [ "$art_start_date" == "keep_current" ] && art_start_date=$(echo "$existing_line" | cut -d',' -f11)
        [ "$country_iso_code" == "keep_current" ] && country_iso_code=$(echo "$existing_line" | cut -d',' -f12)

        sed -i "s|^$email,$uuid,[^,]*,[^,]*,$password,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*$|$email,$uuid,$first_name,$last_name,$password,$date_of_birth,$hiv_positive,$diagnosis_date,$on_art,$art_start_date,$country_iso_code|" "$USER_STORE"
        echo "SUCCESS: Patient profile updated"
    else
        echo "FAILURE: User not found"
    fi
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
    initialize)
        init_registration "$@"
        ;;
    register)
        register_user "$@"
        ;;
    login)
        check_login "$@"
        ;;
    update)
        edit_patient_profile "$@"
        ;;
    view)
        view_user "$@"
        ;;
    get_patient)
        get_patient_by_email "$@"
        ;;
    *)
        echo "FAILURE: Invalid command"
        echo "Usage: $0 {initialize|register|login|update|view|get_patient} [arguments]"
        exit 1
        ;;
esac