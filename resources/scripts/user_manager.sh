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
# Function to edit patient profile
edit_patient() {
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
        role=$(echo "$existing_line" | cut -d',' -f6)  # Extract the role

        # Replace 'keep_current' with the current value from the file
        [ "$first_name" == "keep_current" ] && first_name=$(echo "$existing_line" | cut -d',' -f3)
        [ "$last_name" == "keep_current" ] && last_name=$(echo "$existing_line" | cut -d',' -f4)
        [ "$date_of_birth" == "keep_current" ] && date_of_birth=$(echo "$existing_line" | cut -d',' -f7)
        [ "$hiv_positive" == "keep_current" ] && hiv_positive=$(echo "$existing_line" | cut -d',' -f8)
        [ "$diagnosis_date" == "keep_current" ] && diagnosis_date=$(echo "$existing_line" | cut -d',' -f9)
        [ "$on_art" == "keep_current" ] && on_art=$(echo "$existing_line" | cut -d',' -f10)
        [ "$art_start_date" == "keep_current" ] && art_start_date=$(echo "$existing_line" | cut -d',' -f11)
        [ "$country_iso_code" == "keep_current" ] && country_iso_code=$(echo "$existing_line" | cut -d',' -f12)

        sed -i "s|^$email,$uuid,[^,]*,[^,]*,$password,$role,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*,[^,]*$|$email,$uuid,$first_name,$last_name,$password,$role,$date_of_birth,$hiv_positive,$diagnosis_date,$on_art,$art_start_date,$country_iso_code|" "$USER_STORE"
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
