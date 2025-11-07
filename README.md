[![Android CI](https://github.com/DavidTOuyang/BuzzGuy/actions/workflows/android-ci.yaml/badge.svg)](https://github.com/DavidTOuyang/BuzzGuy/actions/workflows/android-ci.yaml)
[![GitHub release](https://img.shields.io/github/v/release/DavidTOuyang/BuzzGuy)](https://github.com/DavidTOuyang/BuzzGuy/releases/latest)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![GitHub Stars](https://img.shields.io/github/stars/DavidTOuyang/BuzzGuy?style=social)](https://github.com/DavidTOuyang/BuzzGuy/stargazers)

# BuzzGuy

<div align="center">
    <img src="/app/src/main/ic_launcher-playstore.png" alt="BuzzGuy Logo" width="150" height="150">
</div>

## Description

BuzzGuy is an AI-powered Android app designed to support students' academic motivation and 
well-being by offering a mix of open-response and guided-response explanations to help reduce stress.

The project is built using technologies such as Kotlin, XML, Google Firebase (Authentication &
Firestore), Gemini API, Google Cloud Platform, and Google Play Console. To ensure industry-level quality, 
the app also incorporates automated unit tests, instrumented tests, and integrates with a DevOps CI/CD pipeline.

This project is part of an educational technology research initiative at the Georgia Institute of Technology. 
Students and researchers interested in exploring similar areas or expanding their studies can use this 
project as a template. The repository includes testing and CI/CD processes, which are often rare in 
similar projects. Feel free to explore and build upon it for your own research.

## Technology Used

Here is the list of technologies used in this project, for convenience:

* [![Android Studio][AndroidStudio-url]][AndroidStudio-site-url]
* [![Kotlin][Kotlin-url]][Kotlin-site-url]
* [![XML][XML-url]][XML-site-url]
* [![Firebase][Firebase-url]][Firebase-site-url]
* [![Google Cloud][GCP-url]][GCP-site-url]
* [![Gemini API][Gemini-url]][Gemini-site-url]

## Installation

<!-- start installation -->
### Setup Android Studio

You may download Android studio from the following link: [Android Studio](https://developer.android.com/studio).

### Clone the Repository

Call `git clone https://github.com/DavidTOuyang/BuzzGuy.git` in the terminal to clone the repository.

### Setup Google Firebase

This project requires a `google-services.json` file from Firebase to enable backend services (like Authentication) and the Gemini API.
1.  **Create a Firebase Project:** Follow the official documentation to [create a new Firebase project and add it to your Android app](https://firebase.google.com/docs/android/setup).
2.  **Enable Gemini:** The Gemini API is part of Google Cloud. In your new project's Google Cloud Console, ensure the **"Generative Language API"** is enabled.
3.  **Download and Place File:** From the Firebase console, download the `google-services.json` file and place it in the `/app/` directory of this project.

The app will not build or run correctly without this file.


### Sync Project with Gradle Files

Once you locate the `google-services.json` file in the `/app/` directory, sync the project by clicking 
the small elephant icon in the top-right corner of the menu bar.

### Fine-Tuning the AI's Personality

One of the key features of this project is the ability to easily customize the AI's behavior and personality. 
All AI configuration is centralized in the `GenerativeAIHelper.kt` object, 
located at `/app/src/main/java/com/mygroup/buzzguy/GenerativeAIHelper.kt`

Inside this file, you can modify several key parameters:

#### 1. System Prompt (`chatbotSystemInstruction`)

This is where you define the core personality of 'BuzzGuy'. You can change its name, its primary goal, its tone, and the rules it must follow. This is the most powerful way to alter the user experience.

#### 2. Model Configuration (`config`)

You can fine-tune the technical parameters of the Gemini model, such as:
*   `temperature`: Controls the creativity of the responses. Lower values are more predictable; higher values are more creative.
*   `maxOutputTokens`: Determines the maximum length of the AI's response.
*   `topK` and `topP`: Adjust how the model selects words, affecting the coherence and randomness of its answers.

#### 3. Safety Settings (`safety`)

This list defines the content moderation level for topics like harassment and hate speech. 
You can adjust the `HarmBlockThreshold` to make the AI more or less strict.

Feel free to experiment with these settings to create a chatbot that fits your own 
preferences or research needs.

### Run BuzzGuy

Once you finish syncing the Gradle files, simply click the Run icon at the top.

### Run Unit Test

You can run the unit test by right-clicking the `test [unitTest]` folder located at
`/app/test[unitTest]` in the Project window. Then, select `Run Tests`.

### Run Instrumented Test (Android Test)

Similar to the Unit Test, you can run the instrumented tests by right-clicking the `androidTest` 
folder located at `/app/src/androidTest` in the Project window. Then, select `Run All Tests`.

### Run the GitHub CI/CD Pipeline

This is optional. You may use the `android-ci.yaml` and `action.yaml` files as references to create 
your own CI/CD pipeline. Ideally, when you push your code, the GitHub Runner will detect the update 
and automatically start the workflow.

To make the CI/CD pipeline work, you have to have the following secret values:

* BUZZGUY_GOOGLE_SERVICES_JSON
* KEYSTORE_FILE
* KEY_ALIAS
* KEY_PASSWORD
* KEY_STORE_PASSWORD

The `.gitignore` file excludes the `google-service.json` file, since it contains credential information.
So, you have to encrypt the file in BASE64 and save the string in the `BUZZGUY_GOOGLE_SERVICES_JSON` secret.

You can use Powershell command to encrypt the file:

```
powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("google-services.json")) | Out-File -FilePath "google-services.b64"
```

You can generate a keystore file by following the official Android Studio documentation or using the command below.

1. Open a terminal or command prompt.
2. Navigate to a secure folder where you want to store the keystore file (e.g., your user home directory).
3. Run the following keytool command. Replace your-key-alias and your-keystore-name.jks with your own values.

``` 
> keytool -genkey -v -keystore your-keystore-name.jks -alias your-key-alias -keyalg RSA -keysize 2048 -validity 10000
> 
>
> The tool will then prompt you to create a password for the keystore and another for the key alias. 
It will also ask for your name, organization, and location. **Remember these passwords**, as you will need to add them as secrets to GitHub Actions.
```

To store this `.jks` file in the `KEYSTORE_FILE` secret, you must first Base64 encode it. Run the appropriate command for your system:

*   **On macOS or Linux:**
```
sh
base64 your-keystore-name.jks > your-keystore-name.jks.b64
```

*   **On Windows (PowerShell):**
```
powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("your-keystore-name.jks")) | Out-File -FilePath "your-keystore-name.jks.b64"
```

# Contribution
Here is a list of contributors who supported this project:
<a href="https://github.com/DavidTOuyang/BuzzGuy/graphs/contributors">
    <img src="https://contrib.rocks/image?repo=DavidTOuyang/BuzzGuy" />
</a>

# License

The scripts and documentation in this project are released under the [MIT License](LICENSE)

[AndroidStudio-url]: https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=AndroidStudio&logoColor=white
[AndroidStudio-site-url]: https://developer.android.com/studio
[Kotlin-url]: https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin-site-url]: https://kotlinlang.org
[XML-url]: https://img.shields.io/badge/XML-4C4C4C?&style=for-the-badge&logo=prolog&logoColor=white
[XML-site-url]: https://en.wikipedia.org/wiki/XML
[Firebase-url]: https://img.shields.io/badge/Firebase-FFCA28?&style=for-the-badge&logo=firebase&logoColor=black
[Firebase-site-url]: https://firebase.google.com
[GCP-url]: https://img.shields.io/badge/Google_Cloud-4285F4?&style=for-the-badge&logo=google-cloud&logoColor=white
[GCP-site-url]: https://cloud.google.com
[Gemini-url]: https://img.shields.io/badge/Gemini_API-8E77F0?&style=for-the-badge&logo=google-gemini&logoColor=white
[Gemini-site-url]: https://ai.google.dev/