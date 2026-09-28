# Catálogo de padrões de projeto

Os 22 padrões do GoF implementados em Java 21, cada um com teste e explicação, organizados por categoria em diretórios navegáveis.

O diferencial em relação aos incontáveis repositórios de "design patterns em Java" está no que cada README responde além do "o que é":

- **onde o próprio Spring usa aquele padrão internamente** — `JdbcTemplate` é Template Method, `@Transactional` é Proxy, `BeanFactory` é Factory Method;
- **quando o padrão é desnecessário** em Java moderno, porque a linguagem absorveu vários deles;
- **a consequência real** de aplicá-lo: o que fica mais fácil e o que fica mais difícil.

Saber recitar os 23 padrões é júnior. Saber que Strategy com uma implementação é complexidade desnecessária, e que Singleton escrito à mão em aplicação Spring quase sempre é erro, é sênior.

## Status

✅ 22 padrões implementados, 71 testes passando.

## Como navegar

```
creational/     singleton  factory-method  abstract-factory  builder  prototype
structural/     adapter  bridge  composite  decorator  facade  flyweight  proxy
behavioral/     chain-of-responsibility  command  iterator  mediator  memento
                observer  state  strategy  template-method  visitor
```

Cada diretório de padrão contém `README.md`, `main/` com a implementação e `test/` com o teste.

## Layout do build

Um build só, 22 diretórios navegáveis. Vinte e dois projetos Gradle, cada um com wrapper e arquivo de build, seriam cerimônia pura — o valor aqui é o código dos padrões, não repetir configuração. Mas uma árvore `src/main/java` convencional enterraria cada padrão quatro níveis abaixo e colocaria o README em outro lugar, e poder abrir `behavioral/strategy/` e ver a explicação ao lado da implementação é boa parte do que torna um catálogo útil.

Então cada diretório de padrão é um *source root*, com `main/` e `test/` e a árvore de pacotes usual dentro. A IDE entende, o javac recebe diretórios que casam com os pacotes, e o repositório continua navegável.

## Onde o Spring usa cada padrão

| Padrão | No Spring / JDK |
|---|---|
| Singleton | escopo padrão de todo bean; `DefaultSingletonBeanRegistry` |
| Factory Method | `BeanFactory`, `FactoryBean<T>`, `ConnectionFactory` |
| Abstract Factory | famílias de componentes por dialeto; os `spring-boot-starter-*` |
| Builder | `UriComponentsBuilder`, `MockMvcRequestBuilders`, `HttpSecurity` |
| Prototype | escopo `prototype` (constrói, não clona) |
| Adapter | `HandlerAdapter`, `MessageConverter` |
| Bridge | SLF4J sobre o binding; `JdbcTemplate` sobre `DataSource` |
| Composite | `CompositeCacheManager`, `SecurityFilterChain` |
| Decorator | `InputStream` do JDK; `HttpServletRequestWrapper` |
| Facade | `JdbcTemplate`, `RestClient`, `SpringApplication.run()` |
| Flyweight | `Integer.valueOf()` cache, pool de `String` |
| Proxy | **todo o AOP**: `@Transactional`, `@Cacheable`, `@Async`, Spring Data |
| Chain of Responsibility | `FilterChain`, `HandlerInterceptor` |
| Command | `Runnable` no `TaskExecutor`; CQRS; mensagens em fila |
| Iterator | `Streamable` do Spring Data; `ResultSetExtractor` |
| Mediator | `DispatcherServlet`; message broker |
| Memento | savepoint de transação (`PROPAGATION_NESTED`) |
| Observer | `ApplicationEventPublisher`, `@TransactionalEventListener` |
| State | Spring Statemachine; `SmartLifecycle` |
| Strategy | `PlatformTransactionManager`, `PasswordEncoder`, `CacheManager` |
| Template Method | **`JdbcTemplate`**, `RestTemplate`, `TransactionTemplate` |
| Visitor | `BeanDefinitionVisitor`; `SimpleFileVisitor` do `Files.walkFileTree` |

## O que Java moderno tornou obsoleto ou trivial

Vários padrões do GoF eram contornos para limitações de uma linguagem de 1994. Fingir que continuam iguais é o erro mais comum desse tipo de catálogo.

| Padrão | O que mudou |
|---|---|
| **Strategy** | Interface funcional. `(total, kg) -> ...` já é uma estratégia; a hierarquia sumiu |
| **Command** (sem undo) | `Runnable`, `Supplier`, `Consumer` |
| **Iterator** | `Iterable` desde 1998, `Stream` desde 2014. Escrever à mão só para travessia preguiçosa de fonte remota |
| **Visitor** | `sealed` + `switch` com pattern matching dá a mesma exaustividade verificada pelo compilador, sem `accept` em cada nó |
| **State** | Hierarquia selada com `switch` exaustivo, quando a lógica é só a tabela de transições |
| **Singleton** | `enum`, ou simplesmente um bean do container |
| **Prototype** | `record` imutável: compartilhar a referência é a cópia |
| **Memento** | `record` imutável **é** o memento; guardar a referência antiga basta |
| **Decorator** | Composição de funções, quando a interface tem um método |
| **Flyweight** | Raramente se paga; `enum` cobre o caso de conjunto fechado |
| **Template Method** | Callbacks funcionais em vez de herança — o caminho que a própria família `*Template` do Spring tomou |

Os que continuam valendo praticamente intactos: **Adapter**, **Facade**, **Proxy**, **Composite**, **Chain of Responsibility**, **Observer**, **Mediator** e **Builder**. Não por acaso, são os que resolvem problemas de **estrutura e acoplamento**, não de expressividade da linguagem.

## Quando não usar nenhum deles

A pergunta que falta na maioria dos catálogos.

Padrão é resposta a uma pressão concreta — algo que já mudou mais de uma vez, ou que você tem evidência de que vai mudar. Aplicado antes dessa pressão existir, ele só acrescenta indireção: mais classes para ler, mais saltos para depurar, e a flexibilidade prevista quase nunca é a que o requisito real pede.

Três sinais recorrentes de padrão aplicado cedo demais, todos apontados no README do respectivo diretório:

- **Strategy com uma estratégia.** Extrair a interface quando a segunda aparecer custa cinco minutos.
- **Factory para um produto.** Uma hierarquia de criadores com um filho só.
- **Facade que virou God Object.** Começou orquestrando, ganhou lógica própria, e agora todo requisito novo é costurado através dela.

## Como rodar os testes

```bash
./gradlew test
```

71 testes, sem Docker e sem rede. Cada teste **demonstra o benefício** do padrão em vez de só verificar que o código roda: o de Strategy acrescenta uma estratégia sem tocar na calculadora, o de Decorator prova que a ordem muda o resultado, o de Proxy mostra a auto-invocação furando o proxy, e o de Prototype demonstra a cópia rasa corrompendo o original.
