# Decorator

Acrescentar responsabilidades a um objeto dinamicamente, envolvendo-o.

## Implementação

`PriceDecorator` implementa `PriceQuote` **e** segura um. Esse par é o que permite empilhar decoradores a qualquer profundidade sem que nenhum deles saiba quão fundo está.

```java
new SalesTax(new ShippingFee(new PercentageDiscount(base, 10%), 50), 18%)
```

## A aresta mais afiada: a ordem importa

Desconto antes do imposto e imposto antes do desconto dão resultados diferentes. E **nada no sistema de tipos impede** a ordem errada — o compilador aceita qualquer empilhamento.

É o principal custo do padrão, e o motivo de decoradores com efeitos não comutativos precisarem de teste que fixe a ordem esperada. Herança teria o mesmo problema sem a flexibilidade.

## Onde o Spring e o JDK usam

- **`InputStream` do JDK** é o exemplo canônico: `new BufferedInputStream(new GZIPInputStream(new FileInputStream(f)))`. Empilhamento, na mesma forma.
- `HttpServletRequestWrapper` / `HttpServletResponseWrapper` — a base de boa parte dos filtros de servlet.
- `TransactionAwareCacheDecorator`, `DelegatingDataSource` e a família `*Wrapper` do Spring.
- Um `ClientHttpRequestInterceptor` em cadeia é decorator com outro nome.

## Decorator x Proxy

Os dois envolvem um objeto e implementam a mesma interface. A diferença é de intenção: **decorator acrescenta comportamento**, **proxy controla acesso** (lazy, cache, permissão, remoto). Na prática a fronteira é borrada — um proxy de cache também acrescenta comportamento — e discutir a classificação raramente vale o tempo.

## Quando não usar

Quando a composição é fixa e conhecida. Três decoradores que sempre aparecem juntos e na mesma ordem são um método.

Quando a pilha fica profunda: depurar sete camadas de embrulho é doloroso, e a stack trace fica ilegível. A partir de certo ponto, uma lista de transformações aplicadas em sequência é mais clara que o aninhamento.

## Alternativa em Java moderno

Com interface funcional, composição de funções faz o mesmo sem hierarquia:

```java
UnaryOperator<BigDecimal> pipeline = discount.andThen(shipping).andThen(tax);
```

A ordem fica explícita na leitura, da esquerda para a direita — o que resolve justamente o problema de ordem citado acima.
