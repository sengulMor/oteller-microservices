package com.oteller.reservationservice.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HotelKnowledgeIngestionService {

    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    public HotelKnowledgeIngestionService(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are an expert AI for hotel management and guest feedback analysis. 
                        Your task is to analyze hotel reviews, determine the sentiment (e.g. POSITIVE, NEGATIVE, NEUTRAL), 
                        extract a concise summary, and list key points.
                        """)
                .build();
    }

    public void ingestSampleHotelDocs() {
        List<Document> docs = List.of(
                new Document(
                        "Wellness & Spa: Unser Spa-Bereich ist täglich von 07:00 bis 22:00 Uhr geöffnet. Bademäntel und Saunatücher liegen auf dem Zimmer bereit.",
                        Map.of("category", "wellness", "source", "hotel-guide")
                ),
                new Document(
                        "Parken & Anreise: Direkt am Hotel gibt es eine Tiefgarage für 18 Euro pro Nacht. E-Ladestationen sind vorhanden.",
                        Map.of("category", "parking", "source", "hotel-guide")
                ),
                new Document(
                        "Stornierung & Haustiere: Kostenlose Stornierung ist bis 24 Stunden vor Anreise möglich. Hunde sind auf Anfrage erlaubt (15 Euro pro Nacht), im Frühstücksraum aber nicht gestattet.",
                        Map.of("category", "policies", "source", "hotel-guide")
                )
        );

        TextReader jobListReader = new TextReader(new ClassPathResource("product-data.txt"));
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().withChunkSize(5).withMaxNumChunks(1000).withMinChunkSizeChars(100)
                .withMinChunkLengthToEmbed(100).withKeepSeparator(true).build();
        List<Document> documents = tokenTextSplitter.split(jobListReader.get());
        vectorStore.add(documents);


        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> chunks = splitter.apply(docs);
        vectorStore.add(chunks);


    }

}
