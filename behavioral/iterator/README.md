# Iterator

Acessar os elementos de uma coleção em sequência sem expor a representação interna.

## Por que este exemplo não é uma lista

**Java deu este padrão de graça em 1998.** Qualquer classe que implemente `Iterable` ganha o for-each, e ninguém escreve `Iterator` à mão para percorrer uma `List`. Escrever o exemplo clássico aqui seria ensinar a reimplementar `ArrayList`.

O que o JDK **não** deu de graça é travessia **preguiçosa** sobre algo que é buscado conforme se anda. `PagedIterable` percorre uma fonte paginada — uma API remota, um cursor de banco — e cada página só é pedida quando a anterior acaba.

O segundo teste é o que justifica o padrão: parar depois de dois itens significa que as outras duas páginas **nunca foram buscadas**. Sobre uma fonte remota, é a diferença entre uma chamada e três.

## Onde o Spring usa

- `Streamable` e o retorno `Stream<T>` dos repositórios Spring Data, que percorrem um cursor aberto em vez de materializar tudo.
- `ResultSetExtractor`/`RowCallbackHandler` no `JdbcTemplate`, para varrer um `ResultSet` grande sem carregá-lo na memória.
- O `Slice` do Spring Data, que é a peça de baixo de uma iteração paginada — e é exatamente o assunto do [projeto de paginação](../../../java-spring-pagination-catalogo-filmes) deste portfólio.

## Quando não usar

Quando os dados já estão em memória numa coleção. `list.forEach(...)` ou um `Stream` resolvem, e escrever um iterador é reinventar o que já existe.

Duas armadilhas do padrão quando escrito à mão:

- **`hasNext()` com efeito colateral.** Aqui ele busca página, o que é necessário para a preguiça funcionar — mas significa que chamar `hasNext()` duas vezes precisa ser seguro. Um iterador que avança dentro do `hasNext()` está quebrado.
- **Modificação concorrente.** Os iteradores do JDK falham rápido com `ConcurrentModificationException`; um iterador caseiro sobre estrutura mutável costuma falhar devagar e errado.

## Alternativa em Java moderno

`Stream` cobre quase tudo, e é preguiçoso por construção. Para uma fonte paginada:

```java
Stream.iterate(0, page -> page + 1)
      .map(source::fetch)
      .takeWhile(page -> !page.items().isEmpty())
      .flatMap(page -> page.items().stream());
```

`Spliterators.spliteratorUnknownSize(iterator, ...)` converte um iterador existente em `Stream` quando a travessia já está escrita.
