# Singleton

Garantir uma única instância de uma classe e um ponto global de acesso a ela.

## Implementação

`ConfigurationRegistry` é um `enum` de um elemento só. A versão de livro — construtor privado, campo estático e verificação de nulo — está sutilmente errada de três formas ao mesmo tempo: precisa de sincronização explícita para ser segura no primeiro acesso concorrente, pode ser derrotada por reflection chamando o construtor privado, e desserializar uma instância produz uma segunda.

O enum ganha as três garantias da linguagem: a JVM garante uma instância por class loader, reflection se recusa a instanciar enum, e a serialização é definida para devolver a constante existente.

## Onde o Spring usa

O escopo padrão de todo bean é singleton — uma instância por container, gerenciada pelo `DefaultSingletonBeanRegistry`. **É por isso que escrever um singleton à mão dentro de uma aplicação Spring quase sempre é erro**: o container já faz isso, e melhor, com ciclo de vida, injeção e possibilidade de substituir por um mock no teste.

## Quando não usar

Quase sempre, em código de aplicação. Singleton é estado global com outro nome, e traz os problemas do estado global: acoplamento invisível (quem depende dele não declara isso em lugar nenhum), teste difícil (repare que o teste aqui precisa de um `clear()` no `@BeforeEach`), e ordem de inicialização que só aparece em produção.

Se a resposta para "por que singleton?" é "porque só precisa de um", isso é um argumento para **criar um só**, não para impedir que existam dois.

## Alternativa em Java moderno

- Um bean do container, com escopo singleton — a opção correta em 99% dos casos numa aplicação Spring.
- `enum` de um elemento, quando é código sem framework.
- Uma classe com métodos `static`, quando não há estado nenhum — mais honesto que fingir que há um objeto.
