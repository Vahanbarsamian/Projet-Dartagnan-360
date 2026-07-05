import os
from google import genai
from dotenv import load_dotenv

def list_models():
    load_dotenv()
    api_key = os.getenv("GEMINI_API_KEY")
    client = genai.Client(api_key=api_key)

    try:
        print("Liste des modèles disponibles :")
        # Utilisation de l'itérateur simple pour lister les modèles
        for model in client.models.list():
            print(f"- {model.name}")
    except Exception as e:
        print(f"Erreur lors de la récupération des modèles : {e}")

if __name__ == "__main__":
    list_models()
