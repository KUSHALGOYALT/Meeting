from transformers import pipeline
import sys
import json

def generate_learning_paths(weaknesses, opportunities):
    generator = pipeline("text-generation", model="distilgpt2")
    paths = []
    for weakness in weaknesses:
        prompt = f"Suggest a learning path to address weakness in {weakness}."
        response = generator(prompt, max_length=50, num_return_sequences=1)[0]["generated_text"]
        paths.append(response.strip())
    for opportunity in opportunities:
        prompt = f"Suggest a learning path to pursue opportunity in {opportunity}."
        response = generator(prompt, max_length=50, num_return_sequences=1)[0]["generated_text"]
        paths.append(response.strip())
    return paths

def answer_question(question, context, additional_data):
    generator = pipeline("text-generation", model="distilgpt2")
    additional = " ".join(additional_data) if additional_data else ""
    prompt = f"Question: {question}\nContext: {context}\nAdditional: {additional}\nAnswer:"
    response = generator(prompt, max_length=100, num_return_sequences=1)[0]["generated_text"]
    return response.strip()

def generate_adaptive_quiz(subject, weakness, session_id, student_id):
    generator = pipeline("text-generation", model="distilgpt2")
    prompt = f"Generate a 3-question quiz for student {student_id} in session {session_id} on {subject}, focusing on {weakness}. Format each question as a single line."
    response = generator(prompt, max_length=150, num_return_sequences=1)[0]["generated_text"]
    questions = [q.strip() for q in response.split("\n") if q.strip()][:3]
    return questions

if __name__ == "__main__":
    action = sys.argv[1]
    if action == "learning_paths":
        weaknesses = json.loads(sys.argv[2])
        opportunities = json.loads(sys.argv[3])
        result = generate_learning_paths(weaknesses, opportunities)
    elif action == "answer_question":
        question = sys.argv[2]
        context = sys.argv[3]
        additional_data = json.loads(sys.argv[4])
        result = answer_question(question, context, additional_data)
    elif action == "adaptive_quiz":
        subject = sys.argv[2]
        weakness = sys.argv[3]
        session_id = sys.argv[4]
        student_id = sys.argv[5]
        result = generate_adaptive_quiz(subject, weakness, session_id, student_id)
    print(json.dumps(result))