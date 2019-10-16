import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

public class Project {
  private Task[] tasks;
  private int antall_tasks;

  public Project(String file_name){
    this.antall_tasks = read_file(file_name);
    if(this.antall_tasks == 0){
      return;
    }
    addEdges();
    if(har_sykler()){
      return;
    }
    topologicalSort();
    calulateStartingTimes();
    skrivInfo();
    printSolution();
  }

  private int read_file(String file_name){
    int antall = 0;
    try{
      File file = new File(file_name);
      Scanner scanner = new Scanner(file);
      try {
        antall = scanner.nextInt();
        this.tasks = new Task[antall];
      }
      catch(InputMismatchException e){
        System.out.println("Please give Integer Value. Terminating");
        return 0;
      }

      while(scanner.hasNextLine()){
        int id=0;
        try{
          id = scanner.nextInt();
        }
        catch(InputMismatchException e){
          System.out.println("Please give me Integer value");
          return 0;
        }

        String name = scanner.next();

        int time=0;
        try{
          time = scanner.nextInt();
        }
        catch(InputMismatchException e){
          System.out.println("Please give me Integer value");
          return 0;
        }

        int manpower=0;
        try{
          manpower = scanner.nextInt();
        }
        catch(InputMismatchException e){
          System.out.println("Please give me Integer value");
          return 0;
        }
        String rest = scanner.nextLine();
        this.tasks[id-1] = new Task(id,name,time,manpower,rest);
      }
    }
    catch(FileNotFoundException e){
      System.out.println("file not found");
    }
    return antall;
  }

  private void addEdges(){
    for(int i=0; i<this.antall_tasks; i++){
      try{
        ArrayList<Integer> kanter = this.tasks[i].get_kanter();
        for(int j=0; j<kanter.size(); j++){
          for(int k=0; k<this.antall_tasks; k++){
            if(this.tasks[k].task_id() == kanter.get(j)){
              this.tasks[i].addOutEdge(this.tasks[k]);
            }
          }
        }
      }
      catch(NullPointerException n){
        System.out.println("NullPointer. Wrong format");
        return;
      }
    }
  }

  private boolean har_sykler(){
    ArrayList<Task> visited = new ArrayList<Task>();
    for(int i=0; i<this.antall_tasks; i++){
      if(isCyclic(this.tasks[i],visited)){
        return true;
      }
    }
    return false;
  }

  private boolean isCyclic(Task task, ArrayList<Task> visited){
    visited.add(task);

    if(task.antall_forgjengere() == 0){
      visited.remove(task);
    }
    else {
      ArrayList<Task> kanter = task.get_predecessor();
      for(int i=0; i<kanter.size(); i++){
        if(visited.contains(kanter.get(i))){
          String sykel = "CYCLE: ";
          for(int j=visited.indexOf(kanter.get(i)); i<visited.size(); i++){
            sykel += visited.get(j).task_id() + " ";
          }
          sykel += " : " + kanter.get(i).task_id();
          System.out.println(sykel);
          return true;
        }
        isCyclic(kanter.get(i),visited);
      }
      visited.remove(task);
    }
    return false;
  }

private void topologicalSort(){
	Task[] t_list = new Task[this.antall_tasks];
	ArrayList<Task> stack = new ArrayList<Task>();
	int i;
	int[] innGrad = new int[this.antall_tasks];
	for(i=0; i<this.antall_tasks; i++){
		innGrad[i] = this.tasks[i].antall_forgjengere();
		if(innGrad[i] == 0){
			stack.add(this.tasks[i]);
		}
	}
	i=0;
	while(!stack.isEmpty()){
		t_list[i] = stack.remove(stack.size()-1);
		i++;
		ArrayList<Task> venter = avhengigAv(t_list[i-1]);
		for(int j=0; j<venter.size(); j++){
			innGrad[venter.get(j).task_id()-1]--;
			if(innGrad[venter.get(j).task_id()-1] == 0){
				stack.add(venter.get(j));
			}
		}
	}
	for(i=0; i<this.antall_tasks; i++){
		this.tasks[i] = t_list[i];
	}
}

