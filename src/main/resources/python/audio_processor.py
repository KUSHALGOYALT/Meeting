
import speech_recognition as sr
import sys
import os
import subprocess
import json

def transcribe_audio(audio_path, output_text_path):
    """
    Transcribe audio file to text using Google Speech Recognition.
    Args:
        audio_path (str): Path to the input WAV audio file.
        output_text_path (str): Path to save the transcribed text.
    Returns:
        dict: Transcription and status.
    """
    recognizer = sr.Recognizer()
    try:
        with sr.AudioFile(audio_path) as source:
            audio = recognizer.record(source)
            text = recognizer.recognize_google(audio)

        # Save transcription to output file
        with open(output_text_path, 'w') as f:
            f.write(text)

        # Extract keywords by calling audio_keyword_extractor.py
        keyword_script = os.path.join(os.path.dirname(__file__), 'audio_keyword_extractor.py')
        result = subprocess.run(
            ['python', keyword_script, output_text_path],
            capture_output=True, text=True
        )
        if result.returncode != 0:
            raise Exception(f"Keyword extraction failed: {result.stderr}")

        keywords = json.loads(result.stdout)

        return {
            'status': 'success',
            'transcription': text,
            'keywords': keywords
        }
    except sr.UnknownValueError:
        return {'status': 'error', 'message': 'Could not understand audio'}
    except sr.RequestError as e:
        return {'status': 'error', 'message': f'Speech recognition error: {str(e)}'}
    except Exception as e:
        return {'status': 'error', 'message': str(e)}

def main():
    if len(sys.argv) != 3:
        print(json.dumps({'status': 'error', 'message': 'Usage: audio_processor.py <audio_path> <output_text_path>'}))
        sys.exit(1)

    audio_path = sys.argv[1]
    output_text_path = sys.argv[2]

    result = transcribe_audio(audio_path, output_text_path)
    print(json.dumps(result))

if __name__ == '__main__':
    main()
