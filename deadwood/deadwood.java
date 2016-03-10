/* Marshall Tanis, Matt Watkins, Elijah Baldwin
 *
 * Deadwood
 *
 * Modified 2/19/2016
 */

import processing.core.*;
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
        String area = local;
        return area;
    }
    public void setActSuccesful(){
        this.actSuccessful = false;
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
      System.out.print(getRoll + "\n");
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
    public void move(CastingOffice office){
        this.local = "Casting Office";
        this.location = null;
    }
    public void move(Trailer trailer){
        this.local = "Trailer";
        this.location = null;
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
    public String getBlurb(){
        return blurb;
    }
    //provide credits reward on success:
    //shotsLeft for ActingSet is also decremented.
    public void reward(Player p) {
      //reward on success only:
      if (p.actSuccessful == true)
        p.setCredits(2);
      //ActingSet.setShotsLeft(ActingSet.getShotsLeft() - 1);
    }
  }
  
  public static class Extra extends Role{
    // provide money and credit reward on Success, money only on failure:
    // shotsLeft for ActingSet is also decremented.
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
        p.setCredits(1);
        p.setDollars(1);
      //reward on failure:
      } else {
        p.setDollars(1);
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
    public Scene(int budget,int numRoles, String name, int num, List<Lead> leadList){
        this.budget = budget;
        this.numRoles = numRoles;
        this.sceneNum = num;
        this.name = name;
        this.leadList = leadList;
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

  

    public static class ActingSet { 
        private String name;
        private int shots;
        private int shotsLeft;
        private Scene scene; //add list of roles to scene
        private List<Extra> extrasList;
        private List<String> adjacencyList;
        
        public ActingSet (String name, int shots, int shotsLeft, Scene scene, List<Extra> extrasList, List<String> adjacencyList){
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
        
        public void setShots(int newShots){
	    this.shots = newShots;
        }
        
        public void setShotsLeft(int newShotsLeft){
	  this.shotsLeft = newShotsLeft;
        }
        
        public void setScene(Scene newscene){
            this.scene = newscene;
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
    public static class Trailer{
        public Trailer(){
        }
    }


   public static void main(String[]arg){
       Scanner console = new Scanner(System.in);
       
       
       Dice officialDice = Dice.getDice();
       Map<String,List<String>> adjacencyList = new HashMap<String,List<String>>();
       Map<String,List<Extra>> extrasList = new HashMap<String,List<Extra>>();
       popAdjList(adjacencyList);
       populateExtrasList(extrasList);
       ActingSet MainStreet = new ActingSet("Main Street",4,4,null,extrasList.get("Main Street"),adjacencyList.get("Main Street"));
       //Create list of all Player objects to iterate through
       //Create list of all Set objects to iterate through
       //Create Deck of scene to choose from for ending days
       Extra l1 = new Extra(1, "Prospector");
       Lead l2 = new Lead(1, "Miner", "Boom Pow"); 
       List<Lead> leads = new ArrayList<Lead>();
       List<Extra> extras = new ArrayList<Extra>();
       leads.add(l2);
       extras.add(l1);
       CastingOffice office = new CastingOffice(adjacencyList);
       Scene curr = new Scene(4,3, "Wild West",20, leads);
       //ActingSet here = new ActingSet("Salooon", 3, 3, curr, extras, adjacencyList);
       Player p1 = new Player(officialDice,1);
       Player p2 = new Player(officialDice,2);
       //ActingSet []list = {here};
       Trailer trailer = new Trailer();
       p1.setDollars(4);
       while(true){
        System.out.print("> ");
        String cmd = console.nextLine();
        //CommandExec(p1,cmd,list, office, trailer);
       }
    }

    /*private static void init() {

    } */
   
  /*  private static void popAdjList(Map<String,List<String>> adjacencyList) {
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
    
    public static void populateLeadsList(){
    
        Lead sc1L1 = new Lead(2,"Defrocked Priest", "Look out below!");
        Lead sc1L2 = new Lead(3,"Marshal Canfield", "Hold fast!");
        Lead sc1L3 = new Lead(4,"One-Eyed Man", "Balderdash!");

        Lead sc2L1 = new Lead(2,"Squeaking Boy", "I'll say!");
        Lead sc2L2 = new Lead(4,"Pharaoh Imhotep", "Attack, soldiers!");
        Lead sc2L3 = new Lead(6,"Aunt Martha", "You got nothin!");

        Lead sc3L1 = new Lead(1,"Rug Merchant", "Don't leave my store!");
        Lead sc3L2 = new Lead(2,"Banker", "Trust me.");
        Lead sc3L3 = new Lead(5,"Talking Mule", "Nice work, Johnny!");

        Lead sc4L1 = new Lead(4,"The Duck", "Waaaak!");
        Lead sc4L2 = new Lead(6,"His Brother", "Waaaaaaak!");

        Lead sc5L1 = new Lead(5,"Auctioneer", "Going once!");
        Lead sc5L2 = new Lead(6,"General Custer", "Go West!");

        Lead sc6L1 = new Lead(2,"Town Drunk", "Even me!");
        Lead sc6L2 = new Lead(4,"Squinting Miner", "Sure we can!");
        Lead sc6L3 = new Lead(5,"Poltergeist", "Wooooo!");

        Lead sc7L1 = new Lead(3,"Drunk", "Where's Willard?");
        Lead sc7L2 = new Lead(4,"Librarian", "Shhhhh!");
        Lead sc7L3 = new Lead(6,"Man with Hay", "Hey!");

        Lead sc8L1 = new Lead(1,"Angry Barber", "Hold him still!");
        Lead sc8L2 = new Lead(3,"Woman with Board", "Nonsense, Frank!");
        Lead sc8L3 = new Lead(5,"Man on Fire", "It burns!");

        Lead sc9L1 = new Lead(2,"Hollering Boy", "Over here, mister!");
        Lead sc9L2 = new Lead(3,"Drunk Farmer", "Git outta my barn!");
        Lead sc9L3 = new Lead(5,"Meek Little Sarah", "He's so cute!");

        Lead sc10L1 = new Lead(1,"Sleeping Man", "Snnkkk snnkk snnkk");
        Lead sc10L2 = new Lead(2,"Man with Pig", "Tally-Hooo!");
        Lead sc10L3 = new Lead(4,"Shooter", "Where's my britches?");

        Lead sc11L1 = new Lead(1,"Buster", "One two three go!");
        Lead sc11L2 = new Lead(4,"Man Reading Paper", "Ouchie!");
        Lead sc11L3 = new Lead(5,"Fate Pete", "Nice kick, boss!");

        Lead sc12L1 = new Lead(2,"Shot in Back", "Arrrggh!");
        Lead sc12L2 = new Lead(4,"Shot in Leg", "Ooh, lordy!");
        Lead sc12L3 = new Lead(5,"Leaps into Cake", "Dangit, Jesse!");

        Lead sc13L1 = new Lead(6,"Martin", "Have you tried soy cheese?");

        Lead sc14L1 = new Lead(2,"Piano Player", "It's a nocturne!");
        Lead sc14L2 = new Lead(3,"Man in Turban", "My stars!");
        Lead sc14L3 = new Lead(4,"Falls on Hoe", "Ow!");

        Lead sc15L1 = new Lead(3,"Preacher", "My word!");
        Lead sc15L2 = new Lead(6,"Amused Witness", "Tee hee hee!");

        Lead sc16L1 = new Lead(1,"Falls from Tree", "What ho!");
        Lead sc16L2 = new Lead(3,"Laughing Woman", "Tis to laugh!");
        Lead sc16L3 = new Lead(4,"Man with Whistle", "Tweeeeet!");

        Lead sc17L1 = new Lead(3,"Curious Girl", "Are you sure?");
        Lead sc17L2 = new Lead(4,"Ghost of Plato", "It happened to me!");

        Lead sc18L1 = new Lead(4,"Ex-Convict", "Never again!");
        Lead sc18L2 = new Lead(6,"Man with Onion", "Fresh onions!");

        Lead sc19L1 = new Lead(2,"Suprised Bison", "Mmrrrrrph!");
        Lead sc19L2 = new Lead(4,"Man with Horn", "Ta daaaa!");

        Lead sc20L1 = new Lead(3,"Staggering Man", "You never know!");
        Lead sc20L2 = new Lead(5,"Woman with Beer", "Howdy, stranger!");
        Lead sc20L3 = new Lead(6,"Marcie", "Welcome home!");

        Lead sc21L1 = new Lead(4,"Looks like Elvis", "Thankyouverymuch");
        Lead sc21L2 = new Lead(5,"Singing Dead Man", "Yeah!");
        Lead sc21L3 = new Lead(6,"Apothecary", "Such drugs I have.");

        Lead sc22L1 = new Lead(1,"Flustered Man", "Well, I never!");
        Lead sc22L2 = new Lead(2,"Space Monkey", "Ook!");
        Lead sc22L3 = new Lead(5,"Cowbot Dan", "Bzzzzzt!");

        Lead sc23L1 = new Lead(2,"Jailer", "You there!");
        Lead sc23L2 = new Lead(4,"Mephistopheles", "Be not afraid!");
        Lead sc23L3 = new Lead(5,"Breaks a Window", "Oops!");

        Lead sc24L1 = new Lead(1,"Man in Poncho", "Howdy, Jones!");
        Lead sc24L2 = new Lead(3,"Ecstatic Housewife", "This is fine!");
        Lead sc24L3 = new Lead(5,"Isaac", "The mail!");

        Lead sc25L1 = new Lead(5,"Film Critic", "Implausible!");
        Lead sc25L2 = new Lead(6,"Hobo with Bat", "Nice house!");

        Lead sc26L1 = new Lead(2,"Cow", "Moo.");
        Lead sc26L2 = new Lead(3,"St. Clement of Alexandria", "Peace be with you, child!");
        Lead sc26L3 = new Lead(4,"Josie", "Yikes!");

        Lead sc27L1 = new Lead(3,"Bewhisker'd Cowpoke", "Oh, sweet Lord!");
        Lead sc27L2 = new Lead(5,"Dog", "Wurf!");

        Lead sc28L1 = new Lead(2,"Willard", "Ain't that a sight?");
        Lead sc28L2 = new Lead(3,"Leprechaun", "Begorrah!");
        Lead sc28L3 = new Lead(5,"Startled Ox", "Mrr?");

        Lead sc29L1 = new Lead(1,"Shot in Head", "Arrrgh!");
        Lead sc29L2 = new Lead(4,"Leaps Out of Cake", "Oh, for Pete's sake!");
        Lead sc29L3 = new Lead(6,"Shot Three Times", "Ow! Ow! Ow!");

        Lead sc30L1 = new Lead(2,"Voice of God", "Grab hold, son!");
        Lead sc30L2 = new Lead(3,"Hands of God", "!");
        Lead sc30L3 = new Lead(4,"Jack Kemp", "America!");

        Lead sc31L1 = new Lead(5,"Opice (Monkey)", "Ukk! (Ook)!");
        Lead sc31L2 = new Lead(6,"Man with Gun", "Hold it right there!");

        Lead sc32L1 = new Lead(1,"Man with Rope", "Look out below!");
        Lead sc32L2 = new Lead(2,"Svetlana", "Says who?");
        Lead sc32L3 = new Lead(5,"Accidental Victim", "Ow! My spine!");

        Lead sc33L1 = new Lead(1,"Thrifty Mike", "Call!");
        Lead sc33L2 = new Lead(3,"Sober Physician", "Raise!");
        Lead sc33L3 = new Lead(5,"Man on Floor", "Fold!");

        Lead sc34L1 = new Lead(2,"Very Wet Man", "Sheesh!");
        Lead sc34L2 = new Lead(4,"Dejected Housewife", "Its time had come.");
        Lead sc34L3 = new Lead(5,"Man with Box", "Progress!");

        Lead sc35L1 = new Lead(3,"Liberated Nun", "Let me have it!");
        Lead sc35L2 = new Lead(5,"Witch Doctor", "Oogie Boogie!");
        Lead sc35L3 = new Lead(6,"Voice of Reason", "Come on, now!");

        Lead sc36L1 = new Lead(4,"Marksman", "Pull!");
        Lead sc36L2 = new Lead(5,"Postal Worker", "It's about time!");
        Lead sc36L3 = new Lead(6,"A Horse", "Yes Sir!");

        Lead sc37L1 = new Lead(2,"Burning Man", "Make it stop!");
        Lead sc37L2 = new Lead(4,"Cheese Vendor", "Opa!");
        Lead sc37L3 = new Lead(5,"Hit with Table", "Ow! A table?");

        Lead sc38L1 = new Lead(2,"Fraternity Pledge", "Beer me!");
        Lead sc38L2 = new Lead(6,"Man with Sword", "None shall pass!");

        Lead sc39L1 = new Lead(3,"Detective", "I have a hunch.");
        Lead sc39L2 = new Lead(4,"File Clerk", "My stapler!");
        Lead sc39L3 = new Lead(5,"Cindy Lou", "Dear Lord!");

        Lead sc40L1 = new Lead(2,"Farmer", "Git off a that!");
        Lead sc40L2 = new Lead(4,"Exploding Horse", "Boom!");
        Lead sc40L3 = new Lead(6,"Jack", "Here we go again!");
        
        List<Leads> sc1Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc2Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc3Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc4Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc5Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc6Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc7Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc8Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc9Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc10Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc11Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc12Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc13Leads = Arrays.asList(sc1L1);
        List<Leads> sc14Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc15Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc16Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc17Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc18Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc19Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc20Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc21Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc22Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc23Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc24Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc25Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc26Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc27Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc28Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc29Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc30Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc31Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc32Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc33Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc34Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc35Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc36Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc37Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc38Leads = Arrays.asList(sc1L1, sc1L2);
        List<Leads> sc39Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Leads> sc40Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        
    } */


    public static void populateExtrasList(Map<String,List<Extra>> extrasList){
    
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
        
        extrasList.put("Main Street",mainExtras);
        extrasList.put("Jail",jailExtras);
        extrasList.put("General Store",storeExtras);
        extrasList.put("Saloon",saloonExtras);
        extrasList.put("Bank",bankExtras);
        extrasList.put("Secret Hideout",hideoutExtras);
        extrasList.put("Train",trainExtras);
        extrasList.put("Ranch",ranchExtras);
        extrasList.put("Church",churchExtras);
        extrasList.put("Hotel",hotelExtras);
        
        
    }
    
    
    private static ActingSet findActingSet(String room, ActingSet [] check){
        for(int i = 0; i < 1; i ++){
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
    
    private static void CommandExec(Player p, String cmd, ActingSet [] list, CastingOffice office, Trailer trailer){
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
            if(p.getActingSet() != null){
                System.out.print(" where " + p.getActingSet().getScene().getName() + ", " + "scene " + p.getActingSet().getScene().getName() +" is shooting.\n");
            }
            else if(p.getLocal().equals("Casting Office")){
                System.out.print(" where there is no scene that is ever worked on.\n");
            }
            else if(p.getLocal().equals("Trailer")){
                System.out.print(" where there is no scene ever shot.\n");
            }
            else{
                System.out.print(" and you are not currently shooting a scene.\n");
            }
        }
        else if(cmd.equals("Rehearse")){
            p.rehearse();
            System.out.print("Player " + p.getId() + " has rehearsed this turn.\n");
        }
        else if(cmd.equals("Act")){
            Lead LeadRole = findLead(p.getLeadRole(), p.getActingSet().getScene().getLeadList());
            Extra ExtraRole = findExtra(p.getExtraRole(), p.getActingSet().getExtrasList());
            if(LeadRole != null){
                p.act();
                LeadRole.reward(p);
                System.out.print("Player " + p.getId() + " now has $" + p.getDollars() + " and " + p.getCredits() + " credits.\n");
                p.setActSuccesful();
            }
            else if(ExtraRole != null){
                p.act();
                ExtraRole.reward(p);
                System.out.print("Player " + p.getId() + " now has $" + p.getDollars() + " and " + p.getCredits() + " credits.\n");
                p.setActSuccesful();
            }
        }
         else if(cmd.equals("end")){
            System.out.print("Player " + p.getId() + "'s turn is over... Please pass the computer to the next player.\n");
        }
        else if(cmd.substring(0,4).equals("move")){
            ActingSet room = findActingSet(cmd.substring(5), list);
            if(room == null && (!(cmd.substring(5).equals( "Casting Office")) && !(cmd.substring(5).equals("Trailer")))){
                System.out.println("The room you tried to move to was an invalid room");
            }
            else{
                if(room == null){
                    if(cmd.substring(5).equals("Casting Office")){
                        p.move(office);
                        System.out.print("Player " + p.getId() + " has moved to room " + p.getLocal() + ".\n");
                    }
                    else{
                        p.move(trailer);
                        System.out.print("Player " + p.getId() + " has moved to room " + p.getLocal() + ".\n");
                    }
                }
                else{
                    p.move(room);
                    System.out.print("Player " + p.getId() + " has moved to room " + p.getActingSet().getName() + ".\n");
                }
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
        else if(cmd.substring(0,7).equals("upgrade")){
            if(cmd.substring(8,9).equals("$")){
                if(p.getLocal().equals("Casting Office")){
                    int dollars = Integer.parseInt(cmd.substring(10));
                    office.upgrade_wDollars(p,dollars);
                }
            }
            else if(cmd.substring(8,10).equals("cr")){
                if(p.getLocal().equals("Casting Office")){
                    int credits = Integer.parseInt(cmd.substring(11));
                    office.upgrade_wCreds(p,credits);
                }
            }
        }
        else{
            System.out.print("That was not a valid command. Please read the README with any questions");
        }
        
        
    }
    
 }
