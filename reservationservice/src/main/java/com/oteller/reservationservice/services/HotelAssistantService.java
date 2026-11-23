package com.oteller.reservationservice.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelAssistantService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public HotelAssistantService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        Du bist ein hilfreicher Assistent für unser Hotel.
                        Beantworte Fragen ausschließlich auf Basis des bereitgestellten Kontexts aus unseren Hotel-Dokumenten.
                        Wenn die Antwort nicht im Kontext steht, sage ehrlich, dass du dazu keine Informationen hast, und erfinde nichts.
                        Antworte freundlich, präzise und auf Deutsch.
                        """)
                .build();
    }

    public String askHotelQuestion(String question) {
        TextReader jobListReader = new TextReader(new ClassPathResource("job_listings.txt"));
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().withChunkSize(5).withMaxNumChunks(1000).withMinChunkSizeChars(100)
                .withMinChunkLengthToEmbed(100).withKeepSeparator(true).build();
        List<Document> documents = tokenTextSplitter.split(jobListReader.get());
        vectorStore.add(documents);


        // 1. Passende Chunks aus pgvector holen
        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(1)
                        .build()
        );

        // 2. Kontext aus den Treffern zusammensetzen
        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));


        // 3. Frage + Kontext an den ChatClient senden
        return chatClient.prompt()
                .user(u -> u.text("""
                                Kontext aus den Hotel-Dokumenten:
                                {context}
                                
                                Frage des Gastes:
                                {question}
                                """)
                        .param("context", context)
                        .param("question", question))
                .call()
                .content();
    }
}
