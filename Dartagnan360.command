#!/bin/bash
# LANCEUR DARTAGNAN 360 POUR macOS - V4.3
echo "⚔️ INITIALISATION DE L'ACADÉMIE DARTAGNAN V4.3 🔱"
echo "------------------------------------------"

# 1. NETTOYAGE ET AUTORISATION OLLAMA
echo "🧠 Préparation du cerveau (CORS Authorization)..."
# On tue l'instance existante pour forcer la relecture des variables
killall ollama 2>/dev/null
export OLLAMA_ORIGINS="*"
ollama serve &

# 2. DOSSIERS DE TRAVAIL
DESKTOP_DIR="$HOME/Desktop"
if [ -d "$HOME/OneDrive/Bureau" ]; then DESKTOP_DIR="$HOME/OneDrive/Bureau"; fi
mkdir -p "$DESKTOP_DIR/Dartagnan_Rendus"
mkdir -p "$DESKTOP_DIR/Dartagnan_Archives"
echo "[+] Dossiers de travail vérifiés."

# 3. PONT ROYAL
echo "🔱 Activation du Pont Royal..."
cd "$(dirname "$0")"
chmod +x gradlew
./gradlew :app:runBridge &

# 4. OUVERTURE INTERFACE
echo "🏰 Ouverture de la Table Ronde..."
sleep 10
open Vision360_Mousquetaires.html

echo "------------------------------------------"
echo "✅ ARSENAL OPÉRATIONNEL !"
echo "------------------------------------------"
