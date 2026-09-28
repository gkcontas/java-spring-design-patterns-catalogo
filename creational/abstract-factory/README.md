# Abstract Factory

Fornecer uma interface para criar **famílias** de objetos relacionados, sem nomear as classes concretas.

## Implementação

`ReportComponentFactory` produz um `ReportHeader` e um `ReportTable`. `HtmlReportFactory` e `PlainTextReportFactory` entregam as famílias completas.

## A diferença para Factory Method

É a única que importa, e é sempre a pergunta de entrevista: **Factory Method cria um produto; Abstract Factory garante que vários produtos combinam entre si.**

Repare que nada na API permite ao cliente montar um cabeçalho HTML com uma tabela em texto. Essa impossibilidade é o objetivo do padrão, não um efeito colateral. Se a consistência entre os produtos não importa, não há família, e o padrão está sendo usado onde caberia Factory Method.

## Onde o Spring usa

Menos onipresente que Factory Method, mas aparece onde há dialetos ou ambientes: as famílias de componentes de acesso a dados que variam por banco, e a forma como `spring-boot-starter-*` entrega conjuntos coerentes de auto-configurações — mesma ideia em outra escala.

## Quando não usar

Quando existe uma família só, ou quando os produtos não têm relação real entre si. O custo do padrão é alto: adicionar um **produto** novo (digamos, um rodapé) obriga a mudar a interface da fábrica e **todas** as implementações. Ele troca facilidade de adicionar famílias por dificuldade de adicionar produtos, e vale a pena apenas quando famílias entram com mais frequência que produtos.

## Alternativa em Java moderno

Um `record` agrupando os componentes já criados costuma bastar:

```java
record ReportComponents(ReportHeader header, ReportTable table) {}
```

Passa-se o registro pronto em vez de uma fábrica que sabe montá-lo — mais simples, e a garantia de coerência passa a ser de quem constrói o registro.
