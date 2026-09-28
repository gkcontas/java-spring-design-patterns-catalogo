# Bridge

Separar uma abstração da sua implementação, para que as duas variem independentemente.

## Implementação

`Notification` é o **quê** (pedido enviado, senha redefinida). `MessageChannel` é o **como** (e-mail, SMS, push). A ponte é o campo `channel` dentro da abstração.

Sem ela, cada combinação vira uma classe: `OrderShippedEmail`, `OrderShippedSms`, `PasswordResetEmail`, `PasswordResetSms`. Cinco tipos de mensagem × quatro canais = vinte classes, e um canal novo adiciona mais cinco. A ponte troca essa multiplicação por uma soma.

## A diferença para Adapter

É a confusão mais comum entre os estruturais, e a distinção é de **intenção**, não de forma — os dois são um objeto segurando outro.

- **Adapter** é aplicado depois: existe uma interface incompatível e você precisa fazê-la caber. É corretivo.
- **Bridge** é decidido antes: você prevê que duas dimensões vão variar e separa as duas desde o início. É preventivo.

Um jeito rápido de decidir: se você pode mudar os dois lados, é Bridge; se um dos lados é dado e imutável, é Adapter.

## Onde o Spring usa

- A hierarquia de logging: a API que você chama é a abstração, o binding (Logback, Log4j2) é a implementação, e SLF4J é a ponte.
- `JdbcTemplate` sobre `DataSource`: a abstração de operações não muda quando o driver muda.
- `CacheManager` sobre os provedores concretos (Caffeine, Redis, Hazelcast).

## Quando não usar

Quando só uma das dimensões varia de verdade. Bridge com uma implementação só é indireção pura — e é o erro de aplicá-lo "por precaução", antes de existir a segunda dimensão.

Duas dimensões previstas mas que nunca se concretizam custam mais que a refatoração que teria sido feita quando a segunda apareceu.

## Alternativa em Java moderno

Quando o lado da implementação tem um método só, ele é uma interface funcional e a ponte fica quase invisível — veja o canal `push` criado como lambda no teste. Em Spring, injetar a implementação por construtor é a mesma ideia com o container montando a ponte.
