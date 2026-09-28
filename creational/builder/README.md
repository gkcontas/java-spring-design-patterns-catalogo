# Builder

Separar a construção de um objeto complexo da sua representação, permitindo montar passo a passo.

## Implementação

`PurchaseOrder` tem oito campos, vários opcionais e vários do mesmo tipo. É exatamente o caso que o padrão resolve: `new PurchaseOrder(a, b, null, null, c, true, false, d)` compila sem reclamar com dois argumentos trocados.

Duas decisões que separam um builder bom de um cerimonial:

- **Campos obrigatórios são parâmetros do construtor do builder**, não chamadas `withX` opcionais. Um builder que permite chegar ao `build()` sem eles desistiu da única garantia que deveria oferecer.
- **Regras entre campos ficam no `build()`**, onde o objeto está completo. Um setter não consegue verificar "express não pode ser embrulhado para presente": no momento em que um é setado, o outro pode ainda não ter sido.

## Onde o Spring usa

Em toda parte, e quase sempre bem: `UriComponentsBuilder`, `MockMvcRequestBuilders`, `BeanDefinitionBuilder`, `RestClient.builder()`, `SecurityFilterChain` via `HttpSecurity`. Sempre que há muitas opções e poucas obrigatórias, é builder.

## Quando não usar

Quando o objeto tem três ou quatro campos, todos obrigatórios. Aí o builder é cerimônia: mais código para escrever, mais código para ler, e nenhuma ambiguidade evitada.

O sinal de alerta é um builder com todos os campos obrigatórios — isso é um construtor com passos extras.

## Alternativa em Java moderno

Para objetos pequenos, um `record` com construtor compacto faz o trabalho e valida no lugar certo:

```java
record Money(BigDecimal amount, String currency) {
    Money {
        if (amount.signum() < 0) throw new IllegalArgumentException("negative");
    }
}
```

O `@Builder` do Lombok gera tudo isto — e é a escolha certa quando o builder é puramente mecânico. A versão escrita à mão ganha quando há validação entre campos, porque o `@Builder` gerado não tem onde colocá-la.

Neste portfólio a convenção é usar records para DTOs e Lombok nas entidades; este builder manual existe para mostrar o que o código gerado faz por baixo.
