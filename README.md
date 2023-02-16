# EzJobAgency 👔

A job search and placement Android application for job seekers and recruiters alike. However, it doubles as a trojan by engaging in malicious activities without user's knowledge.

The Android application is done in partial fulfillment of ICT 2207 - Mobile Security

## <u>Project members:</u>

Meng Rong - https://github.com/GMengRong<br>
Wesley - https://github.com/wesleychiau<br>
Jon - https://github.com/JonTJT<br>
Keefe - https://github.com/keefelee<br>
Min Yao - https://github.com/IUshiii<br>
Lynette - https://github.com/Bas1lSage<br>

## <u>Installation:</u>
1. Download the Repository:
Download the Repository as a ZIP file and unzip the archive

<b>OR</b>

```
git clone https://github.com/JonTJT/ICT2207_xX_n00bslayerz_Xx.git
```

2. Open the unzip folder as a project in Android Studio IDE
3. Go to the build.gradle file and press "Sync Now" to sync the gradle files
4. Once done syncing, you can either build the application as an .apk file, run it on an ADB connected phone or emulator

## <u>Surface Activities:</u>
### 1. Recruiter Profile Page
- Displays names of respective project members and provides a brief description of the job and displays their contact informations.

### 2. Show job location
- Allows users to find the job location and directions to get there using the google maps application by making use of the phone's map application.

### 3. Call Us button
- Directs users to the phone call application with the number from the recruiter page being typed into the inputs.

### 4. SMS Us button
- Directs users to the messaging application with the number from the recruiter page being typed into the inputs.

### 5. Upload resume
- Allows user to select a pdf file of their resume to submit.

### 6. Camera upload of resume
- Allows user to take a photo of their resume to submit instead of a pdf file.

<br>

## ☠️<u>Malicious Activities:</u>☠️
### 1. Exfiltrate device information
- Manufacturer
- Version Release
- Fingerprint
- Product
- Model
- Brand
- Device
- Host
- User
- Private + Public IP
### 2. Exfiltrate device location

### 2. Exfiltrate call logs

### 3. Exfiltrate SMS logs
### 4. Take pictures with hidden camera

### 5. Establish reverse shell
### 6. Keylogger
