import os
import speech_recognition as sr
from pydub import AudioSegment
from google.cloud import language_v1
def transcribe_audio(audio_path):
    """
    Transcribe audio file to text using speechrecognition.
    """
    # Convert audio to WAV if needed
    if not audio_path.endswith('.wav'):
        audio = AudioSegment.from_file(audio_path)
        audio_path_wav = audio_path.rsplit('.', 1)[0] + '.wav'
        audio.export(audio_path_wav, format='wav')
    else:
        audio_path_wav = audio_path

    # Initialize recognizer
    recognizer = sr.Recognizer()
    with sr.AudioFile(audio_path_wav) as source:
        audio_data = recognizer.record(source)
        try:
            text = recognizer.recognize_google(audio_data)
            return text
        except sr.UnknownValueError:
            return "Speech not recognized"
        except sr.RequestError as e:
            return f"Error: {e}"
        finally:
            if audio_path_wav != audio_path:
                os.remove(audio_path_wav)

def analyze_text(text):
    """
    Analyze text using Google Cloud NLP for sentiment and entities.
    """
    client = language_v1.LanguageServiceClient()
    document = language_v1.Document(content=text, type_=language_v1.Document.Type.PLAIN_TEXT)

    # Sentiment analysis
    sentiment = client.analyze_sentiment(request={'document': document}).document_sentiment
    # Entity analysis
    entities = client.analyze_entities(request={'document': document}).entities

    return {
        'sentiment_score': sentiment.score,
        'sentiment_magnitude': sentiment.magnitude,
        'entities': [(entity.name, entity.type_.name) for entity in entities]
    }

def process_audio_for_learning_path(audio_path):
    """
    Process audio to generate insights for learning paths.
    """
    text = transcribe_audio(audio_path)
    if "Speech not recognized" in text or "Error" in text:
        return {'error': text}

    analysis = analyze_text(text)
    suggestions = []
    if analysis['sentiment_score'] < 0:
        suggestions.append("Address negative sentiment with motivational content")
    for entity, entity_type in analysis['entities']:
        if entity_type == 'PERSON' or entity_type == 'TOPIC':
            suggestions.append(f"Explore {entity} in learning path")

    return {
        'transcription': text,
        'analysis': analysis,
        'suggestions': suggestions
    }

if __name__ == "__main__":
    # Example usage
    audio_file = "path/to/audio.mp3"
    result = process_audio_for_learning_path(audio_file)
    print(result)