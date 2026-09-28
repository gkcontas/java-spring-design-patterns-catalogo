# Observer

Notificar automaticamente um conjunto de interessados quando o estado de um objeto muda.

## Implementação

Este é o único padrão do catálogo implementado com o framework em vez de à mão, porque a versão manual — `List<Listener>`, `addListener`, `removeListener`, `notifyAll` — ensina menos do que ver o que o Spring já entrega.

A medida do padrão está em `OrderService`: ele **não nomeia nenhum ouvinte**. Sem a indireção, o método chamaria o mailer, o estoque e a analytics na mão, dependeria dos três, e mudaria toda vez que um quarto aparecesse. Aqui depende só do tipo do evento.

## Onde o Spring usa

- `ApplicationEventPublisher` e `@EventListener` — este exemplo.
- `@TransactionalEventListener(phase = AFTER_COMMIT)` resolve o problema mais comum de eventos de domínio: reagir só depois que a transação **de fato** commitou, e não quando o publisher foi chamado.
- Todo o ciclo de vida do container (`ContextRefreshedEvent`, `ApplicationReadyEvent`) é observer — os projetos de scheduler deste portfólio usam exatamente isso.

## Duas armadilhas que pegam todo mundo

**O padrão é síncrono.** `publishEvent` chama os ouvintes na thread do chamador, em sequência. Um ouvinte lento atrasa quem publicou; um ouvinte que lança exceção **propaga de volta** e derruba a operação original. Quase nunca é o desejado para efeitos colaterais — daí `@Async`, que muda isso de propósito.

**Vazamento de memória por ouvinte não removido.** Na versão manual, um observer registrado e nunca removido mantém o objeto vivo para sempre. É o motivo de `WeakReference` aparecer em implementações de UI. Com o Spring, o container cuida do ciclo de vida e o problema some — mais um argumento para não escrever o padrão à mão.

## Observer x Mediator

- **Observer** não decide nada: entrega o evento a quem se inscreveu, e a lógica fica em cada ouvinte. Distribuído por desenho.
- **Mediator** conhece os componentes e decide o que fazer. Centralizado por desenho.

## Quando não usar

Quando o resultado importa. Observer é *fire-and-forget*: quem publica não sabe quem ouviu nem o que aconteceu. Se a resposta é necessária, isso é uma chamada, não um evento.

Quando a ordem entre ouvintes importa. `@Order` existe, mas depender dela é sinal de que aquilo era um fluxo, não uma notificação.

Quando há um ouvinte só, para sempre. Aí o evento é indireção que só dificulta seguir o código.

## Alternativa em Java moderno

Para dentro de um processo, `Flow.Publisher` (JDK 9) ou Reactor dão contrapressão, que eventos do Spring não têm. Entre processos, mensageria — os projetos de [Kafka](../../../java-spring-kafka-dlq-reprocessamento) e RabbitMQ deste portfólio são Observer na escala de sistemas, com durabilidade e entrega garantida no lugar de uma lista em memória.
