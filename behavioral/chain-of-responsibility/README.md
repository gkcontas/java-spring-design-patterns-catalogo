# Chain of Responsibility

Passar uma requisição por uma cadeia de tratadores até que um deles a resolva.

## Implementação

Cada `ValidationHandler` responde só à sua pergunta e não sabe nada sobre os outros nem sobre a própria posição na cadeia. É isso que permite mudar a ordem, ou remover um elo, sem tocar em nenhum deles — demonstrado no último teste.

A cadeia **curto-circuita**: a primeira rejeição vence e o resto não roda. Isso importa quando uma verificação posterior é cara, ou quebraria com um dado que uma anterior já rejeitou.

## Uma escolha de implementação

Guardei a cadeia como uma `List` em vez de cada tratador apontar para o `next`. A forma encadeada clássica acopla cada elo ao seguinte e transforma reordenar numa recabeação de construtores; a lista deixa a ordem explícita no ponto de montagem, que é onde ela pertence.

## Onde o Spring usa

- `FilterChain` da API de Servlet — o exemplo canônico, e a base de todo o Spring Security.
- `HandlerInterceptor` em cadeia no Spring MVC.
- `ClientHttpRequestInterceptor` no `RestTemplate`/`RestClient`.
- A resolução de `HandlerMethodArgumentResolver`: cada um diz se sabe tratar o parâmetro; o primeiro que souber trata.

## Quando não usar

Quando você quer **todas** as falhas, não a primeira. Um formulário que reporta um erro por vez é péssima experiência — aí o que se quer é acumular os resultados, que é outro desenho com outro nome.

Quando a ordem é irrelevante e todos sempre rodam: isso é uma lista de predicados, não uma cadeia.

E o risco silencioso: uma requisição que chega ao fim sem ninguém tratar. Aqui isso significa "válida", o que é intencional e explícito; numa cadeia de despacho significa que a requisição sumiu. Vale sempre decidir o que acontece quando ninguém assume.

## Alternativa em Java moderno

Com interface funcional, a cadeia é uma lista de lambdas e um `stream`:

```java
handlers.stream().map(h -> h.validate(request)).flatMap(Optional::stream).findFirst();
```

`findFirst()` sobre stream preguiçoso mantém o curto-circuito.
