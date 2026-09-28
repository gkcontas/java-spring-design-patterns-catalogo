# Prototype

Criar objetos novos copiando um exemplar existente, em vez de construí-los do zero.

## Implementação

`DocumentTemplate` é um modelo copiado por documento. O padrão se paga quando construir o original é caro — ler de disco, consultar banco, parsear um layout — e as cópias diferem pouco. Copiar um objeto em memória é barato; refazer aquele setup não é.

## A armadilha, que é o conteúdo do padrão

Todo o risco está em cópia rasa contra cópia profunda, e o teste demonstra o problema em vez de descrevê-lo:

```java
copy.sections().add("edited in the copy");
assertThat(original.sections()).containsExactly("intro", "edited in the copy");
```

Editar a cópia mudou o original, porque os dois apontam para a **mesma** lista. Esse é exatamente o comportamento de `Object.clone()`: ele copia cada campo, e campo que guarda referência é copiado como referência.

O defeito parece correto na leitura, passa em qualquer teste que só leia, e corrompe o original na primeira vez que alguém edita uma cópia em produção.

## Onde o Spring usa

O escopo `prototype` — `@Scope("prototype")` — entrega uma instância nova a cada injeção. Note que é o mesmo nome com significado diferente: o Spring **constrói** um bean novo, não clona um exemplar. A semelhança está no resultado (um objeto por uso), não no mecanismo.

## Quando não usar

Quando o objeto é imutável: não há o que copiar, basta compartilhar a referência. Records, `String`, `BigDecimal` — copiar é desperdício puro.

Quando construir é barato. `new Pedido()` não precisa de protótipo.

E **nunca** implemente `Cloneable`. A interface é um erro de design reconhecido: não declara método nenhum, `clone()` é protegido em `Object`, e o contrato é definido em prosa que ninguém segue. Um construtor de cópia ou um método de fábrica fazem o mesmo trabalho sem nada disso.

## Alternativa em Java moderno

- Construtor de cópia: `new DocumentTemplate(outro)`.
- Para records, `with`-ers manuais ou o construtor canônico com os campos alterados.
- Para estruturas realmente grandes, uma biblioteca de coleções persistentes resolve melhor que copiar.
