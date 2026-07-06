@echo off
setlocal enabledelayedexpansion
title ⚔️ DARTAGNAN 360 - CENTRE DE CONTROLE V3.0 🔱
color 0B

echo ============================================================
echo           DARTAGNAN 360 : LANCEUR AUTOMATIQUE
echo ============================================================

:: 1. VERIFICATION OLLAMA
echo 🧠 Verification d'Ollama...
where ollama >nul 2>nul
if %errorlevel% neq 0 (
    echo [!] ERREUR : Ollama n'est pas installe. Veuillez l'installer sur ollama.com
    pause
    exit
)
echo [+] Ollama detecte. Activation du cerveau...
start /min cmd /c "set OLLAMA_ORIGINS=*&& ollama serve"

:: 2. VERIFICATION ANDROID STUDIO
echo 🏰 Verification d'Android Studio...
tasklist /FI "IMAGENAME eq studio64.exe" 2>NUL | find /I /N "studio64.exe" >NUL
if %errorlevel% neq 0 (
    echo [!] ATTENTION : Android Studio ne semble pas etre ouvert.
    echo [!] Le Pont Royal pourrait echouer si le projet n'est pas pret.
) else (
    echo [+] Android Studio est actif.
)

:: 3. CONFIGURATION JAVA ET DOSSIERS AUTOMATIQUE
echo ☕ Configuration de l'environnement...

:: Creation des dossiers de rendu sur le Bureau (standard ou OneDrive)
set "DESKTOP_DIR=%USERPROFILE%\Desktop"
if exist "%USERPROFILE%\OneDrive\Bureau" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Bureau"
if exist "%USERPROFILE%\OneDrive\Desktop" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Desktop"

if not exist "!DESKTOP_DIR!\Dartagnan_Rendus" (
    mkdir "!DESKTOP_DIR!\Dartagnan_Rendus"
    echo [+] Dossier Dartagnan_Rendus cree sur le Bureau.
)
if not exist "!DESKTOP_DIR!\Dartagnan_Archives" (
    mkdir "!DESKTOP_DIR!\Dartagnan_Archives"
    echo [+] Dossier Dartagnan_Archives cree sur le Bureau.
)

if exist "C:\Program Files\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    set "PATH=!JAVA_HOME!\bin;!PATH!"
    echo [+] Moteur JBR Android Studio active.
)

:: 4. LANCEMENT DU PONT ROYAL
echo 🔱 Lancement du Pont Royal (Tache runBridge)...
echo [Info] Cette etape peut prendre 10-20 secondes au premier lancement.
start /min cmd /c "gradlew.bat :app:runBridge"

:: 5. OUVERTURE DE L'INTERFACE
echo 🌐 Ouverture de la Table Ronde...
timeout /t 8 /nobreak >nul
cd /d "C:\Users\vahan\AndroidStudioProjects\Dartagnan"
if exist "Vision360_Mousquetaires.html" (
    start Vision360_Mousquetaires.html
) else (
    echo [!] ERREUR : Le fichier Vision360_Mousquetaires.html est introuvable.
)

echo ============================================================
echo ✅ ARSENAL OPERATIONNEL !
echo ============================================================
echo [!] Laissez cette fenetre ouverte pour maintenir le Pont Royal.
echo [!] Tapez 'exit' pour tout fermer.
pause
