# State

Permitir que um objeto altere seu comportamento quando seu estado interno muda, como se tivesse trocado de classe.

## Implementação

Cada estado é um objeto que sabe quais transições permite. `Order` — o contexto — delega tudo e **não tem um único `if` ou `switch`**.

O detalhe que faz a implementação valer: os métodos `default` da interface recusam a transição. Cada estado sobrescreve só o que permite, e **transição proibida é recusada por omissão**. Ninguém precisa lembrar de escrever o caso de erro; `SHIPPED` é terminal simplesmente por não sobrescrever nada.

Isso substitui um `switch (status)` repetido dentro de `pay()`, `ship()` e `cancel()`, cada um com sua lista de casos. Adicionar um status significa encontrar todos eles — e o que for esquecido falha em produção, não na compilação.

## Onde o Spring usa

- Spring Statemachine é o padrão como projeto inteiro.
- O ciclo de vida de bean (`Lifecycle`, `SmartLifecycle`) e o de transação (`TransactionStatus`) são máquinas de estado.
- Em Spring Batch, o status de um `StepExecution` governa o que pode acontecer em seguida.

## State x Strategy

Mesma estrutura — contexto delegando a um objeto substituível — e intenção oposta:

- **Strategy**: quem escolhe é o cliente, de fora, e as estratégias não se conhecem.
- **State**: quem escolhe é o estado atual, de dentro, e os estados conhecem seus sucessores.

Se o objeto decide sozinho para onde vai, é State.

## A alternativa em Java 21, que muitas vezes vence

Com `sealed` e pattern matching, a máquina de estados cabe num switch exaustivo, e o compilador garante a cobertura:

```java
sealed interface OrderStatus permits New, Paid, Shipped, Cancelled {}

OrderStatus pay(OrderStatus current) {
    return switch (current) {
        case New ignored -> new Paid();
        case Paid p -> throw new IllegalStateException("already paid");
        case Shipped s -> throw new IllegalStateException("already shipped");
        case Cancelled c -> throw new IllegalStateException("cancelled");
    };
}
```

O `switch` sem `default` sobre hierarquia selada **quebra a compilação** quando um estado novo é adicionado — o que resolve exatamente o problema que o padrão clássico resolvia com polimorfismo, e concentra a tabela de transições num lugar legível.

Qual escolher: a versão com objetos ganha quando cada estado tem **comportamento** próprio além de decidir o próximo (campos, invariantes, efeitos). A versão selada ganha quando a lógica é só a tabela de transições — que é o caso mais comum.

## Quando não usar

Quando há dois estados e uma transição. Um `boolean` resolve, e uma hierarquia de estados é cerimônia.

Quando os estados não mudam comportamento, só rotulam: aí é um `enum`, não o padrão.
