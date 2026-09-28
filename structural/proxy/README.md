# Proxy

Fornecer um substituto para outro objeto, controlando o acesso a ele.

## Implementação

Dois sabores, ambos implementando a mesma interface do objeto real:

- `CachingReportProxy` — evita a chamada cara. Três chamadas lógicas, uma real.
- `AccessControlledReportProxy` — decide antes de chamar; a chamada recusada nunca chega ao delegado.

O cliente não consegue distinguir o proxy do objeto real. Essa é a exigência: um proxy que muda a interface é um Adapter, e um que muda a resposta é um bug.

## A armadilha que faz `@Transactional` não fazer nada

O teste `selfInvocationShouldBypassTheProxyEntirely` demonstra o problema mais caro desta lista.

Um proxy **envolve** o objeto. Chamadas que vêm de fora passam por ele; uma chamada de um método do objeto para outro método do mesmo objeto vai direto para `this`, e o embrulho nunca participa.

Como `@Transactional`, `@Cacheable`, `@Async` e `@PreAuthorize` são implementados como proxies no Spring, anotar um método e chamá-lo a partir de um método vizinho da mesma classe **não produz transação, nem cache, nem erro**. A anotação é simplesmente ignorada, em silêncio.

A saída é separar em outro bean — é por isso que os projetos deste portfólio têm classes como `ReconciliationChunkProcessor` e `OrderWriter` isoladas: a fronteira transacional precisa cruzar um proxy.

## Onde o Spring usa

É o mecanismo central do framework:

- **AOP inteiro**: `@Transactional`, `@Cacheable`, `@Async`, `@Retryable`, segurança por método.
- Repositórios do Spring Data: a interface não tem implementação; o container gera um proxy.
- `@Lazy` cria um proxy que adia a construção do bean real.
- JDK dynamic proxies quando há interface, CGLIB quando não há — daí a exigência de método não-`final` e classe não-`final`.

## Proxy x Decorator

Mesma forma, intenção diferente: **decorator acrescenta comportamento**, **proxy controla acesso**. Na prática a fronteira é borrada — um proxy de cache também acrescenta comportamento — e discutir a classificação raramente vale o tempo.

## Quando não usar

Quando não há o que controlar. Um proxy que só repassa é indireção pura, e ainda atrapalha a stack trace.

Quando o custo real está na chamada de rede e o que se quer é resiliência, um cliente com retry e circuit breaker resolve melhor que um proxy caseiro.

## Alternativa em Java moderno

Para uma interface de um método, uma lambda que envolve outra faz o trabalho. Para cache, `@Cacheable` em vez de escrever o proxy — desde que a chamada venha de fora do bean.
