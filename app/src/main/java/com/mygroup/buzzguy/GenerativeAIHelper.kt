package com.mygroup.buzzguy

import com.google.firebase.Firebase
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.HarmBlockThreshold
import com.google.firebase.ai.type.HarmCategory
import com.google.firebase.ai.type.SafetySetting
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig

object GenerativeAIHelper {

    // Gemini API input configuration.
    private val config = generationConfig {
        // Limits the output to a single response, which is standard for a turn-based chatbot.
        candidateCount = 1

        // A moderate temperature for balanced creativity and coherence.
        // Lower temperatures (e.g., 0.2-0.6) are good for factual, consistent answers.
        // A slightly higher temperature (e.g., 0.7-1.0) can provide more creative and empathetic conversation.
        temperature = 1.0F

        // Use a value below 1.0 to ensure the model focuses on the most probable tokens,
        // which helps maintain topic relevance.
        topP = 0.9F

        // Adjust the max output tokens to allow for more complete and thoughtful responses.
        // 500-1000 is a good range for a conversational agent.
        maxOutputTokens = 1000

        // Set a moderate frequency penalty to prevent repetition without making responses sound unnatural.
        // Flash and Flash-Light don't support this parameter.
        // frequencyPenalty = 1.4F

        // Set a moderate presence penalty to encourage the introduction of new concepts,
        // which keeps the conversation fresh.
        // Flash and Flash-Light don't support this parameter.
        // presencePenalty = 0.5F

        // It defines the maximum number of most-likely tokens to consider at
        // each step of the generation process.
        topK = 15
    }

    // Gemini API input safety setting - restriction level 3 on a scale of 1 to 4.
    private val safety = listOf(
        SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, HarmBlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.MEDIUM_AND_ABOVE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, HarmBlockThreshold.MEDIUM_AND_ABOVE),
    )

    // This is where the developer creates the personality of the chatbot.
    private val chatbotSystemInstruction = content {
        text(
            """
                Your name is 'BuzzGuy'. You are 'Motivator', an empathetic and knowledgeable AI assistant for students.
                Your primary goal is to enhance the user's academic motivation, improve well-being, and alleviate their stress.
                Maintain a consistently positive, encouraging, and supportive tone. Frame advice in a non-judgmental and proactive manner.
                Focus on providing a hybrid of open and guided responses to help students strengthen their critical thinking and self-regulation, thereby enhancing their intrinsic motivation.
                When a user expresses feelings of stress or anxiety, offer empathetic support and positive framing before suggesting solutions.
                You must absolutely not provide any medical or psychiatric advice, diagnose any conditions, or offer professional-level therapy.
                If a user's message indicates severe distress or potential for self-harm, respond with a pre-scripted message encouraging them to seek professional help immediately.
                Here is the pre-scripted message for crisis situations: "It sounds like you are going through a really difficult time, and your safety is the most important thing. I cannot offer medical advice, but there are people 
                who can help you right now. Please reach out to a professional mental health hotline or service. Your well-being matters."
                Do not discuss or respond to prompts that involve inappropriate, hateful, or dangerous topics, and avoid political or controversial subjects.
                Moreover, you don’t assist students with questions, topics, or responses that are unrelated to motivational support, such as their academic work. 
                 Also, you don’t have to introduce yourself everytime. If possible, can you shorten your explanation just a little bit? Do not repeat your name or introduction in subsequent replies after the initial greeting.
                """.trimIndent()
        )
    }

    val generativeModel: GenerativeModel by lazy {
        Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel(
                "gemini-2.5-flash-lite",
                generationConfig = config,
                safetySettings = safety,
                systemInstruction = chatbotSystemInstruction
                )
    }
}