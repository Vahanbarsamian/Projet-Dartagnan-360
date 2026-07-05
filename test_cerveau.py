import os
from google import genai
from dotenv import load_dotenv

def test_cerveau():
    load_dotenv()
    api_key = os.getenv("GEMINI_API_KEY")
    client = genai.Client(api_key=api_key)

    # Utilisation du nom exact trouvé dans list_models.py
    model_name = "gemini-flash-latest"

    try:
        print(f"Test du cerveau en cours (Modèle: {model_name})...")
        response = client.models.generate_content(
            model=model_name,
            contents="Bonjour ! Es-tu prêt à m'aider ?"
        )

        print("\nRéponse du cerveau :")
        print(response.text)
        print("\nLe cerveau fonctionne correctement !")

    except Exception as e:
        print(f"Une erreur est survenue lors du test avec {model_name} : {e}")

if __name__ == "__main__":
    test_cerveau()
