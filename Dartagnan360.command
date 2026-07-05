#!/bin/bash
# LANCEUR DARTAGNAN 360 POUR macOS - V1.1
echo "⚔️ INITIALISATION DE L'ARSENAL D'ARTAGNAN 🔱"
echo "------------------------------------------"

# Etape 1 : Cerveau (Ollama)
echo "🧠 Etape 1: Demarrage du cerveau (Ollama)..."
export OLLAMA_ORIGINS="*"
ollama serve &

# Etape 2 : Messager (Pont Royal)
echo "🔱 Etape 2: Activation du Pont Royal..."
cd "$(dirname "$0")"
chmod +x gradlew
./gradlew :app:runBridge &

# Etape 3 : Table Ronde
echo "🏰 Etape 3: Ouverture de la Table Ronde..."
sleep 5
open Vision360_Mousquetaires.html

echo "------------------------------------------"
echo "✅ TOUT EST PRÊT, MON CAPITAINE !"
echo "------------------------------------------"
