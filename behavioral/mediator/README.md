# Mediator

Concentrar num objeto a comunicação entre um conjunto de componentes, que passam a não se referenciar diretamente.

## Implementação

`FormField` avisa o mediador do que aconteceu e **nunca** fala com outro campo. Essa restrição é o padrão.

Sem ela, cada campo precisa de referência a todo campo que ele afeta, e o cabeamento cresce com o **quadrado** do número de componentes: cinco campos que se afetam mutuamente são até vinte referências. Com mediador, são cinco — cada um para o mediador.

## O trade-off, que é explícito

Os componentes ficam simples e mutuamente ignorantes, e a complexidade que eles dividiam se **concentra** no mediador.

Isso é ganho enquanto as regras são poucas, e perda quando o mediador vira a classe que ninguém quer abrir. O padrão não elimina a complexidade — ele a move para um lugar onde é visível. Às vezes isso já é o suficiente; às vezes é só um God Object com nome melhor.

O sinal de que passou do ponto: o `switch` do mediador com mais de uma dúzia de casos, ou o mediador precisando saber detalhes internos dos componentes.

## Onde o Spring usa

- `ApplicationEventPublisher` é um mediador com acoplamento ainda menor: o publicador não conhece nem o mediador, só o evento. Ver [Observer](../observer).
- `DispatcherServlet` medeia entre `HandlerMapping`, `HandlerAdapter`, `ViewResolver` e `HandlerExceptionResolver` — nenhum deles se conhece.
- Em arquitetura, um *message broker* é mediador na escala de sistemas.

## Mediator x Observer

São primos e a diferença confunde:

- **Mediator** conhece os componentes e decide o que fazer. A lógica de coordenação está nele.
- **Observer** não decide nada: apenas entrega o evento a quem se inscreveu. A lógica está em cada ouvinte.

Mediador é centralizador por desenho; observer é distribuído por desenho.

## Quando não usar

Quando os componentes têm relação simples e estável. Dois objetos que se falam não precisam de intermediário.

Quando a coordenação é na verdade **notificação** — "isso aconteceu, quem se importa que reaja" —, Observer acopla menos.

## Alternativa em Java moderno

Para formulários e UI reativa, *data binding* declarativo resolve sem mediador escrito à mão. Em backend, eventos de domínio (Observer) costumam ser a escolha melhor, porque não exigem que um objeto central conheça todos os outros.
