# Command

Encapsular uma requisição como objeto, permitindo enfileirar, registrar e desfazer operações.

## Implementação

`Command` transforma uma operação em objeto. Essa **reificação** é a ideia inteira, e tudo que o padrão serve decorre dela: um objeto pode ir para uma fila, ser logado, ser repetido, atravessar a rede e — como aqui — saber se reverter. Uma chamada de método não faz nada disso.

`CommandHistory` é o invoker: roda comandos e guarda o histórico que torna o undo possível. O log de auditoria sai de graça.

## O detalhe do undo

`undo()` precisa do estado que o `execute()` destruiu. Por isso um comando quase sempre tem que **capturar** esse estado na construção, não recalculá-lo depois. `AppendTextCommand` guarda o texto que acrescentou; para uma operação que sobrescreve, seria preciso guardar o valor anterior.

Quando o estado a guardar é grande, Command se combina com **Memento**: o comando guarda um memento em vez do estado cru.

## Onde o Spring usa

- `TaskExecutor` recebe `Runnable` — que é `Command` sem o undo.
- `TransactionCallback` e `JdbcTemplate` com `StatementCallback`: a operação vira objeto para que o template controle quando executá-la.
- Em arquitetura, é a base de **CQRS**: o lado de escrita é literalmente uma hierarquia de comandos.
- Mensagens em fila são comandos serializados — os projetos de Kafka e RabbitMQ deste portfólio são isso.

## Quando não usar

Quando não há nada a fazer com a operação além de executá-la agora. Um comando que é criado, executado e descartado imediatamente é uma chamada de método com três classes de cerimônia em volta.

O undo é mais difícil do que parece: operações com efeito externo (e-mail enviado, cobrança feita) **não têm inverso**. O honesto nesses casos é declarar que não há undo — e não escrever um `undo()` que finge desfazer.

## Alternativa em Java moderno

`Runnable`, `Supplier<T>` e `Consumer<T>` cobrem o caso sem undo:

```java
history.run(() -> document.append("Hello"));
```

Quando há undo, um `record UndoableCommand(Runnable execute, Runnable undo, String description)` dá o mesmo resultado com muito menos código que uma hierarquia de classes.
