/* Marshall Tanis, Matt Watkins, Elijah Baldwin
 *
 * Deadwood
 *
 * Modified 2/19/2016
 */

import java.util.*;
import java.lang.*;

 
 public class deadwood{
  public static class Player{
    private Dice dice;
    private int dollars = 0;
    private int credits = 0;
    private int rehearseCount = 0;
    private int rank = 1;
    private int score = 0;
    private boolean role = false;
    private Scene scene;
    private Lead actRole;
    private Extra extraRole;
    private boolean actSuccessful;
    
    private String local = "Trailer";
    public Player(Dice pDice){
        this.dice = pDice;
    }
    public void joinScene(Scene scene){
        this.scene = scene;
    }
    public int getDollars(){
        int bucks = dollars;
        return bucks;
    }
    public int getCredits(){
        int creds = credits;
        return creds;
    }
    public int rank(){
        int pRank = rank;
        return pRank;
    }
    public int getScore(){
        int Score = score;
        return Score;
    }
    public String getLocal(){
        String location = local;
        return location;
    }
    public void setDollars(int amount){
        this.dollars += amount;
    }
    public void setCredits(int amount){
        this.credits += amount;
    }
    public String getLeadRole(){
        String name = this.actRole.getName();
        return name;
    }
    public String getExtraRole(){
        String name = this.extraRole.getName();
        return name;
    }

    public boolean act(){
      int getRoll = dice.roll();
      System.out.println(getRoll);
      int budget = scene.getBudget();   //Identify which scene based on the players position
      if(getRoll >= budget - rehearseCount){
        actSuccessful = true;
        return actSuccessful;
      }
      else{
        actSuccessful = false;
        return actSuccessful;
      }
    }
    public void takeLeadRole(Lead roll){
      if(role == false && rank >= roll.getRank()){           
        role = true;
        actRole = roll;
      }
      else{
        System.out.print("Invalid Action");
      }
    }
    public void takeExtraRole(Extra roll){
      if(role == false && rank >= roll.getRank()){           
        role = true;
        extraRole = roll;
      }
      else{
        System.out.print("Invalid Action");
      }
    }
   /* public void move(){
      /* If location is in adjacent set, move, else choose a different room */
    /*public void rehearse(){
      this.rehearse ++;
    }*/

    
    public void rehearse(){
      /*add 1 to rehearseCount */
      this.rehearseCount ++;
    }
}

   public static class Dice{
    private Dice(){
    }
    public static Dice getDice(){
        if(dice == null){
            dice = new Dice();
        }
        return dice;
    }
    public static Dice dice;
    
    public int roll(){
        Random rand = new Random();
        int random = rand.nextInt(6) + 1;
        return random;
    }
    }
//   }
// 
   public static abstract class Role{
    private int rankReq;

    //Abstract function with implementation provided by children:
    public abstract void reward(Player p);
  }

  public static class Lead extends Role{
    private String roleName;
    private int rankReq;
    public Lead(int val, String name) {
      rankReq = val;
      roleName = name;
    }
    public String getName(){
        String actName = roleName;
        return actName;
    }
    public int getRank(){
        int rank = rankReq;
        return rank;
    }
    //provide credits reward on success:
    //shotsLeft for ActingSet is also decremented.
    public void reward(Player p) {
      //reward on success only:
      if (p.actSuccessful == true)
        p.setCredits(p.getCredits() + 2);
      //ActingSet.setShotsLeft(ActingSet.getShotsLeft() - 1);
    }
  }
  public static class Extra extends Role{
    // provide money and credit reward on Success, money only on failure:
    //shotsLeft for ActingSet is also decremented.
    private String roleName;
    private int rankReq;
    public Extra(int val, String name) {
      rankReq = val;
      roleName = name;
    }
    public String getName(){
        String actname = roleName;
        return actname;
    }
    public int getRank(){
        int rank = rankReq;
        return rank;
    }
    public void reward(Player p) {
      //reward on success:
      if (p.actSuccessful == true) {
        p.setCredits(p.getCredits() + 1);
        p.setDollars(p.getDollars() + 1);
      //reward on failure:
      } else {
        p.setDollars(p.getDollars() + 1);
      }
      //ActingSet.setShotsLeft(ActingSet.getShotsLeft() - 1);

    }
  }
   public static class Scene{
    private int budget; 
    private int numRoles;
    public Scene(int budget,int numRoles){
        this.budget = budget;
        this.numRoles = numRoles;
    }
    public int getBudget(){
        int budg = budget;
        return budg;
    }
  }
//   public interface Day{
//     //private int dayNum;
//   }
// 
//     public void newDay () {
//       /* do some shit */
//     }
//   }
//   public class ActingSet extends Set implements Day{
//     private int shots;
//     private int shotsLeft;
//     private Scene scene;
//     private List<Scene> scenelist;
//     private List<Extra> extraList;
// 
//   }
//   public class CastingOffice extends Set{
//     public void upgrade_wDollars (int dollars){
// 
//     }
//     public void upgrade_wCreds (int credits) {
// 
//     }
//   }
//   public class Trailer extends Set{
// 
//   }
//   public abstract class Set{
//     private List<Set> adjRooms;
// 
//   }
   public static void main(String[]arg){
       Dice officialDice = Dice.getDice();
       Map<String,List<String>> adjacencyList = new HashMap<String,List<String>>();
       popAdjList(adjacencyList);
       Scene curr = new Scene(4,3);
       Player p1 = new Player(officialDice);
       Player p2 = new Player(officialDice);
       p1.joinScene(curr);
       p2.joinScene(curr);
       Extra l1 = new Extra(1, "Prospector");
       Lead l2 = new Lead(1, "Miner");
       p1.takeExtraRole(l1);
       p2.takeLeadRole(l2);
       System.out.println(p1.getExtraRole());
       System.out.println(p2.getLeadRole());
       p1.act();
       p2.act();
       l1.reward(p1);
       l2.reward(p2);
       System.out.println(p1.getCredits() + "\t" + p1.getDollars());
       System.out.println(p2.getCredits() + "\t" + p2.getDollars());
       
       
   }
   
   
   public static void popAdjList(Map<String,List<String>> adjacencyList) {
      List<String> mainAdj = Arrays.asList("Trailers", "Saloon", "Jail");
      List<String> jailAdj = Arrays.asList("Main Street", "General Store", "Train Station");
      List<String> storeAdj = Arrays.asList("Saloon","Ranch","Train Station","Jail");
      List<String> saloonAdj = Arrays.asList("Main Street","Trailers","General Store");
      List<String> trailersAdj = Arrays.asList("Main Street","Saloon","Hotel");
      List<String> bankAdj = Arrays.asList("Hotel","Church","Ranch","Saloon");
      List<String> hideoutAdj = Arrays.asList("Ranch","Casting Office","Church");
      List<String> trainAdj = Arrays.asList("Jail","General Store","Casting Office");
      List<String> castingAdj = Arrays.asList("Ranch","Secret Hideout","Train Station");
      List<String> ranchAdj = Arrays.asList("Casting Office","Secret Hideout","General Store","Bank");
      List<String> churchAdj = Arrays.asList("Secret Hideout","Bank","Hotel");
      List<String> hotelAdj = Arrays.asList("Church","Bank","Trailers");
      adjacencyList.put("Main Street",mainAdj);
      adjacencyList.put("Jail",jailAdj);
      adjacencyList.put("General Store",storeAdj);
      adjacencyList.put("Saloon",saloonAdj);
      adjacencyList.put("Trailers",trailersAdj);
      adjacencyList.put("Bank",bankAdj);
      adjacencyList.put("Secret Hideout",hideoutAdj);
      adjacencyList.put("Train Station",trainAdj);
      adjacencyList.put("Casting Office",castingAdj);
      adjacencyList.put("Ranch",ranchAdj);
      adjacencyList.put("Church",churchAdj);
      adjacencyList.put("Hotel",hotelAdj);
    }
    
    
 }
