@echo off
setlocal enabledelayedexpansion
title ⚔️ DARTAGNAN 360 - CENTRE DE CONTROLE V4.2 🔱
color 0B

:: CONFIGURATION DU CHEMIN DU PROJET (CHEMIN ABSOLU)
set "PROJECT_ROOT=C:\Users\vahan\AndroidStudioProjects\Dartagnan"

echo ============================================================
echo           DARTAGNAN 360 : LANCEUR AUTOMATIQUE V4.2
echo ============================================================

:: 1. VERIFICATION OLLAMA
echo 🧠 Verification d'Ollama...
where ollama >nul 2>nul
if %errorlevel% neq 0 (
    echo [!] ERREUR : Ollama n'est pas installe.
    pause
    exit
)
echo [+] Activation du cerveau...
start /min cmd /c "set OLLAMA_ORIGINS=*&& ollama serve"

:: 2. DOSSIERS DE TRAVAIL SUR LE BUREAU
echo 📂 Verification des dossiers sur le Bureau...
set "DESKTOP_DIR=%USERPROFILE%\Desktop"
if exist "%USERPROFILE%\OneDrive\Bureau" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Bureau"
if exist "%USERPROFILE%\OneDrive\Desktop" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Desktop"

mkdir "!DESKTOP_DIR!\Dartagnan_Rendus" 2>nul
mkdir "!DESKTOP_DIR!\Dartagnan_Archives" 2>nul
echo [+] Dossiers prets sur : !DESKTOP_DIR!

:: 3. CONFIGURATION JAVA
if exist "C:\Program Files\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    set "PATH=!JAVA_HOME!\bin;!PATH!"
)

:: 4. LANCEMENT DU PONT ROYAL
echo 🔱 Lancement du Pont Royal...
cd /d "!PROJECT_ROOT!"
if not exist "gradlew.bat" (
    echo [!] ERREUR : gradlew.bat introuvable dans !PROJECT_ROOT!
    pause
    exit
)
:: Lancement de la tâche Gradle dans une fenêtre séparée
start "🔱 DARTAGNAN BRIDGE" cmd /c "gradlew.bat :app:runBridge"

:: 5. OUVERTURE DE L'INTERFACE
echo 🌐 Ouverture de la Table Ronde...
timeout /t 10 /nobreak >nul
if exist "Vision360_Mousquetaires.html" (
    start Vision360_Mousquetaires.html
) else (
    echo [!] ERREUR : Vision360_Mousquetaires.html introuvable dans !PROJECT_ROOT!
)

echo ============================================================
echo ✅ SYSTEME LANCE ! (Verifiez la fenetre 'DARTAGNAN BRIDGE')
echo ============================================================
pause
