# Factory Method

Definir uma interface para criar um objeto, deixando a subclasse decidir qual classe instanciar.

## Implementação

`CheckoutProcess` fixa o algoritmo de checkout e declara `createPaymentMethod()` como abstrato. `CreditCardCheckout` e `PixCheckout` respondem por essa única decisão.

O que separa Factory Method de uma fábrica estática qualquer é justamente isso: a decisão é **adiada para a subclasse** por um método sobrescrevível, não tomada por um `switch` dentro de um helper. A classe base chama o método sem saber — e sem poder saber — o que vem de volta.

## Onde o Spring usa

- `BeanFactory` é a interface central do container, e o nome não é coincidência.
- `FactoryBean<T>` é o Factory Method exposto ao usuário: um bean cuja única função é produzir outro objeto, usado quando a construção é complexa demais para um construtor.
- `ConnectionFactory`, `EntityManagerFactory`, `SqlSessionFactory` — o mesmo padrão em cada integração.

## Quando não usar

Quando existe **uma** implementação. Uma hierarquia de criadores com um único filho é uma indireção que não paga o próprio custo.

Quando a escolha depende de um dado em runtime (o tipo de pagamento que veio no request), Factory Method não é a resposta — subclasse é decisão de compilação. Aí cabe um `Map<String, Supplier<PaymentMethod>>` ou, em Spring, injetar `List<PaymentMethod>` e selecionar pelo que cada um declara suportar.

## Alternativa em Java moderno

Um `Supplier<T>` como parâmetro resolve boa parte dos casos sem hierarquia nenhuma:

```java
String checkout(BigDecimal amount, Supplier<PaymentMethod> factory) { ... }
```

E em Spring, injetar `List<PaymentMethod>` e deixar o container montar a coleção é quase sempre mais direto que uma árvore de criadores.
