# Facade

Oferecer uma interface única e simples para um conjunto de interfaces de um subsistema.

## Implementação

`CheckoutFacade` substitui três chamadas por uma. O ganho não é escrever menos linhas: é que a **sequência** e a **regra de ordem** passam a morar num lugar só — reservar antes de cobrar, nunca o contrário; não despachar o que não foi pago. Espalhado pelos chamadores, esse conhecimento é duplicado e alguma cópia acaba errando.

Repare no que a fachada **não** faz: não acrescenta comportamento próprio, e não impede quem precisa de usar os três serviços direto.

## Onde o Spring usa

- `JdbcTemplate` esconde `Connection`, `PreparedStatement`, `ResultSet` e o tratamento de `SQLException`.
- `RestClient` / `RestTemplate` sobre a pilha HTTP.
- `SpringApplication.run()` é uma fachada sobre toda a inicialização do container.

## Quando não usar — e o sintoma de que deu errado

A fachada vira **God Object** com facilidade, e o caminho é sempre o mesmo: cada requisito novo ganha mais um método nela, até virar a classe de mil linhas por onde tudo passa.

Dois sinais de alerta:

- A fachada começou a ter **lógica própria** em vez de só orquestrar. Isso é responsabilidade que pertencia a um dos subsistemas.
- Os subsistemas viraram `package-private` para forçar o uso dela. Aí ela deixou de ser conveniência e virou gargalo: todo requisito novo precisa ser costurado através dela.

Uma fachada saudável é fina, orquestra, e é opcional.

## Alternativa em Java moderno

Quando a sequência é uma só e sem ramificação, um método estático ou uma função em um serviço de aplicação faz o mesmo sem uma classe nova. A fachada se paga quando há várias sequências relacionadas sobre os mesmos subsistemas.
