import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Task {
  private int id, time, staff;
  private String name;
  private int earliestStart = 999;
  private int latestStart = 999;
  private ArrayList<Task> predecessor;
  private ArrayList<Integer> kanter_i;
  private int cntPredecessors;

  public Task(int id, String name, int time, int staff, String s){
    this.id = id;
    this.name = name;
    this.time = time;
    this.staff = staff;
    int a = byggArrayListe(s);
    if(a==0){
      return;
    }
    predecessor = new ArrayList<Task>();
  }

  private int byggArrayListe(String s){
    this.kanter_i = new ArrayList<Integer>();
    String[] words = s.split("\\s+");
    ArrayList<String> ord = new ArrayList<String>();
    for(int i=0; i<words.length; i++){
      if(!words[i].equals("0")){
        ord.add(words[i]);
      }
    }
    ord.removeAll(Arrays.asList("", null));
    for(int i=0; i<ord.size(); i++){
      try{
        this.kanter_i.add(Integer.parseInt(ord.get(i)));
      }
      catch(NumberFormatException e){
        System.out.println("Please give Integer value. ");
        return 0;
      }
    }
    return 1;
  }

  public void addOutEdge(Task t){
    try{
      predecessor.add(t);
      cntPredecessors++;
    }
    catch(NullPointerException n){
      System.out.println("predecessor ArrayList does not exists.");
    }
  }

  public ArrayList<Integer> get_kanter(){
    return this.kanter_i;
  }

  public ArrayList<Task> get_predecessor(){
    return this.predecessor;
  }

  public int task_id(){
    return this.id;
  }

  public int time(){
    return this.time;
  }

  public int staff(){
    return this.staff;
  }

  public String name(){
    return this.name;
  }

  public int antall_forgjengere(){
    return this.cntPredecessors;
  }

  public int earliestStart(){
    return this.earliestStart;
  }

  public void set_earliestStart(int e){
    this.earliestStart = e;
  }

  public int earliestFinish(){
    return this.earliestStart() + this.time();
  }

  public int latestStart(){
    return this.latestStart;
  }

  public void set_latestStart(int l){
    this.latestStart = l;
  }

  public int latestFinish(){
    return this.latestStart() + this.time();
  }

  public int slack(){
    return this.latestStart() - this.earliestStart();
  }

  public void skrivInfo(){
    System.out.printf("Identity number: %d\nName: %s\nTime: %d\nManpower: %d\nEarliest starting time: %d\nSlack %d\n",
    this.task_id(), this.name(), this.time(), this.staff(), this.earliestStart(), this.slack());
  }
}
