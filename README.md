
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
    *   `cd src`
    
    *  `sudo chmod 755 user_manager.sh`
        
    *  `sudo chmod 755 survival_rate.sh`
        
2.  **Compile the Code**:

    *   `javac Main.java`
        
3.  **Run the Application**:
    
    *   `java Main`
        
4.  **Follow the On-Screen Prompts**:
    
    *   The application will guide you through various operations, including patient registration, profile updates, and survival rate calculations.

Project Structure
-----------------

```
├── README.md
├── assets
│   ├── life-expectancy.csv
│   └── scripts
│       ├── survival_rate.sh
│       └── user_manager.sh
├── credentials.txt
├── data
│   ├── patient_data.csv
│   └── user-store.txt
├── documentation
│   ├── Activity Diagram.jpg
│   ├── ClassDiagram.png
│   └── Explanation of Diagrams.pdf
└── src
    ├── Main.java
    ├── UserManager
    │   ├── models
    │   │   ├── Admin.java
    │   │   ├── Patient.java
    │   │   ├── User.java
    │   │   └── UserRole.java
    │   └── services
    │       └── UserService.java
    ├── helpers
    │   ├── BashConnect.java
    │   ├── DataExport.java
    │   ├── ExportType.java
    │   ├── Help.java
    │   └── UserUtils.java
    └── statistics
        ├── models
        │   ├── Country.java
        │   └── PatientStatistic.java
        └── services
            ├── StatServices.java
            └── SurvivalRate.java
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