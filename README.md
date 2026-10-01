# Catálogo de Padrões de Projeto

Os 22 padrões do GoF implementados em Java 21, cada um com código, teste e explicação, organizados por categoria em diretórios navegáveis.

Cada padrão responde a três perguntas além do "o que é": onde o próprio Spring usa aquele padrão internamente, em que situação ele deixou de ser necessário no Java moderno, e qual a consequência prática de adotá-lo — o que fica mais fácil e o que fica mais difícil.

## Como navegar

```
creational/     singleton  factory-method  abstract-factory  builder  prototype
structural/     adapter  bridge  composite  decorator  facade  flyweight  proxy
behavioral/     chain-of-responsibility  command  iterator  mediator  memento
                observer  state  strategy  template-method  visitor
```

Cada diretório de padrão tem o seu próprio `README.md`, uma pasta `main/` com a implementação e uma `test/` com o teste. A explicação fica ao lado do código, não em outro lugar do repositório.

## Tecnologias e bibliotecas

| | |
|---|---|
| Linguagem | Java 21 (records, sealed types, pattern matching) |
| Framework | Spring Boot 3.5 (apenas como contexto de referência; o catálogo é Java puro) |
| Build | Gradle Kotlin DSL (wrapper `gradlew`), com os 22 diretórios declarados como source roots |
| Testes | JUnit 5, AssertJ |

## Pré-requisitos

- JDK 21 ou superior

Não precisa de Docker, banco de dados nem acesso à rede.

## Como rodar os testes

```bash
./gradlew test
```

```bash
./gradlew test --tests '*StrategyTest'
```

71 testes. Cada um demonstra o benefício do padrão, não apenas que o código executa: o de Strategy acrescenta uma estratégia sem tocar na calculadora, o de Decorator mostra que a ordem dos decoradores muda o resultado, o de Proxy mostra a auto-invocação furando o proxy.

## Onde o Spring usa cada padrão

| Padrão | No Spring ou no JDK |
|---|---|
| Singleton | Escopo padrão de todo bean; `DefaultSingletonBeanRegistry` |
| Factory Method | `BeanFactory`, `FactoryBean<T>`, `ConnectionFactory` |
| Abstract Factory | Famílias de componentes por dialeto; os `spring-boot-starter-*` |
| Builder | `UriComponentsBuilder`, `MockMvcRequestBuilders`, `HttpSecurity` |
| Prototype | Escopo `prototype` |
| Adapter | `HandlerAdapter`, `MessageConverter` |
| Bridge | SLF4J sobre o binding; `JdbcTemplate` sobre `DataSource` |
| Composite | `CompositeCacheManager`, `SecurityFilterChain` |
| Decorator | `InputStream` do JDK; `HttpServletRequestWrapper` |
| Facade | `JdbcTemplate`, `RestClient`, `SpringApplication.run()` |
| Flyweight | Cache do `Integer.valueOf()`, pool de `String` |
| Proxy | Todo o AOP: `@Transactional`, `@Cacheable`, `@Async`, Spring Data |
| Chain of Responsibility | `FilterChain`, `HandlerInterceptor` |
| Command | `Runnable` no `TaskExecutor`; mensagens em fila |
| Iterator | `Streamable` do Spring Data; `ResultSetExtractor` |
| Mediator | `DispatcherServlet`; message broker |
| Memento | Savepoint de transação (`PROPAGATION_NESTED`) |
| Observer | `ApplicationEventPublisher`, `@TransactionalEventListener` |
| State | Spring Statemachine; `SmartLifecycle` |
| Strategy | `PlatformTransactionManager`, `PasswordEncoder`, `CacheManager` |
| Template Method | `JdbcTemplate`, `RestTemplate`, `TransactionTemplate` |
| Visitor | `BeanDefinitionVisitor`; `SimpleFileVisitor` |

## O que o Java moderno mudou

Vários padrões do GoF eram contornos para limitações de uma linguagem de 1994, e hoje a própria linguagem resolve:

| Padrão | Substituto natural |
|---|---|
| Strategy | Interface funcional e lambda |
| Command sem undo | `Runnable`, `Supplier`, `Consumer` |
| Iterator | `Iterable` e `Stream` |
| Visitor | `sealed` + `switch` com pattern matching |
| State | Hierarquia selada com `switch` exaustivo |
| Singleton | `enum`, ou um bean do container |
| Prototype e Memento | `record` imutável |
| Template Method | Callbacks funcionais no lugar de herança |

Continuam valendo praticamente intactos **Adapter**, **Facade**, **Proxy**, **Composite**, **Chain of Responsibility**, **Observer**, **Mediator** e **Builder** — justamente os que resolvem problemas de estrutura e acoplamento, não de expressividade da linguagem.

Cada README de padrão também aponta quando ele é desnecessário: Strategy com uma única estratégia, Factory para um único produto, ou Facade que acumulou lógica e virou um ponto central por onde todo requisito novo passa.
