@echo off
setlocal enabledelayedexpansion
title ⚔️ DARTAGNAN 360 - CENTRE DE CONTROLE V4.4 🔱
color 0B

set "PROJECT_ROOT=C:\Users\vahan\AndroidStudioProjects\Dartagnan"

echo ============================================================
echo           DARTAGNAN 360 : LANCEUR AUTOMATIQUE V4.4
echo ============================================================

:: 1. DESTRUCTION DES INSTANCES REBELLES
echo 🧹 Nettoyage des processus...
taskkill /f /im "ollama.exe" >nul 2>nul
taskkill /f /im "ollama app.exe" >nul 2>nul

:: 2. VERIFICATION ET REVEIL OLLAMA
echo 🧠 Preparation du cerveau...
set "OLLAMA_EXE=ollama"
where ollama >nul 2>nul
if %errorlevel% neq 0 (
    if exist "%LOCALAPPDATA%\Ollama\ollama.exe" (
        set "OLLAMA_EXE=%LOCALAPPDATA%\Ollama\ollama.exe"
    ) else (
        echo [!] ERREUR : Ollama introuvable.
        pause
        exit
    )
)

:: Force l'autorisation CORS au niveau local de la session
set "OLLAMA_ORIGINS=*"
echo [+] Autorisation CORS injectee : !OLLAMA_ORIGINS!

:: Lancement d'Ollama dans une fenetre separee avec la variable FORCEE
start "🧠 OLLAMA ENGINE" cmd /c "set OLLAMA_ORIGINS=*&& \"!OLLAMA_EXE!\" serve"

:: 3. DOSSIERS DE TRAVAIL
echo 📂 Verification des dossiers sur le Bureau...
set "DESKTOP_DIR=%USERPROFILE%\Desktop"
if exist "%USERPROFILE%\OneDrive\Bureau" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Bureau"
if exist "%USERPROFILE%\OneDrive\Desktop" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Desktop"

mkdir "!DESKTOP_DIR!\Dartagnan_Rendus" 2>nul
mkdir "!DESKTOP_DIR!\Dartagnan_Archives" 2>nul

:: 4. CONFIGURATION JAVA
if exist "C:\Program Files\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    set "PATH=!JAVA_HOME!\bin;!PATH!"
)

:: 5. LANCEMENT DU PONT ROYAL
echo 🔱 Lancement du Pont Royal...
cd /d "!PROJECT_ROOT!"
start "🔱 DARTAGNAN BRIDGE" cmd /c "gradlew.bat :app:runBridge"

:: 6. OUVERTURE DE L'INTERFACE
echo 🌐 Ouverture de la Table Ronde...
timeout /t 12 /nobreak >nul
start Vision360_Mousquetaires.html

echo ============================================================
echo ✅ ARSENAL OPERATIONNEL ! (V4.4)
echo ============================================================
pause
