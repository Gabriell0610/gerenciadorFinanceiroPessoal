# Financeiro Conversacional

Projeto pessoal criado com dois objetivos principais:

Servir como um laboratório de Engenharia de Software, permitindo estudar e aplicar conceitos importantes de desenvolvimento backend, arquitetura e infraestrutura.
Ser utilizado por mim e pela minha namorada para gerenciar os gastos do dia a dia de maneira simples.

A ideia surgiu de uma pergunta simples:

Por que pagar por um aplicativo financeiro se posso construir uma solução mais simples, adaptada às minhas necessidades e, ao mesmo tempo, utilizá-la como ambiente de aprendizado?

Por esse motivo, algumas decisões arquiteturais deste projeto são propositalmente mais complexas do que o necessário para o volume atual da aplicação.

O objetivo não é criar a arquitetura mais simples possível, mas utilizar um projeto real como ambiente para estudar problemas e soluções que aparecem em sistemas maiores.

## Tópicos de estudo que foram e vão ser implementados 
- Arquitetura Limpa
- SOLID
- Banco de dados (N+1, JPA/Hibernate, Indices, Lock pessimista e otimista)
- Java (conceitos básicos, threads, concorrência, paralelismo)
- Spring Boot (Container do spring, anotações, 
- FeingClient
- Clean Code
- Docker
- RabbitMq
- Opentelemtry
- Grafana
- Idempotencia
- Fila, Retry/Backof, DLQ
- Observabilidade

#### Escolhas e Tradeoffs
**Motivo de escolher comunicação assíncrona com fila entre o notification-api e o finance-pai**: Essa escolha surgiu por dois motivos principais, o primeiro é o desacoplamento que a fila traz de um serviço para o outro, fazendo a mensagem ser processada de forma assíncrona além de que todo o processo com o **rest** poderia ser demorado para entregar o status necessário para fechar a comunicação do webhook do telegram com o notification-api. O notification ele recebe a mensagem do telegram pelo webhook e validar/processa a mensagem e publica na fila. O fim desse processo retorna o 200 Ok para o telegram. 


# Arquitetura
O sistema é divido em três serviços com responsabilidades diferentes
Telegram -> notification-api -> Message Broker -> finance-api -> PostgreSQL -> APIs externas

O notification-api é responsável pela comunicação com o Telegram -> validar a mensagem e publicar na fila de gasto ou de relatório.

O finance-api concentra as regras de negócio relacionadas ao gerenciamento financeiro, como:

cadastro de despesas;
processamento das mensagens;
categorização;
geração de relatórios;
persistência dos dados;
integração com serviços externos.
E também na criação de usuários pelo chat_id vindo do telegram

## Evolução da arquitetura

A arquitetura do projeto está sendo modificada conforme novos conceitos são estudados e novas necessidades aparecem

### V1
<img width="1354" height="476" alt="image" src="https://github.com/user-attachments/assets/be82ae0e-7495-4317-8288-3be2c8ba76b3" />

### V2
<img width="1910" height="704" alt="image" src="https://github.com/user-attachments/assets/e1fc7566-3339-46c5-a5a4-2c813da36536" />
