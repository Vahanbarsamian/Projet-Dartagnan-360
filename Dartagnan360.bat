@echo off
setlocal enabledelayedexpansion
title ⚔️ DARTAGNAN 360 - CENTRE DE CONTROLE V4.8 🔱
color 0B

set "PROJECT_ROOT=C:\Users\vahan\AndroidStudioProjects\Dartagnan"

echo ============================================================
echo           DARTAGNAN 360 : LANCEUR AUTOMATIQUE V4.8
echo ============================================================

:: 1. PURGE TOTALE DES PROCESSUS
echo 🧹 Purge de la zone de combat...
taskkill /f /im "ollama*" /t >nul 2>nul
taskkill /f /im "llama*" /t >nul 2>nul

:: 2. VERIFICATION GPU
nvidia-smi >nul 2>nul
if %errorlevel% equ 0 (echo [+] Carte NVIDIA prete.) else (echo [!] GPU non detecte.)

:: 3. REVEIL OLLAMA AVEC AUTORISATION STRICTE
echo 🧠 Activation du Cerveau (Liaison Royale)...
set "OLLAMA_EXE=ollama"
where ollama >nul 2>nul
if %errorlevel% neq 0 (set "OLLAMA_EXE=%LOCALAPPDATA%\Ollama\ollama.exe")

:: Force la variable sans aucun espace parasite
set OLLAMA_ORIGINS=*
set OLLAMA_HOST=127.0.0.1

:: Lancement sans 'start /min' pour voir les messages d'erreur en cas de crash
start "🧠 OLLAMA ENGINE" cmd /c "set OLLAMA_ORIGINS=*& set OLLAMA_HOST=127.0.0.1& \"!OLLAMA_EXE!\" serve"

:: 4. DOSSIERS ET JAVA
set "DESKTOP_DIR=%USERPROFILE%\Desktop"
if exist "%USERPROFILE%\OneDrive\Bureau" set "DESKTOP_DIR=%USERPROFILE%\OneDrive\Bureau"
mkdir "!DESKTOP_DIR!\Dartagnan_Rendus" 2>nul
mkdir "!DESKTOP_DIR!\Dartagnan_Archives" 2>nul

if exist "C:\Program Files\Android\Android Studio\jbr" (
    set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    set "PATH=!JAVA_HOME!\bin;!PATH!"
)

:: 5. LANCEMENT DU PONT ROYAL
echo 🔱 Activation du Pont Royal...
cd /d "!PROJECT_ROOT!"
start "🔱 DARTAGNAN BRIDGE" cmd /c "gradlew.bat :app:runBridge"

:: 6. OUVERTURE INTERFACE
echo 🌐 Ouverture de la Table Ronde...
timeout /t 10 /nobreak >nul
start Vision360_Mousquetaires.html

echo ============================================================
echo ✅ ARSENAL OPERATIONNEL !
echo ============================================================
pause
