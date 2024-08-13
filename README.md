
Life Prognosis Management Tool
==============================

This Life Prognosis Management System is designed to handle patient registrations, profile updates, and calculate survival rates based on various health parameters. The system also includes administrative functions for managing patient information, downloading relevant data, and onboarding new administrators.

Table of Contents
-----------------
*   [Getting Started](#getting-started)

*   [Project Structure](#project-structure)
    
*   [Features](#features)
    
*   [Configuration](#configuration)
    
*   [Usage](#usage)
    
*   [Bash Scripts](#bash-scripts)
    
*   [Requirements](#requirements)
    
*   [Future Enhancements](#future-enhancements)
    

Getting Started
---------------

### Prerequisites

1.  **Java 11 or Above**: Make sure you have Java 11 or a newer version installed on your machine. \`\`\`java -version\`\`\`
    
2.  **Bash Shell**: Ensure that you have a Bash shell environment available, as the application relies on Bash scripts for certain operations.
    
**Test AdminUser Credentials**
```
email: admin@example.com
password: admin123
```

**Test Patient Credentials**
```
email: patient@example.com
password: patient123
```

### Setup Instructions

1.  **Configure the Scripts**:
    ```
    cd project_directory

    sudo chmod 755 setup.sh
    ./setup.sh

    ````

You might encounter an issue with the setup script. due to some unwanted characters stored in the script. the following command would help

    `sed -i 's/\r$//' ./setup.sh`
    
After the application executes, it will guide you through various operations, including patient registration, profile updates, and survival rate calculations.

Project Structure
-----------------

```
.
├── README.md
├── credentials.txt
├── downloads
│   └── patient_data.csv
├── resources
│   ├── data
│   │   ├── life-expectancy.csv
│   │   └── user-store.txt
│   └── scripts
│       ├── auth.sh
│       ├── register.sh
│       ├── search_country.sh
│       ├── survival_rate.sh
│       └── user_manager.sh
├── setup.sh
└── src
    ├── Main.java
    ├── accounts
    │   ├── controllers
    │   │   ├── AuthController.java
    │   │   ├── PatientRegistrationController.java
    │   │   ├── ProfileController.java
    │   │   └── RegistrationController.java
    │   ├── models
    │   │   ├── Admin.java
    │   │   ├── Patient.java
    │   │   └── User.java
    │   └── services
    │       ├── AuthService.java
    │       ├── RegistrationService.java
    │       └── UserManagementService.java
    ├── core
    │   ├── AppConfig.java
    │   ├── BashConnect.java
    │   └── Docs.java
    ├── dashboard
    │   ├── controllers
    │   │   ├── AdminDashboardController.java
    │   │   └── PatientDashboardController.java
    │   └── services
    ├── datacompute
    │   ├── models
    │   │   ├── Country.java
    │   │   └── PatientStatistics.java
    │   └── services
    │       ├── CountryService.java
    │       ├── StatServices.java
    │       └── SurvivalRate.java
    └── utils
        ├── enums
        │   ├── ExportType.java
        │   └── UserRole.java
        ├── interractions
        │   └── Reset.java
        ├── user
        │   ├── DataExport.java
        │   ├── PatientDetailsUpdater.java
        │   ├── RegistrationUtils.java
        │   └── SessionUtils.java
        └── validators
            ├── BinaryDecisionValidator.java
            ├── DateValidator.java
            ├── EmailValidator.java
            ├── InputValidator.java
            ├── PasswordValidator.java
            └── Validator.java

```

Features
--------

### 1\. Complete Registration

*   Patients can complete their registration by providing essential details like name, date of birth, HIV status, and country of residence.
    
*   UUID validation ensures that only pre-registered patients can complete the registration process.
    

### 2\. Admin Initializes Registration

*   Admins can initialize a patient's registration by generating a unique UUID.
    
*   This UUID is then used by the patient to complete the registration process.
    

### 3\. Profile Update

*   Patients can update their profile information, including their name, HIV status, diagnosis date, and ART start date.
    
*   The system allows selective updates, meaning patients can choose specific fields to update without needing to provide all information again.
    

### 4\. Survival Rate Calculation

*   The system calculates the survival rate based on the patient's age, diagnosis date, and ART start date.
    
*   Different scenarios are handled, including patients who are not on ART, those who started ART immediately, and those who delayed starting ART.
    

### 5\. Download Death Schedule ICS File

*   Patients can download an ICS file with their calculated survival rate, which can be imported into calendar applications.
    

### 6\. Admin Can Download Patient Info

*   Admins can download detailed patient information in CSV format for analysis or record-keeping purposes.
    

### 7\. Admin Can Onboard New Admins

*   Admins have the ability to onboard new administrators, allowing them to manage the system.
    

Usage
-----

### Running the System

1.  **Compile the Java Classes**: Ensure all Java classes are compiled.
    
2.  **Execute the Main Class**: Run the Main class to interact with the system.
    

### Example Commands

*   **Register a New Patient:** Follow the on-screen prompts to register a new patient.
    
*   **Update Patient Profile:** Follow the prompt, Provide the new values and update
    

Bash Scripts
------------

### 1\. `survival_rate.sh`

*   Calculates the survival rate based on life expectancy data and patient information.
    

### 2\. `user_manager.sh`

*   interacts with the user-store.txt file.
    

Requirements
------------

*   **Java 11 or higher**
    
*   **Bash** (for executing the scripts)
    
*   **CSV file** for life expectancy data
    

Future Enhancements
-------------------

*   Implement additional validation for other fields.
    
*   Add encryption for stored data, especially for sensitive information like Passwords.
    
*   Improve user experience

*   Lots more..