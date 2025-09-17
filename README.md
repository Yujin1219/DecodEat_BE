graph TD
    subgraph "End-User"
        Client[📱/💻 Client Application]
    end

    subgraph "AWS Cloud (ap-northeast-2)"
        ALB
        subgraph "Amazon EC2"
            SpringBoot
            FastAPI
        end
        subgraph "Data Persistence Layer"
            RDS
            S3
            VectorDB
        end
    end

    subgraph "External Services"
        Kakao[Kakao Authentication API]
        GoogleAI[Google Cloud AI Platform<br/>(Vision API / Gemini)]
    end

    %% Data Flows
    Client -- HTTPS Request --> ALB
    ALB -- HTTP --> SpringBoot

    SpringBoot -- "1. Kakao Redirect" --> Client
    Client -- "2. User Consent" --> Kakao
    Kakao -- "3. Auth Code" --> SpringBoot
    SpringBoot -- "4. Token Request" --> Kakao
    Kakao -- "5. Access Token" --> SpringBoot
    SpringBoot -- "6. User Info Request" --> Kakao
    Kakao -- "7. User Profile" --> SpringBoot
    SpringBoot -- "8. Issue JWT" --> Client

    Client -- "Image Upload (JWT)" --> SpringBoot
    SpringBoot -- "AWS SDK (Credentials)" --> S3
    SpringBoot -- "JDBC" --> RDS
    SpringBoot -- "HTTP POST (Image Location)" --> FastAPI
    FastAPI -- "AWS SDK" --> S3[Fetch Image]
    FastAPI -- "REST API Call" --> GoogleAI
    GoogleAI -- "Analysis Result (JSON)" --> FastAPI
    FastAPI -- "Generate Vector" --> FastAPI
    FastAPI -- "Analysis Result + Vector (JSON)" --> SpringBoot
    SpringBoot -- "Store Vector" --> VectorDB
    SpringBoot -- "JDBC" --> RDS[Update Metadata]
    SpringBoot -- "HTTP 200 OK" --> Client

    Client -- "Recommendation Request (Image ID, JWT)" --> SpringBoot
    SpringBoot -- "Query by ID" --> VectorDB
    SpringBoot -- "k-NN Search" --> VectorDB
    VectorDB -- "List of similar Image IDs" --> SpringBoot
    SpringBoot -- "JDBC (Batch Fetch)" --> RDS[Hydrate Metadata]
    SpringBoot -- "JSON Response" --> Client
