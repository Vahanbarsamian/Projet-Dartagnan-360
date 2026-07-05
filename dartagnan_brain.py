import os
import json
import requests
from google import genai
from dotenv import load_dotenv

class DartagnanBrain:
    def __init__(self):
        load_dotenv()
        self.api_key_gemini = os.getenv("GEMINI_API_KEY")
        self.client_gemini = genai.Client(api_key=self.api_key_gemini)

        # Configuration Ollama (Local)
        self.ollama_url = "http://localhost:11434/api/generate"
        self.local_model = "mistral"

        self.system_instruction = """
        Tu ES Dartagnan, moteur d'IA d'élite.
        MISSION : Analyse technique concise et souveraine.

        PHILOSOPHIE :
        1. CONCISION : Va à l'essentiel.
        2. VALIDATION : Confirme la faisabilité.
        3. ACCOMPAGNEMENT : Propose des options claires.

        FORMAT JSON STRICT :
        {
          "faisabilite": "OUI/NON + Bref pourquoi",
          "exploration_360": "Synthèse technique concise...",
          "options_evolution": ["Option 1", "Option 2"],
          "demande_ordre": "Phrase noble demandant validation."
        }
        """

    def interroger_local(self, sujet):
        """ Interroge le moteur local (Ollama/Mistral) """
        prompt = f"{self.system_instruction}\n\nMajesté travaille sur : {sujet}. Analyse la faisabilité."

        try:
            response = requests.post(
                self.ollama_url,
                json={
                    "model": self.local_model,
                    "prompt": prompt,
                    "stream": False,
                    "format": "json"
                },
                timeout=30
            )
            return response.json().get("response")
        except Exception as e:
            return f"Erreur locale : {str(e)}"

    def analyser_projet(self, sujet, mode="local"):
        """
        Mode 'cloud' (Gemini) ou 'local' (Ollama)
        """
        if mode == "local":
            print(f"--- [MODE LOCAL ARAMIS ACTIVÉ] ---")
            raw_res = self.interroger_local(sujet)
            try:
                return json.loads(raw_res)
            except:
                return {"exploration_360": raw_res, "statut": "brut"}
        else:
            # Code Gemini précédent...
            pass

if __name__ == "__main__":
    brain = DartagnanBrain()
    print("Dartagnan consulte la brigade locale...")
    # Test sur votre projet d'hydrogène
    rapport = brain.analyser_projet("Générateur d'hydrogène par cavitation via disques en contre-rotation.", mode="local")
    print(json.dumps(rapport, indent=2, ensure_ascii=False))
