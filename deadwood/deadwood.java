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
    public ActingSet getActingSet(){
        if(location != null){
            ActingSet currentSet = location;
            return currentSet;
        }
        return null;
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
    public void resetRole() {
        role = false;
        extraRole = null;
        actRole = null;
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

   public static abstract class Role{
    private int rankReq;

    //Abstract function with implementation provided by children:
    public abstract void reward(Player p);
  }

  public static class Lead extends Role{
    private String roleName;
    private int rankReq;
    private String blurb;
    public Lead(int val, String name, String talk) {
      rankReq = val;
      roleName = name;
      blurb = talk;
    }
    public String getName(){
        String actName = roleName;
        return actName;
    }
    public int getRank(){
        int rank = rankReq;
        return rank;
    }
    public int getBlurb(){
        return blurb;
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
    private List<Lead> leadList;
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
    public List<Lead> getLeadList(){
        List<Lead> theLeadList = leadList;
        return theLeadList;
    }
  }
  
  
  

  public class Day{
    private int dayNum;
    private List<ActingSet> setsList;
    private List<Scene> scenesList;
    private List<Player> playersList;
    private int numelems;

    public Day (List<ActingSet> sets, List<Scene> scenes, List<Player> players) {
        this.dayNum = 0;
        this.setsList = sets;
        this.scenesList = scenes;
        this.playersList = players;
        this.numelems = scenesList.size();
    }

    public void newDay () {
      /* Reset all  roles to false for players
        remove scenes from Sets list
        Choose new Scenes for Sets
        Reset all shotsLeft = shots
        iterate Day*/

        dayNum++;
        if(dayNum > 3) {
            System.out.println("Game Over");
        } else {

            //Resetting all roles to false for players
            for (Player player : playersList) {
                player.resetRole();
            }

            //Resetting shotLeft for all Sets
            for (ActingSet set : setsList) {
                set.resetShots();
            }

            for (int i=0;i<setsList.size();i++) {
                Random rand = new Random();
                int random = rand.nextInt(numelems);

                Scene scene = scenesList.get(random);
                scenesList.remove(random);
                numelems--;

                setsList.get(i).setScene(scene);

            }
        }
    }
  }
  

    public static class ActingSet { //implements Day{    <---
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

        public void resetShots(){
            int shotsLeft = shots;
        }
        
        public Scene getScene(){
            return scene;
        }
        
        public void setScene(Scene newscene){
            scene = newscene;
        }
        public List<Extra> getExtrasList(){
            List<Extra> temp = extrasList;
            return temp;
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

















   public static void main(String[]arg){
       Scanner console = new Scanner(System.in);
       
       System.out.print("> ");
       String cmd = console.nextLine();
       Dice officialDice = Dice.getDice();
       Map<String,List<String>> adjacencyList = new HashMap<String,List<String>>();
       popAdjList(adjacencyList);
       //Create list of all Player objects to iterate through
       //Create list of all Set objects to iterate through
       //Create Deck of scene to choose from for ending days


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

    /*private static void init() {

    } */
   
    private static void popAdjList(Map<String,List<String>> adjacencyList) {
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
    
   /* public static void populateExtrasList(){
    
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc2L1 = new Lead(2,"Railroad Worker", "");
        Lead sc3L1 = new Lead(2,"Railroad Worker", "");
        Lead sc4L1 = new Lead(2,"Railroad Worker", "");
        Lead sc5L1 = new Lead(2,"Railroad Worker", "");
        Lead sc6L1 = new Lead(2,"Railroad Worker", "");
        Lead sc7L1 = new Lead(2,"Railroad Worker", "");
        Lead sc8L1 = new Lead(2,"Railroad Worker", "");
        Lead sc9L1 = new Lead(2,"Railroad Worker", "");
        Lead sc10L1 = new Lead(2,"Railroad Worker", "");
        Lead sc11L1 = new Lead(2,"Railroad Worker", "");
        Lead sc12L1 = new Lead(2,"Railroad Worker", "");
        Lead sc13L1 = new Lead(2,"Railroad Worker", "");
        Lead sc14L1 = new Lead(2,"Railroad Worker", "");
        Lead sc15L1 = new Lead(2,"Railroad Worker", "");
        Lead sc16L1 = new Lead(2,"Railroad Worker", "");
        Lead sc17L1 = new Lead(2,"Railroad Worker", "");
        Lead sc18L1 = new Lead(2,"Railroad Worker", "");
        Lead sc19L1 = new Lead(2,"Railroad Worker", "");
        Lead sc20L1 = new Lead(2,"Railroad Worker", "");
        Lead sc21L1 = new Lead(2,"Railroad Worker", "");
        Lead sc22L1 = new Lead(2,"Railroad Worker", "");
        Lead sc23L1 = new Lead(2,"Railroad Worker", "");
        Lead sc24L1 = new Lead(2,"Railroad Worker", "");
        Lead sc25L1 = new Lead(2,"Railroad Worker", "");
        Lead sc26L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        Lead sc1L1 = new Lead(2,"Railroad Worker", "");
        
        List<Leads> sc1Leads = Arrays.asList(main1,main2,main3,main4);
        
    } */

    public static void populateLeadsList(){
    
        Extra main1 = new Extra(1,"Railroad Worker");
        Extra main2 = new Extra(2,"Falls off Roof");
        Extra main3 = new Extra(2,"Woman in Black Dress");
        Extra main4 = new Extra(4,"Mayor McGinty");
        Extra saloon1 = new Extra(1,"Reluctant Farmer");
        Extra saloon2 = new Extra(2,"Woman in Red Dress");
        Extra ranch1 = new Extra(1,"Shot in Leg");
        Extra ranch2 = new Extra(2,"Saucy Fred");
        Extra ranch3 = new Extra(3,"Man Under Horse");
        Extra hideout1 = new Extra(1,"Clumsy Pit Fighter");
        Extra hideout2 = new Extra(2,"Thug with Knife");
        Extra hideout3 = new Extra(3,"Dangerous Tom");
        Extra hideout4 = new Extra(4,"Penny, who is Lost");
        Extra bank1 = new Extra(2,"Suspicious Gentleman");
        Extra bank2 = new Extra(3,"Flustered Teller");
        Extra church1 = new Extra(1,"Dead Man");
        Extra church2 = new Extra(2,"Crying Woman");
        Extra hotel1 = new Extra(1,"Faro Player");
        Extra hotel2 = new Extra(1,"Sleeping Drunkard");
        Extra hotel3 = new Extra(2,"Falls from Balcony");
        Extra hotel4 = new Extra(3,"Australian Bartender");
        Extra jail1 = new Extra(2,"Prisoner in Cell");
        Extra jail2 = new Extra(3,"Feller in Irons");
        Extra store1 = new Extra(1,"Man in Overalls");
        Extra store2 = new Extra(3,"Mister Keach");
        Extra train1 = new Extra(1,"Dragged by Train");
        Extra train2 = new Extra(1,"Crusty Prospector");
        Extra train3 = new Extra(2,"Preacher with Bag");
        Extra train4 = new Extra(4,"Cyrus the Gunfighter");
        
        List<Extra> mainExtras = Arrays.asList(main1,main2,main3,main4);
        List<Extra> jailExtras = Arrays.asList(jail1,jail2);
        List<Extra> storeExtras = Arrays.asList(store1,store2);
        List<Extra> saloonExtras = Arrays.asList(saloon1,saloon2);
        List<Extra> bankExtras = Arrays.asList(bank1,bank2);
        List<Extra> hideoutExtras = Arrays.asList(hideout1,hideout2,hideout3,hideout4);
        List<Extra> trainExtras = Arrays.asList(train1,train2,train3,train4);
        List<Extra> ranchExtras= Arrays.asList(ranch1,ranch2,ranch3);
        List<Extra> churchExtras = Arrays.asList(church1,church2);
        List<Extra> hotelExtras = Arrays.asList(hotel1,hotel2,hotel3,hotel4);
        
    }
    
    
    private static ActingSet findActingSet(String room, ActingSet [] check){
        for(int i = 0; i < 10; i ++){
            if(check[i].getName().equals(room)){
                return check[i];
            }
        }
        return null;
    }
    private static Lead findLead(String part, List<Lead> leadList){
        for(int i =0; i < 1; i ++){
            if(leadList.get(i).getName().equals(part)){
                return leadList.get(i);
            }
        }
        return null;
    }
    private static Extra findExtra(String part, List<Extra> extraList){
        for(int i =0; i < 1; i ++){
            if(extraList.get(i).getName().equals(part)){
                return extraList.get(i);
            }
        }
        return null;
    }
    
    private static void CommandExec(Player p, String cmd, ActingSet [] list){
        if(cmd.equals("who")){
            System.out.print("\nPlayer " + p.getId() + " has $" + p.getDollars() + " and " + p.getCredits() + " credits ");
            String name = p.getLeadRole();
            String name2 = p.getExtraRole();
            if(name != null){
                System.out.print("and is working on " + name + ".\n");
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
            if(p.getActingSet().getScene().getName() != null){
                System.out.print(" where " + p.getActingSet().getScene().getName() + ", " + "scene " + p.getActingSet().getScene().getName() +" is shooting.\n");
            }
            else{
                System.out.print(" and you are not currently shooting a scene.\n");
            }
        }
        else if(cmd.substring(0,4).equals("move")){
            ActingSet room = findActingSet(cmd.substring(5), list);
            if(room == null){
                System.out.println("The room you tried to move to was an invalid room");
            }
            else{
                p.move(room);
                System.out.print("Player " + p.getId() + " has moved to room " + room.getName() + ".\n");
            }
        }
        else if(cmd.substring(0,4).equals("work")){
            Lead isLead = findLead(cmd.substring(5),p.getActingSet().getScene().getLeadList());
            Extra isExtra = findExtra(cmd.substring(5),p.getActingSet().getExtrasList());
            if(isLead == null){
                if(isExtra == null){
                    System.out.print("I'm sorry but that role doesn't exist...");
                }
                p.takeExtraRole(isExtra);
                System.out.print("Player " + p.getId() + " has taken the Extra role of " + p.getExtraRole() + ".\n");
            }
            else{
                p.takeLeadRole(isLead);
                System.out.print("Player " + p.getId() + " has taken the Lead role of " + p.getLeadRole() + ".\n");
            }
        }
    }
    
 }
