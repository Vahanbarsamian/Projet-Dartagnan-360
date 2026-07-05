@echo off
title DARTAGNAN 360 - LAUNCHER
echo ⚔️ INITIALISATION DE L'ARSENAL D'ARTAGNAN 🔱
echo ------------------------------------------

echo 🧠 Etape 1: Demarrage du cerveau (Ollama)...
start /min powershell -Command "$env:OLLAMA_ORIGINS='*'; ollama serve"

echo 🔱 Etape 2: Activation du Pont Royal...
:: Utilisation de la nouvelle tache Gradle runBridge (plus robuste)
start /min cmd /c "gradlew.bat :app:runBridge"

echo 🏰 Etape 3: Ouverture de la Table Ronde...
timeout /t 5
start Vision360_Mousquetaires.html

echo ------------------------------------------
echo ✅ TOUT EST PRET, MON CAPITAINE !
echo Laissez cette fenetre ouverte tant que vous travaillez.
echo ------------------------------------------
pause
