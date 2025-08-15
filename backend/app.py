from flask import Flask, request, jsonify
import google.generativeai as genai
from google.generativeai.types import GenerationConfig
from flask_cors import CORS
from dotenv import load_dotenv
import os

# Load environment variables
load_dotenv()

api_key = os.getenv("GEMINI_API_KEY")
if not api_key:
    raise ValueError("Missing GEMINI_API_KEY in .env file")

# Configure Gemini
genai.configure(api_key=api_key)

app = Flask(__name__)
CORS(app)

system_message = (
    "Your communication style should be professional, friendly.\n"
    "Always be concise and to the point, unless directed by the user.\n"
    "If a user's question is ambiguous or incomplete, politely ask clarifying questions.\n"
    "Never reply with 'AI:'\n"
)

conversation_history = ""

@app.route("/", methods=["GET"])
def home():
    return "Server is running!"

@app.route("/gemini-chat", methods=["POST"])
def gemini_chat():
    global conversation_history
    data = request.get_json()
    user_input = data.get("text")

    if not user_input:
        return jsonify({"error": "No text provided"}), 400

    # Add user input to conversation history
    conversation_history += f"User: {user_input}\n"

    try:
        model = genai.GenerativeModel(
            model_name="gemini-1.5-flash",
            generation_config=GenerationConfig(
                temperature=0.7,
                top_p=1,
                top_k=1,
                max_output_tokens=500
            ),
            system_instruction=system_message
        )

        # Generate response
        response = model.generate_content(conversation_history)
        ai_reply = response.text.strip()

        # Update conversation history
        conversation_history += f"AI: {ai_reply}\n"

        return jsonify({"reply": ai_reply})

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
    