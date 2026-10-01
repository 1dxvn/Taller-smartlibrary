# SmartLibrary - Relationships and Contracts (Java project)

Requirements: JDK 11 or newer (`java -version`). No external libraries.

## Folder structure

    smartlibrary/
    |-- README.md
    `-- src/main/java/com/smartlibrary/
        |-- Main.java
        `-- model/
            |-- Notifiable.java
            |-- User.java
            |-- Student.java
            |-- Librarian.java
            |-- Book.java
            |-- Copy.java
            |-- Loan.java
            `-- Renewal.java

## Compile and run (Linux / macOS)

    cd smartlibrary
    mkdir -p out
    javac -d out $(find src -name "*.java")
    java -cp out com.smartlibrary.Main

## Compile and run (Windows, PowerShell)

    cd smartlibrary
    mkdir out
    javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
    java -cp out com.smartlibrary.Main

The program exits with code 0 when every test passes and 1 otherwise.
