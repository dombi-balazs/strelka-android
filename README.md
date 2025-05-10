# Strelka Android application

## Status

![Project Status: Under Development](https://img.shields.io/badge/Status-Under%20Development-orange?style=for-the-badge)

## Tech Stack

[![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Compose-4285F4.svg?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/jetpack/compose)
![Ktor](https://img.shields.io/badge/ktor-000000.svg?style=for-the-badge&logo=ktor)
[![Supabase](https://img.shields.io/badge/Supabase-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white)](https://supabase.com/)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=gradle&logoColor=white)


## Summary

This repository contains the codes of the Strelka Android app which is one of the apps from the multiplatform Strelka project. If you want to know more about the project, please write me a message. The development plan is under design, it will be published soon. The main goal is to create a multiplatform health manager app, which uses AI to help people stay healthy.

At first I want to create a calorie and step tracker application for mobile and desktop. This is the repository for the mobile app for Android. The application is written in Kotlin and uses Compose for the UI. The app uses Supabase as database, and it has a simple authentication and it uses that for fetching and updating data. So the user can log in, use the default items, but also can add new user items to the food and calorie table.

## Database Key Configuration

The database key is not uploaded to the repository because of safety reasons, but you can easily add your own database key to the project, just create a Kotlin file in the `app/src/main/java/com/aricia/strelka/android/` directory with the name `Keys.kt`, and this should be the content of the created file:

```kotlin
package com.aricia.strelka.android

class Keys {
    object KeysObject {
        const val URL = "Your URL"
        const val KEY = "Your key"
    }
}
``` 

## Authentication
Opening the app the user can log in or register. If the user wants to register, the app needs an email and a password. After that the user gets a confirmation email, which has a link. Clicking on the link the browser opens the application and the user can log in using the registered email and password. Note it, that the app doesn’t have persistent sign in, so if you close the app from the task manager, you have to log in again. The application has an authentication skipping button for testing purposes, but if you skip the authentication the app won’t be able to use the database until you are logged in.

## Main page
The main page has a column Compose component, which contains the back button for testing purposes, the three dot dropdown list, the log out button, and the add user item section with the text input fields and the add button. If you choose an item for the dropdown menu, now the application prints it out on the screen. The goal is develop it to be able to chose items the user ate and calculate the calories the user has eaten and to count the steps and from that data will be able to calculate the calorie number which can be subtracted from the eaten foods calorie values and then, the app can print out the statistics.

## Project structure
For project structure and code related questions please write me a message, but soon I will create the documentation.
