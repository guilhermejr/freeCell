
class ListNode {
   Carta data;
   ListNode nextNode;

   // --- Construtor ----------------------------------------------------------
   ListNode(Carta object) {
      this( object, null );
   }

   // --- Construtor ----------------------------------------------------------
   ListNode(Carta object, ListNode node) {
      data = object;
      nextNode = node;
   }

   // --- Retorna refer�ncia aos dados no n� ----------------------------------
   Carta getObject() {
      return data;
   }

   // --- Retorna refer�ncia ao pr�ximo n� na lista ---------------------------
   ListNode getNext() {
      return nextNode;
   }
}

// --- Defini��o da classe List -----------------------------------------------
public class List
{
   private ListNode firstNode;
   private ListNode lastNode;
   private String name;

   // --- Construtor ----------------------------------------------------------
   public List() {
      this("list");
   }

   // --- construtor ----------------------------------------------------------
   public List(String listName) {
      name = listName;
      firstNode = lastNode = null;
   }

   // --- Insere Object na frente de List -------------------------------------
   public void insertAtFront(Carta insertItem) {
      if (isEmpty())
         firstNode = lastNode = new ListNode(insertItem);
      else
         firstNode = new ListNode(insertItem, firstNode);
   }

   // --- Insere Object no fim de List ----------------------------------------
   public void insertAtBack(Carta insertItem) {
      if (isEmpty())
         firstNode = lastNode = new ListNode(insertItem);
      else
         lastNode = lastNode.nextNode = new ListNode(insertItem);
   }

   // --- Retorna �ltima posi��o de List --------------------------------------
   public Carta lastPosition () throws EmptyListException {
	   if (isEmpty())
		   throw new EmptyListException(name);

	   Carta removedItem = firstNode.data;

	   return removedItem;
   }

   // --- Remove o primeiro n� de List ----------------------------------------
   public Carta removeFromFront() throws EmptyListException {
      if (isEmpty())
         throw new EmptyListException( name );

      Carta removedItem = firstNode.data;

      if (firstNode == lastNode)
         firstNode = lastNode = null;
      else
         firstNode = firstNode.nextNode;

      return removedItem;
   }

   // --- Remove o �ltimo n� de List ------------------------------------------
   public Carta removeFromBack() throws EmptyListException {
      if (isEmpty())
         throw new EmptyListException(name);

      Carta removedItem = lastNode.data;

      if (firstNode == lastNode)
         firstNode = lastNode = null;
      else {
         ListNode current = firstNode;

         while (current.nextNode != lastNode)
            current = current.nextNode;

         lastNode = current;
         current.nextNode = null;
      }

      return removedItem;
   }

   // --- Determina se a lista estiver vazia ----------------------------------
   public boolean isEmpty() {
      return firstNode == null;
   }

   // --- Gera sa�da do conte�do de List --------------------------------------
   public void print() {
      if (isEmpty()) {
         System.out.printf("Empty %s\n", name);
         return;
      }

      System.out.printf("The %s is: ", name);
      ListNode current = firstNode;

      while (current != null) {
         System.out.printf("%-5s %-10s\n", current.data.getValor(), current.data.getNaipe());
         current = current.nextNode;
      }

      System.out.println( "\n" );
   }
}