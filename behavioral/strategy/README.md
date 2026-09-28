# Strategy

Definir uma família de algoritmos intercambiáveis, deixando o algoritmo variar independentemente de quem o usa.

## Implementação

`ShippingStrategy` tem um método e nenhum estado. `ShippingCalculator` segura uma estratégia e nunca pergunta qual é.

O terceiro teste mostra o benefício de forma direta: uma estratégia que **não existia** quando a calculadora foi escrita, acrescentada como lambda, sem uma linha alterada na calculadora. É Open/Closed demonstrado em vez de citado.

## O detalhe que quase sempre falta

O padrão existe para remover um `switch (tipoDeFrete)`. Trocar esse switch por uma hierarquia de estratégias e então escrever **outro switch para escolher a estratégia** não resolve nada — só move o problema.

A resposta é seleção por chave: um `Map<String, ShippingStrategy>`. Em Spring, injetar `Map<String, ShippingStrategy>` faz o container preencher o mapa com os nomes dos beans, e estratégia nova é um `@Component` novo — zero alteração em código existente.

## Onde o Spring usa

- `PlatformTransactionManager` — JPA, JDBC, JTA: mesma interface, algoritmos diferentes.
- `PasswordEncoder`, com `DelegatingPasswordEncoder` fazendo justamente a seleção por chave.
- `CacheManager`, `TaskExecutor`, `ViewResolver`, `MessageConverter` — a lista é longa, porque é o padrão mais usado do framework.

## State x Strategy

Mesma estrutura, intenção oposta: em **Strategy** quem escolhe é o cliente, de fora, e as estratégias não se conhecem; em **State** quem escolhe é o estado atual, de dentro. Ver [State](../state).

## Quando não usar

Quando existe **uma** implementação. Strategy com uma estratégia é indireção pura, e o "mas pode ser que apareça outra" raramente se concretiza — e quando se concretiza, extrair a interface naquele momento custa cinco minutos.

Quando os "algoritmos" diferem em uma constante. Isso é um parâmetro, não uma estratégia.

## Alternativa em Java moderno

Strategy é o padrão que a linguagem mais absorveu. Com interface funcional:

```java
BigDecimal quote(BigDecimal total, double kg, ShippingStrategy strategy) { ... }
quote(total, kg, (t, w) -> BigDecimal.valueOf(w * 5));
```

Não há hierarquia, não há classes de estratégia, e o `Function`/`BiFunction` do JDK substitui a interface quando ela não tem nome de domínio útil. A interface nomeada continua valendo quando o nome comunica algo — `ShippingStrategy` diz mais que `BiFunction<BigDecimal, Double, BigDecimal>`.
