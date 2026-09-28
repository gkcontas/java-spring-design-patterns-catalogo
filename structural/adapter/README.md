# Adapter

Converter a interface de uma classe na interface que o cliente espera.

## Implementação

`LegacyTaxEngine` é a biblioteca de terceiros como ela chegou: centavos como `long`, estado como código numérico, alíquota em pontos-base. Não dá para mudar. `LegacyTaxEngineAdapter` implementa `TaxCalculator`, que é a interface que **esta** aplicação quer.

O valor está em uma frase: o adapter é a **única** classe que sabe que o motor conta em centavos. Sem ele, esse vocabulário se espalha por todo chamador, e trocar o motor depois significa mexer em todos em vez de em um.

## Onde o Spring usa

- `HandlerAdapter` — adapta diferentes tipos de handler (`@Controller`, `HttpRequestHandler`, `Servlet`) à interface única que o `DispatcherServlet` chama.
- `MessageConverter` — adapta corpos HTTP a objetos Java.
- Os módulos `spring-boot-starter-*` são, em boa parte, adapters entre bibliotecas de terceiros e o modelo de configuração do Spring.

## Quando não usar

Quando você controla os dois lados. Se a interface "incompatível" é código seu, corrija-a em vez de embrulhar — um adapter aí é dívida disfarçada de padrão.

Quando é só renomear um método. `Adapter` que chama um método e devolve o resultado sem converter nada é indireção sem benefício.

## Camada anticorrupção

Adapter é a peça básica de uma anti-corruption layer no sentido de DDD: a fronteira onde o modelo de outro sistema é traduzido para o seu, para que o modelo dele não contamine o seu. Num projeto com várias integrações, esse costuma ser o uso mais valioso do padrão.

## Alternativa em Java moderno

Quando a interface alvo tem um único método, uma lambda já é o adapter:

```java
TaxCalculator calculator = (amount, state) -> converte(legacy.computeTaxInCents(...));
```

O teste acima faz exatamente isso para mostrar a substituição. Para interfaces com vários métodos, a classe continua sendo a forma legível.
