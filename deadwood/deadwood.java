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
    private int id;
    private int dollars = 0;
    private int credits = 0;
    private int rehearseCount = 0;
    private int rank = 1;
    private int score = 0;
    private boolean role = false;
    private Lead actRole;
    private Extra extraRole;
    private boolean actSuccessful;
    private ActingSet location;
    
    private String local = "Trailer";
    public Player(Dice pDice, int pId){
        this.dice = pDice;
        this.id = pId;
    }
    public int getId(){
        int thisId = id;
        return thisId;
    }
    public int getDollars(){
        int bucks = dollars;
        return bucks;
    }
    public int getCredits(){
        int creds = credits;
        return creds;
    }
    public int getRank(){
        int pRank = rank;
        return pRank;
    }
    public int getScore(){
        int Score = score;
        return Score;
    }
    public String getLocal(){
        if(location != null){
            this.local = location.getName();
            return local;
        }
        return null;
    }
    public String getSceneName(){
        if(location != null){
            String roomScene = location.getScene().getName();
            return roomScene;
        }
        return null;
    }
    public int getSceneId(){
        if(location != null){
            int sceneId = location.getScene().getId();
            return sceneId;
        }
        return 0;
    }
    public void setDollars(int amount){
        this.dollars += amount;
    }
    public void setCredits(int amount){
        this.credits += amount;
    }
    public void setRank(int newRank) {
        this.rank = newRank;
    }
    public String getLeadRole(){
        if(actRole != null){
            String name = actRole.getName();
            return name;
        }
        return null;
    }
    public String getExtraRole(){
        if(extraRole != null){
            String name = extraRole.getName();
            return name;
        }
        return null;
    }

    public boolean act(){
      int getRoll = dice.roll();
      System.out.println(getRoll);
      int budget = location.getScene().getBudget();   //Identify which scene based on the players position
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
    public void move(ActingSet location){
        this.location = location;
      /* If location is in adjacent set, move, else choose a different room */
    }
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
    private String name;
    private int sceneNum;
    public Scene(int budget,int numRoles, String name, int num){
        this.budget = budget;
        this.numRoles = numRoles;
        this.sceneNum = num;
        this.name = name;
    }
    public int getBudget(){
        int budg = budget;
        return budg;
    }
    public String getName(){
        if(name != null){
            String sName = name;
            return sName;
        }
        return null;
    }
    public int getId(){
        int id = sceneNum;
        return id;
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
  /* public static class ActingSet{
    private int shots;
    private int shotsLeft;
    private Scene scene;
    private List<Scene> scenelist;
    private List<Extra> extraList;
    private String name;
    public ActingSet(String name, Scene scene){
        this.name = name;
        this.scene = scene;
    }
    public Scene getScene(){
        return scene;
    }
    public String getName(){
        return name;
    }*/

    public class ActingSet { //implements Day{    <---
        private String name;
        private int shots;
        private int shotsLeft;
        private Scene scene; //add list of roles to scene
        private List<Extra> extrasList;
        private Map<String,List<String>> adjacencyList;
        
        public ActingSet (String name, int shots, int shotsLeft, Scene scene, List<Extra> extrasList, Map<String,List<String>> adjacencyList){
            this.name = name;
            this.shots = shots;
            this.shotsLeft = shotsLeft;
            this.scene = scene;
            this.extrasList = extrasList;
            this.adjacencyList = adjacencyList;
        }
        
        public String getName(){
            String thisName = name;
            return thisName;
        }
        
        public int getShots(){
            int thisShots = shots;
            return thisShots;
        }
        
        public int getShotsLeft(){
            int thisShotsLeft = shotsLeft;
            return thisShotsLeft;
        }
        
        public Scene getScene(){
            return scene;
        }
        
        

    }
    
    public static class CastingOffice {
    
        private Map<String,List<String>> adjacencyList;
    
        public CastingOffice (Map<String,List<String>> adjacencyList) {
            this.adjacencyList = adjacencyList;
        }
        
        public void upgrade_wDollars (Player p, int dollars){
        
            switch (dollars) {
                case 4 :
                    if (p.getDollars() >= 4){
                        p.setDollars(-4);
                        p.setRank(2);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        System.out.println("Insufficient Dollars");
                    }
                    break;
                case 10 :
                    if (p.getDollars() >= 10){
                        p.setDollars(-10);
                        p.setRank(3);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        System.out.println("Insufficient Dollars");
                    }
                    break;
                case 18 :
                    if (p.getDollars() >= 18){
                        p.setDollars(-18);
                        p.setRank(4);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        System.out.println("Insufficient Dollars");
                    }
                    break;
                case 28 :
                    if (p.getDollars() >= 28){
                        p.setDollars(-28);
                        p.setRank(5);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        System.out.println("Insufficient Dollars");
                    }
                    break;
                case 40 :
                    if (p.getDollars() >= 40){
                        p.setDollars(-40);
                        p.setRank(6);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        System.out.println("Insufficient Dollars");
                    }
                    break;
                default : 
                    System.out.println("Invalid dollar amount");
            }
        }
        
        public void upgrade_wCreds (Player p, int credits) {
            switch (credits) {
            
                case 5 :
                    if (p.getCredits() >= 5){
                        p.setCredits(-5);
                        p.setRank(2);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        System.out.println("Insufficient Credits");
                    }
                    break;
                    
                case 10 :
                    if (p.getCredits() >= 10){
                        p.setCredits(-10);
                        p.setRank(3);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        System.out.println("Insufficient Credits");
                    }
                    break;
                    
                case 15 :
                    if (p.getCredits() >= 15){
                        p.setCredits(-15);
                        p.setRank(4);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        System.out.println("Insufficient Credits");
                    }
                    break;
                    
                case 20 :
                    if (p.getCredits() >= 20){
                        p.setCredits(-20);
                        p.setRank(5);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        System.out.println("Insufficient Credits");
                    }
                    break;
                    
                case 25 :
                    if (p.getCredits() >= 25){
                        p.setCredits(-25);
                        p.setRank(6);
                        System.out.println("Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        System.out.println("Insufficient Credits");
                    }
                    break;
                    
                default : 
                    System.out.println("Invalid credit amount");
            }
        }
    }










//   public class Trailer extends Set{
// 
//   }
//   public abstract class Set{
//     private List<Set> adjRooms;
// 
//   }









   public static void main(String[]arg){
       Scanner console = new Scanner(System.in);
       
       System.out.print("> ");
       String cmd = console.next();
       Dice officialDice = Dice.getDice();
       Map<String,List<String>> adjacencyList = new HashMap<String,List<String>>();
       popAdjList(adjacencyList);
//        Scene curr = new Scene(4,3, "Wild West",20);
//        ActingSet here = new ActingSet("Salooon", curr);
//        Player p1 = new Player(officialDice,1);
//        Player p2 = new Player(officialDice,2);
//        p1.move(here);
//        p2.move(here);
//        Extra l1 = new Extra(1, "Prospector");
//        Lead l2 = new Lead(1, "Miner");
//        p1.takeExtraRole(l1);
//        p2.takeLeadRole(l2);
//        p1.act();
//        p2.act();
//        l1.reward(p1);
//        l2.reward(p2);
//        CommandExec(p1,cmd);
//        CommandExec(p2,cmd);
       
       
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
    
    public static void CommandExec(Player p, String cmd){
        if(cmd.equals("who")){
            System.out.print("\nPlayer " + p.getId() + " has $" + p.getDollars() + " and " + p.getCredits() + " credits ");
            String name = p.getLeadRole();
            String name2 = p.getExtraRole();
            if(name != null){
                System.out.print("and is working on " + name);
            }
            else if(name2 != null){
                System.out.print("and is working on " + name2 + ".\n");
            }
            else{
                System.out.print("and is currently not working on a role.\n");
            }
        }
        else if(cmd.equals("Where")){
            System.out.print("You are in the " + p.getLocal());
            if(p.getSceneName() != null){
                System.out.print(" where " + p.getSceneName() + ", " + "scene " + p.getSceneId() +" is shooting.\n");
            }
            else{
                System.out.print(" and you are not currently shooting a scene.\n");
            }
        }
    }
    
 }
