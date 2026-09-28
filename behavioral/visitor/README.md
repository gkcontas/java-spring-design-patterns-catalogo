# Visitor

Representar uma operação a ser executada sobre os elementos de uma estrutura de objetos, permitindo adicionar operações novas sem alterar as classes dos elementos.

## Implementação, em duas versões

Este é o padrão em que o Java 21 mais muda as coisas, então o diretório traz as duas formas lado a lado sobre a mesma árvore de expressões:

- `ExpressionEvaluator` — a forma clássica, com `accept`/`visit` e duplo despacho.
- `ExpressionPrinter` — o mesmo trabalho com `switch` sobre hierarquia selada.

```java
return switch (expression) {
    case Literal(double value) -> String.valueOf(value);
    case Addition(Expression left, Expression right) -> "(%s + %s)".formatted(print(left), print(right));
    case Multiplication(Expression left, Expression right) -> "(%s * %s)".formatted(print(left), print(right));
    case Negation(Expression operand) -> "-%s".formatted(print(operand));
};
```

Sem `accept`, sem métodos `visit`, sem duplo despacho. E porque a hierarquia é `sealed`, o `switch` dispensa `default` e **o compilador se recusa a construir** se um tipo de nó for adicionado e não tratado aqui.

Essa garantia de exaustividade é exatamente o que o Visitor clássico comprava obrigando cada nó a implementar `accept`. Padrões de desconstrução de record vão um passo além: os filhos são ligados na própria cláusula, sem chamadas de acessor.

## O trade-off que define o padrão

Visitor troca uma facilidade por uma dificuldade, e saber qual lado dói é a pergunta de entrevista:

- **Adicionar uma operação é fácil**: uma implementação nova do visitor, e nenhuma classe de nó é tocada — demonstrado no teste que conta nós.
- **Adicionar um tipo de nó é caro**: quebra **toda** implementação do visitor.

Por isso ele cabe em hierarquias **fechadas** — AST, documentos, tipos de arquivo — e machuca em hierarquias que crescem.

## Onde o Spring usa

- `BeanDefinitionVisitor`, que percorre definições de bean resolvendo placeholders.
- `PropertyPlaceholderConfigurer` visitando a árvore de metadados de configuração.
- Fora do Spring, o `TreeVisitor` da API de compilação do Java e o `SimpleFileVisitor` de `java.nio.file.Files.walkFileTree` são o mesmo padrão.

## Quando não usar

Quando a hierarquia ainda cresce. Cada tipo novo é uma mudança em todo visitor existente.

Quando há uma operação só. Um método na própria classe resolve, e o duplo despacho é cerimônia.

Quando a operação precisa do estado privado dos nós. Visitor só alcança o que é público, e abrir o estado para acomodá-lo destrói o encapsulamento que motivava tudo.

## Alternativa em Java moderno

`sealed` + `switch` com pattern matching, como no `ExpressionPrinter`. Em Java 21 essa é a escolha padrão: mesma exaustividade verificada pelo compilador, sem `accept` em cada nó e sem uma interface de visitor para manter.

A forma clássica ainda ganha em dois casos: quando a hierarquia **não** pode ser selada (classes em módulos diferentes fora do seu controle), e quando o visitor carrega estado acumulado ao longo da travessia — o que o `switch` também resolve, mas com menos elegância.
