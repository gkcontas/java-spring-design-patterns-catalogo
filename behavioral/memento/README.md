# Memento

Capturar o estado interno de um objeto para restaurá-lo depois, sem violar o encapsulamento.

## Implementação

`DraftArticle` é o originator e a única coisa que sabe ler o próprio estado de volta. `DraftHistory` é o caretaker: guarda snapshots e devolve, sem entender nenhum.

A parte que quase toda implementação erra está em "sem violar o encapsulamento". `Snapshot` é público para poder ser guardado, mas só é **consumido** por `restore()`. O caretaker pode segurar e devolver, e não pode inspecionar nem forjar. Expor getters no memento — o jeito mais comum de escrever isto — entrega justamente o encapsulamento que o padrão existe para proteger.

## Duas armadilhas, as duas cobertas por teste

**Compartilhar estado mutável com o snapshot.** Se o memento guardasse a mesma lista do objeto, ele mudaria junto com o objeto que deveria lembrar. `List.copyOf` nos dois sentidos resolve.

**Histórico sem limite.** Undo ilimitado mantém toda versão do objeto viva. Para qualquer coisa de tamanho relevante, isso é vazamento de memória com nome simpático. `DraftHistory` tem profundidade máxima.

## Onde o Spring usa

Raro no framework, porque undo raramente é requisito de backend. O parente mais próximo:

- O savepoint de transação é memento no nível do banco: `PROPAGATION_NESTED` marca um ponto de retorno.
- `RequestContextHolder` salva e restaura contexto ao cruzar fronteiras de thread.

## Memento x Command

Complementares, e combinam:

- **Command** guarda a **operação** e sabe se inverter.
- **Memento** guarda o **estado** e não sabe nada sobre operações.

Quando a operação é difícil de inverter, mas o estado é pequeno, o comando guarda um memento em vez de escrever um `undo()` próprio. É a combinação padrão em editores.

## Quando não usar

Quando o objeto é imutável: a "versão anterior" é o objeto anterior, basta guardar a referência. Com records, memento praticamente desaparece.

Quando o estado é grande. Snapshot completo a cada mudança não escala; aí a saída é event sourcing — guardar as mudanças, não os estados.

## Alternativa em Java moderno

Um `record` imutável **é** o memento: guardar a referência antiga é o snapshot inteiro.

```java
record Draft(String title, List<String> paragraphs) {}
Deque<Draft> history = new ArrayDeque<>();
```

Sem cópia defensiva, sem classe extra, sem risco de compartilhar estado mutável — os três problemas do padrão clássico somem porque o dado não muda.
