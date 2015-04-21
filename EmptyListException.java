
public class EmptyListException extends RuntimeException {
   // --- Construtor ----------------------------------------------------------
   public EmptyListException() {
      this("List");
   }

   // --- Construtor ----------------------------------------------------------
   public EmptyListException( String name ) {
      super(name + " is empty");
   }
}