# Flyweight

Compartilhar objetos para suportar grandes quantidades deles de forma eficiente.

## Implementação

`Currency` guarda estado **intrínseco** — código, símbolo, casas decimais — que é idêntico para toda quantia em BRL. `MonetaryAmount` guarda o estado **extrínseco**, o valor, que muda toda vez.

Essa separação é o padrão inteiro. Dez mil quantias compartilham um único objeto `Currency`, e o teste verifica identidade (`isSameAs`), não igualdade: a identidade é a economia.

Duas condições sem as quais não funciona:

- **A fábrica não é decoração.** Um construtor público deixaria o chamador criar as próprias instâncias e o compartilhamento simplesmente deixaria de acontecer.
- **O flyweight precisa ser imutável.** Um flyweight mutável é uma variável global entregue a todo mundo que pediu aquela moeda.

## Onde o JDK e o Spring usam

- **`Integer.valueOf()`** cacheia de -128 a 127. É por isso que `Integer.valueOf(127) == Integer.valueOf(127)` é `true` e `Integer.valueOf(128) == Integer.valueOf(128)` é `false` — a pegadinha de entrevista mais antiga do Java é flyweight vazando.
- `String` internada no pool de constantes.
- `Boolean.TRUE`/`FALSE`, `Character` até 127.
- No Spring, beans singleton são compartilhamento pela mesma razão, ainda que motivados por ciclo de vida e não por memória.

## Quando não usar

Quase sempre, hoje. O padrão nasceu quando memória era escassa; com objetos modernos e GC generacional, o ganho raramente justifica a indireção.

O caso em que ainda se paga é bem específico: **muitas** instâncias (centenas de milhares), com estado intrínseco **grande** em relação ao extrínseco. Fora disso, é otimização prematura — e medir antes é obrigatório, porque o cache também custa memória e uma lookup concorrente custa tempo.

Há ainda um risco real: um cache sem limite é vazamento de memória. `computeIfAbsent` sobre entrada não confiável cresce para sempre.

## Alternativa em Java moderno

- `record` imutável, simplesmente compartilhado por referência, sem cache nenhum.
- `enum`, quando o conjunto de valores é fechado — que é o caso de moeda na maioria dos sistemas, e seria a escolha melhor aqui se não fosse um exemplo do padrão.
- Value classes do Project Valhalla devem tornar boa parte do caso de uso obsoleto.
