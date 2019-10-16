public class Main {
  public static void main(String[] args) {
    try{
      Project p = new Project(args[0]);
    }
    catch(ArrayIndexOutOfBoundsException e){
      System.out.println("WRONG FORMAT OF FILE");
    }
  }
}
