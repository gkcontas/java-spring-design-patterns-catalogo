# Template Method

Definir o esqueleto de um algoritmo, deixando que subclasses redefinam passos sem mudar a estrutura.

## Implementação

`FileImporter.importFrom` fixa a sequência — ler, pular, validar, transformar, guardar — e deixa os passos abertos.

O método template é **`final`**, e isso não é detalhe: um template que a subclasse pode sobrescrever não é template. A garantia sendo vendida é que a sequência acontece naquela ordem toda vez, e um override a remove.

## Passo abstrato x hook

Distinção que quase todo texto omite:

- **Passo abstrato** (`validate`, `transform`, `store`): a subclasse *precisa* fornecer. O compilador cobra.
- **Hook** (`shouldSkip`): tem implementação padrão razoável; a subclasse sobrescreve *se quiser*.

O teste mostra a armadilha do hook: `CsvCustomerImporter` chama `super.shouldSkip(line)` antes de acrescentar sua própria regra. Um hook que esquece o `super` faz o comportamento herdado desaparecer em silêncio — o problema da classe base frágil em uma linha.

## Onde o Spring usa

É o padrão mais visível do framework, e está no nome das classes:

- **`JdbcTemplate`** — abre conexão, prepara statement, executa, mapeia, fecha, traduz exceção. Você fornece o SQL e o `RowMapper`.
- `RestTemplate`, `TransactionTemplate`, `RedisTemplate`, `KafkaTemplate`, `JmsTemplate`.
- `AbstractApplicationContext.refresh()` é um template method com mais de dez passos, vários sendo hooks.

Vale notar que a família `*Template` do Spring usa **callback** em vez de herança: você passa um `RowMapper` ou um `TransactionCallback` em vez de estender a classe. Mesmo padrão, composição no lugar de herança — e é a forma que envelheceu melhor.

## Quando não usar

Quando há uma subclasse só. O template é uma promessa de variação que não se cumpriu.

Quando os passos não são independentes. Se `transform` precisa saber o que `validate` decidiu, a decomposição está errada, e o resultado é subclasse mexendo em estado protegido — o caminho mais curto para a classe base frágil.

E o custo estrutural: herança é acoplamento forte e Java só tem herança simples. Gastar a única superclasse num template method é uma decisão que limita tudo que vier depois.

## Alternativa em Java moderno

Composição com interfaces funcionais, que é o caminho da própria família `*Template`:

```java
ImportReport importFrom(List<String> lines,
                        Predicate<String> skip,
                        Function<String, String> validate,
                        UnaryOperator<String> transform,
                        Consumer<String> store) { ... }
```

Sem herança, sem superclasse gasta, e os passos podem ser combinados livremente. A versão com herança ainda ganha quando os passos compartilham estado protegido ou quando são muitos — cinco parâmetros funcionais numa assinatura já são ilegíveis.
