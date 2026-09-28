# Composite

Compor objetos em árvore e tratar objeto individual e composição de maneira uniforme.

## Implementação

`CatalogNode` é implementado por `Product` (folha) e `Category` (galho). As respostas do galho são as respostas dos filhos combinadas — e é isso o padrão: **a recursão mora na estrutura, não no chamador**.

O teste soma um catálogo com categoria aninhada em dois níveis sem um laço sequer no ponto de chamada.

## A versão Java 21

A interface é `sealed`. O catálogo clássico deixa a hierarquia aberta e paga com cadeias de `instanceof` que envelhecem mal: quando alguém adiciona um tipo de nó, cada cadeia precisa ser encontrada e atualizada, e as que forem esquecidas só falham em runtime.

Selada, o `switch` sobre os tipos é exaustivo e dispensa `default`:

```java
String describe = switch (node) {
    case Product product -> "leaf " + product.name();
    case Category category -> "branch with %d children".formatted(category.children().size());
};
```

Um terceiro tipo de nó quebra a compilação, que é onde se quer descobrir.

## Onde o Spring usa

- `CompositeCacheManager`, `CompositeHealthContributor`, `DelegatingFilterProxy` com cadeia — todos são "trate um e vários igual".
- A árvore de `BeanDefinition` com `parent`.
- Em Spring Security, `SecurityFilterChain` agrega filtros e é ela mesma um filtro.

## Quando não usar

Quando a estrutura é plana. Composite para uma lista é cerimônia.

Quando folha e galho **não** respondem às mesmas perguntas. Forçar isso produz o pior sintoma do padrão: métodos que a folha implementa lançando `UnsupportedOperationException` — `add()` e `remove()` na interface são o caso clássico. Se metade da interface não faz sentido para metade das implementações, a uniformidade é fingida.

## Cuidado com a profundidade

Recursão sobre árvore vinda de dado externo é risco de `StackOverflowError`, e uma árvore com ciclo trava para sempre. Estrutura de origem não confiável pede limite de profundidade ou travessia iterativa com pilha explícita.

## Alternativa em Java moderno

Para árvores pequenas e só de leitura, um `record` recursivo com `Stream` cobre quase tudo — que é literalmente esta implementação. O padrão clássico com classe abstrata e lista mutável só se paga quando a árvore é editada em runtime.
