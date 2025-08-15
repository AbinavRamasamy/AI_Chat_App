# AI Chat App
A simple Android chat application that lets you interact with an AI assistant. This app supports persistent chat history across app launches, so you never lose your conversation unless you delete it.

# Features
- **AI Chat**: Send messages to an AI and receive intelligent responses (From Gemini)
- **Persistent Chat History**: Chats are saved locally and remembered across app launches
- **User-Friendly Interface**: Simple design for easy interaction
- **Clear Chat Options**: Easily clear your chat history whenever you want.
- **Automatic Scrolling**: The chat automatically scrolls to the latest message.


# Instructions
Make sure you open the backend/.env file and add your Gemini API Key

Ensure you run the Flask Backend by opening the backend folder in terminal, then run: 
- flask run --host=0.0.0.0 --port=8000

Verify the host and port addresses match what is in backend/app.py file
