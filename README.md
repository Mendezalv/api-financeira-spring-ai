# API Inteligente de Orçamento com Spring AI

## O que o projeto faz
Esta é uma API RESTful de controle financeiro inteligente. Diferente de aplicações tradicionais onde o usuário preenche formulários, aqui o usuário envia um texto ou um arquivo de áudio relatando um gasto (ex: *"Gastei 150 reais no supermercado hoje"*). A aplicação utiliza IA generativa para transcrever o áudio, compreender a intenção, categorizar a despesa automaticamente e registrar no banco de dados. Em seguida, a IA responde com um feedback em formato de texto ou áudio.

## Como executar a aplicação

1. **Clone o repositório e acesse a pasta raiz.**
2. **Defina a variável de ambiente do OpenAI:**
   - No Linux/Mac: `export OPENAI_API_KEY="sua_chave_aqui"`
   - No Windows (PowerShell): `$env:OPENAI_API_KEY="sua_chave_aqui"`
3. **Execute usando o Maven Wrapper:**
   - Linux/Mac: `./mvnw spring-boot:run`
   - Windows: `.\mvnw.cmd spring-boot:run`
4. A API estará disponível em `http://localhost:8080`.
5. O console do banco H2 pode ser acessado em `http://localhost:8080/h2-console` (URL JDBC: `jdbc:h2:mem:budgetdb`, User: `sa`, Senha: `password`).

## Qual melhoria foi implementada
Foi implementado um **Sistema de Categorização Inteligente e Alerta de Gastos**. Usando a funcionalidade de *Tool Calling* do Spring AI, a Inteligência Artificial não apenas salva o dado, mas também invoca dinamicamente uma função Java interna (`checkBudget`) para consultar a soma dos gastos daquela categoria específica. 
O System Prompt foi configurado para agir como uma "assistente financeira rigorosa". Se o valor cadastrado ultrapassar o limite fictício pré-estabelecido (R$ 1000,00), a IA formula uma resposta alertando o usuário sobre o estouro do orçamento.

## Quais tecnologias foram usadas
* **Java 17**
* **Spring Boot 3.3.x**
* **Spring AI (1.0.0-M2)**
* **Spring Data JPA**
* **H2 Database** (Banco em memória)
* **Modelos da OpenAI:** `gpt-4o` (ou `gpt-3.5-turbo`) para Chat/Tool Calling, `whisper-1` para Transcrição, `tts-1` para Áudio.

## Como testar o fluxo principal

### Testando via Texto
```bash
curl -X POST http://localhost:8080/api/budget/text \
-H "Content-Type: application/json" \
-d '{"message": "Acabei de gastar 850 reais comprando passagens e uber."}'