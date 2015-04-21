/**
 * Classe Pilha, contém os métodos para a manipulação da pilha: push, pop, isEmtry e ultimaPosicao.
 */
public class Pilha {
   private List stackList;

   // --- Construtor ----------------------------------------------------------
   public Pilha() {
      stackList = new List( "stack" );
   }

   /** Adiciona uma elemento a pilha */
   public void push(Carta object) {
      stackList.insertAtFront(object);
   }

   /** Remove um elemento da pilha */
   public Carta pop() throws EmptyListException {
      return stackList.removeFromFront();
   }

   /** Checa se a pilha está vazia */
   public boolean isEmpty() {
      return stackList.isEmpty();
   }

   /** Retorna a última posição da pilha sem remove-la */
   public Carta ultimaPosicao() throws EmptyListException {
	   return stackList.lastPosition();
   }
}