  private ArrayList<Task> avhengigAv(Task t){
    ArrayList<Task> ret = new ArrayList<Task>();
    for(int i=0; i<this.antall_tasks; i++){
      ArrayList<Task> kanter = this.tasks[i].get_predecessor();
      for(int j=0; j<kanter.size(); j++){
        if(kanter.get(j).task_id() == t.task_id()){
          ret.add(this.tasks[i]);
        }
      }
    }
    return ret;
  }

  private void calulateStartingTimes(){
    for(int i=0; i<this.antall_tasks; i++){
    	if(this.tasks[i].antall_forgjengere() == 0){
          this.tasks[i].set_earliestStart(0);
          this.tasks[i].set_latestStart(0);
        }
        else {
          calculateEarliestStart(this.tasks[i]);
        }
      }
      for(int i=0; i<this.antall_tasks; i++){
        calculateLatestStart(this.tasks[i]);
      }
  }

  private void calculateEarliestStart(Task t){
    ArrayList<Task> task = t.get_predecessor();
    for(int i=0; i<task.size(); i++){
      t.set_earliestStart(task.get(i).earliestFinish());
    }
  }

  private void calculateLatestStart(Task t){
    ArrayList<Task> v = avhengigAv(t);
    ArrayList<Task> kanter = t.get_predecessor();
    if(t == this.tasks[this.antall_tasks-1]){
      t.set_latestStart(t.earliestStart());
    }
    else if(v.isEmpty()){
      t.set_latestStart(earliestFinishTimeProject()-t.time());
    }
    else if(t.antall_forgjengere() == 1){
      t.set_latestStart(kanter.get(0).latestFinish()+t.time());
    }
    else {
      t.set_latestStart(finnSeneste(kanter));
    }
  }

  private int finnSeneste(ArrayList<Task> kanter){
    int seneste = 0;
    for(int i=0; i<kanter.size(); i++){
      if(kanter.get(i).latestFinish() > seneste){
        seneste = kanter.get(i).latestFinish();
      }
    }
    return seneste;
  }

  public int earliestFinishTimeProject(){
    int storst = 0;
    for(int i=0; i<this.antall_tasks; i++){
      if(this.tasks[i].earliestFinish() > storst){
        storst = this.tasks[i].earliestFinish();
      }
    }
    return storst;
  }

  public void printSolution(){
    int staff = 0;
    for(int i=0; i<=earliestFinishTimeProject(); i++){
      for(int j=0; j<this.antall_tasks; j++){
        if(this.tasks[j].earliestStart() == i){
          staff+=this.tasks[j].staff();
          System.out.printf("TIME: %d Starting: %d\n", i, this.tasks[j].task_id());
          System.out.println("Current staff: " + staff);
          System.out.println();
        }
        if(this.tasks[j].earliestFinish() == i){
          staff-=this.tasks[j].staff();
          System.out.printf("TIME %d Finished: %d\n", i, this.tasks[j].task_id());
          System.out.println("Current staff: " + staff);
          System.out.println();
        }
      }
    }
  }

  public void skrivInfo(){
    for(int i=0; i<this.antall_tasks; i++){
      this.tasks[i].skrivInfo();
      System.out.println("Tasks which depend on this task:");
      ArrayList<Task> v = avhengigAv(this.tasks[i]);
      for(int j=0; j<v.size(); j++){
        System.out.println("Task: " + v.get(j).task_id());
      }
      System.out.println();
    }
  }

  public void criticalTasks(){
    for(int i=0; i<this.antall_tasks; i++){
      if(this.tasks[i].slack() == 0){
        System.out.printf("Task %d is critical", this.tasks[i].task_id());
      }
      else {
        System.out.printf("Task %d has %d slack", this.tasks[i].task_id(), this.tasks[i].slack());
      }
    }
  }

}
