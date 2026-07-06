#!/bin/bash
# LANCEUR DARTAGNAN 360 POUR macOS - V4.2
echo "⚔️ INITIALISATION DE L'ACADÉMIE DARTAGNAN V4.2 🔱"
echo "------------------------------------------"

# Création des dossiers sur le Bureau
DESKTOP_DIR="$HOME/Desktop"
if [ -d "$HOME/OneDrive/Bureau" ]; then DESKTOP_DIR="$HOME/OneDrive/Bureau"; fi

mkdir -p "$DESKTOP_DIR/Dartagnan_Rendus"
mkdir -p "$DESKTOP_DIR/Dartagnan_Archives"
echo "[+] Dossiers de travail vérifiés sur le Bureau."

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
sleep 8
open Vision360_Mousquetaires.html

echo "------------------------------------------"
echo "✅ ARSENAL OPÉRATIONNEL !"
echo "------------------------------------------"
