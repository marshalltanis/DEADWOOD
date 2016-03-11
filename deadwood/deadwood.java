/* Marshall Tanis, Matt Watkins, Elijah Baldwin
 *
 * Deadwood
 *
 * Modified 2/23/2016
 */

import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.Color;


 public class deadwood extends JFrame{

/* GLOBALS */
static Map<String,List<String>> adjacencyList = new HashMap<String,List<String>>();
static Map<String,List<Extra>> extrasList = new HashMap<String,List<Extra>>();
static Map<String,ActingSet> actingSetList = new HashMap<String,ActingSet>();
public static int activeScenes = 10;

public static Graphics g;
private static JPanel wind = new JPanel();
private JFrame frame = new JFrame(){
        Image background = ImageIO.read(new File("background.jpg"));
        public void paint(Graphics g){
            super.paint(g);
            g.drawImage(background,0,0,this);
        }
    };
    
    public deadwood() throws IOException{
        frame.setSize(1280,720);
        frame.setResizable(false);
        frame.setTitle("Deadwood");
        
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    
}
/* Player class */
  public static class Player{
    private int x = 1107;
    private int y = 261;
    private int width = 10;
    private int height = 10;
    private Dice dice;
    private int id;
    private int dollars = 0;
    private int credits = 0;
    private int rehearseCount = 0;
    private int rank = 1;
    private int score = 0;
    private boolean role = false;
    private boolean haveMoved = false;
    public boolean haveActed = false;
    public boolean thisTurn = false;
    private Lead actRole;
    private Extra extraRole;
    public boolean actSuccessful;
    private ActingSet location;

    private String local = "Trailers";
    
    public void drawPlayer(Graphics g){
        if(id == 1){
            g.setColor(Color.RED);
            g.fillRect(x,y,width,height);
        }
        else if(id == 2){
            g.setColor(Color.BLUE);
            g.fillRect(x + 12, y+12, width, height);
        }
        else if(id == 3){
            g.setColor(Color.GREEN);
            g.fillRect(x + 24, y + 24, width, height);
        }
        else if(id == 4){
            g.setColor(Color.YELLOW);
        }
        else if(id == 5){
            g.setColor(Color.MAGENTA);
        }
        else if(id == 6){
            g.setColor(Color.GRAY);
        }
        else if(id == 7){
            g.setColor(Color.CYAN);
        }
        else if(id == 8){
            g.setColor(Color.ORANGE);
        }
        
    }
    public Player(Dice pDice, int pId){
        this.dice = pDice;
        this.id = pId;
    }
    public Player(Dice pDice, int pId, int startCreds){
        this.dice = pDice;
        this.id = pId;
        this.credits = startCreds;
    }
    public Player(Dice pDice, int pId, int startCreds, int startRank){
        this.dice = pDice;
        this.id = pId;
        this.credits = startCreds;
        this.rank = startRank;
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

    public void resetTurn() {
        haveActed = false;
        haveMoved = false;
        thisTurn = false;
    }

    public void act(){
        if (this.haveActed==false) {
              int getRoll = dice.roll();
              JOptionPane.showMessageDialog(null,getRoll + "\n");
              int budget = location.getScene().getBudget();   //Identify which scene based on the players position
              this.haveActed = true;
              if(getRoll >= budget - rehearseCount){
                this.actSuccessful = true;
              }
              else{
                this.actSuccessful = false;
              }
        } else {
            JOptionPane.showMessageDialog(null,"Too tired to act, need to wait till next turn.");
        }
    }
    public void takeLeadRole(Lead roll){
      if(role == false && rank >= roll.getRank()){
        role = true;
        actRole = roll;
        
      }
      else{
        JOptionPane.showMessageDialog(null,"Invalid Action, not high enough level or you have a role currently\n");
      }
    }
    public void takeExtraRole(Extra roll){
      if(role == false && rank >= roll.getRank()){
        role = true;
        extraRole = roll;
      }
      else{
        JOptionPane.showMessageDialog(null,"Invalid Action, not high enough level or you have a Role currently\n");
      }
    }
    public void move(ActingSet location){
      /* If location is in adjacent set, move, else choose a different room */
        if (this.haveMoved == false) {
            this.location = location;
            this.local = location.getName();
            this.haveMoved = true;
        } else {
            JOptionPane.showMessageDialog(null,"Already moved, need to wait till next turn to move again.\n");
        }
    }
    public void move(CastingOffice office){
        if (this.haveMoved == false) {
            this.local = "Casting Office";
            this.location = null;
            this.haveMoved = true;
        } else {
            JOptionPane.showMessageDialog(null,"Already moved, need to wait till next turn to move again.");
        }
    }
    public void move(Trailer trailer){
        if (this.haveMoved == false) {
            this.local = "Trailers";
            this.location = null;
            this.haveMoved = true;
        } else {
            JOptionPane.showMessageDialog(null,"Already moved, need to wait till next turn to move again.");
        }
    }
    /*public void rehearse(){
      this.rehearse ++;
    }*/


    public void rehearse(){
        /*add 1 to rehearseCount */
        if (this.haveActed == false) {
            this.rehearseCount ++;
            this.haveActed = true;
        } else {
            JOptionPane.showMessageDialog(null,"Too tired to rehearse, need to wait till next turn.");
        }
    }
}

/* Dice class */
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

/* Lead class */
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

/* Extra class */
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
        p.thisTurn = true;
      //reward on failure:
      } else {
        if(p.thisTurn == false && p.haveActed == true){
            p.thisTurn = true;
            p.setDollars(1);
            
        }
      }
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

/* Day class */
  public static class Day{
    private int dayNum;
    private int lastDay;
    private Map<String,ActingSet> setsList;
    private List<Scene> scenesList;
    private List<Player> playersList;
    private int numelems;


    public Day (List<Scene> scenes, List<Player> players) {
        this.dayNum = 0;
        this.lastDay = 4;
        this.setsList = actingSetList;
        this.scenesList = scenes;
        this.playersList = players;
        this.numelems = scenesList.size();
    } public Day (List<Scene> scenes, List<Player> players, int day) {
        this.dayNum = 0;
        this.lastDay = day;
        this.setsList = actingSetList;
        this.scenesList = scenes;
        this.playersList = players;
        this.numelems = scenesList.size();
    }

    public boolean newDay () {
      /* Reset all  roles to false for players
        remove scenes from Sets list
        Choose new Scenes for Sets
        Reset all shotsLeft = shots
        iterate Day*/

        dayNum++;
        if(dayNum > lastDay) {
            JOptionPane.showMessageDialog(null,"Game Over");
            return true;
        } else {

            //Resetting all roles to false for players
            for (Player player : playersList) {
                player.resetRole();
            }

            //Resetting shotLeft for all Sets
            for (ActingSet set : setsList.values()) {
                set.resetShots();
            }

            for (ActingSet set : setsList.values()) {
                Random rand = new Random();
                int random = rand.nextInt(numelems);
                Scene scene = scenesList.get(random);
                String sceneName = scene.getName();

                if(sceneName == null){
                    JOptionPane.showMessageDialog(null,"Could not start new day");
                }
                scenesList.remove(random);
                numelems--;

                //System.out.println(set.getName());
                set.setScene(scene);
            }
        }
        return false;
    }
  }


/* ActingSet class */
    public static class ActingSet {
        private String name;
        private int shots;
        private int shotsLeft;
        private Scene scene; //add list of roles to scene
        private List<Extra> extrasList;

        public ActingSet (String name, int shots, int shotsLeft, Scene scene, List<Extra> extrasList){
            this.name = name;
            this.shots = shots;
            this.shotsLeft = shotsLeft;
            this.scene = scene;
            this.extrasList = extrasList;
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

/* CastingOffice */
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
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Dollars");
                    }
                    break;
                case 10 :
                    if (p.getDollars() >= 10){
                        p.setDollars(-10);
                        p.setRank(3);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Dollars");
                    }
                    break;
                case 18 :
                    if (p.getDollars() >= 18){
                        p.setDollars(-18);
                        p.setRank(4);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Dollars");
                    }
                    break;
                case 28 :
                    if (p.getDollars() >= 28){
                        p.setDollars(-28);
                        p.setRank(5);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Dollars");
                    }
                    break;
                case 40 :
                    if (p.getDollars() >= 40){
                        p.setDollars(-40);
                        p.setRank(6);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getDollars() + " dollars.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Dollars");
                    }
                    break;
                default :
                    JOptionPane.showMessageDialog(null,"Invalid dollar amount");
            }
        }

        public void upgrade_wCreds (Player p, int credits) {
            switch (credits) {
                case 5 :
                    if (p.getCredits() >= 5){
                        p.setCredits(-5);
                        p.setRank(2);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Credits");
                    }
                    break;

                case 10 :
                    if (p.getCredits() >= 10){
                        p.setCredits(-10);
                        p.setRank(3);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Credits");
                    }
                    break;

                case 15 :
                    if (p.getCredits() >= 15){
                        p.setCredits(-15);
                        p.setRank(4);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Credits");
                    }
                    break;

                case 20 :
                    if (p.getCredits() >= 20){
                        p.setCredits(-20);
                        p.setRank(5);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Credits");
                    }
                    break;

                case 25 :
                    if (p.getCredits() >= 25){
                        p.setCredits(-25);
                        p.setRank(6);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " is now rank " + p.getRank() + " and has " + p.getCredits() + " credits.");
                    } else {
                        JOptionPane.showMessageDialog(null,"Insufficient Credits");
                    }
                    break;

                default :
                    JOptionPane.showMessageDialog(null,"Invalid credit amount");
            }
        }
    }

/* Trailer class */
    public static class Trailer{
        public Trailer(){
        }
    }

/* Board class */
    public static class Board{
      public Board(){

      }
      public static void init(){
        populateAdjacencyList();
        populateExtrasList();
        populateActingList();

      }

      private static void populateAdjacencyList() {
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

      private static void populateExtrasList(){

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
          Extra train1 = new Extra(1,"Crusty Prospector");
          Extra train2 = new Extra(1,"Dragged by Train");
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
      private static void populateActingList(){
         ActingSet MainStreet = new ActingSet("Main Street",3,3,null,extrasList.get("Main Street"));
         ActingSet Saloon = new ActingSet("Saloon",2,2,null,extrasList.get("Saloon"));
         ActingSet Ranch = new ActingSet("Ranch",2,2,null,extrasList.get("Ranch"));
         ActingSet SecretHideout = new ActingSet("Secret Hideout",3,3,null,extrasList.get("Secret Hideout"));
         ActingSet Bank = new ActingSet("Bank",1,1,null,extrasList.get("Bank"));
         ActingSet Hotel = new ActingSet("Hotel",3,3,null,extrasList.get("Hotel"));
         ActingSet Church = new ActingSet("Church",2,2,null,extrasList.get("Church"));
         ActingSet Jail = new ActingSet("Jail",1,1,null,extrasList.get("Jail"));
         ActingSet TrainStation = new ActingSet("Train Station",3,3,null,extrasList.get("Train Station"));
         ActingSet GeneralStore = new ActingSet("General Store",2,2,null,extrasList.get("General Store"));

         actingSetList.put("Main Street", MainStreet);
         actingSetList.put("Saloon", Saloon);
         actingSetList.put("Ranch", Ranch);
         actingSetList.put("Secret Hideout", SecretHideout);
         actingSetList.put("Bank", Bank);
         actingSetList.put("Hotel", Hotel);
         actingSetList.put("Church", Church);
         actingSetList.put("Jail", Jail);
         actingSetList.put("Train Station", TrainStation);
         actingSetList.put("General Store", GeneralStore);
      }
    }





/* Main */
   public static void main(String[]arg) throws IOException{

        //initialization stuff:
       new deadwood();
       Board board = new Board();
       Scanner console = new Scanner(System.in);
       List<Scene> sceneList = populateSceneList();
       List<Player> playersList = new ArrayList<Player>();
       Day day;

       Dice officialDice = Dice.getDice();
       board.init();

       //get player input and intialize playersList
       int playernum = 0;
       while ((playernum < 2) || (playernum > 8)) {
            String numPlayers = JOptionPane.showInputDialog("Enter a number of players between 2 and 8: ");
            playernum = Integer.parseInt(numPlayers);
        }
        
        for (int i=0;i<playernum;i++) {
            if (playernum < 5) {
                Player playkid = new Player(officialDice, i+1);
                playersList.add(playkid);
                //playkid.drawPlayer(g);

            } else if (playernum == 5) {
                Player playkid = new Player(officialDice, i+1, 2);
                playersList.add(playkid);
                //playkid.drawPlayer(g);
            } else if (playernum == 6) {
                Player playkid = new Player(officialDice, i+1, 4);
                playersList.add(playkid);
                //playkid.drawPlayer(g);
            } else if (playernum > 6) {
                Player playkid = new Player(officialDice, i+1, 0, 2);
                playersList.add(playkid);
                //playkid.drawPlayer(g);
            }
        }

        //intialize Day:
        if (playernum < 4) {
            day = new Day(sceneList, playersList, 3);
            day.newDay();
        } else {
            day = new Day(sceneList, playersList);
            day.newDay();
        }


       //Extra l1 = new Extra(1, "Prospector");
       //Lead l2 = new Lead(1, "Miner", "Boom Pow");
       //List<Lead> leads = new ArrayList<Lead>();
       //List<Extra> extras = new ArrayList<Extra>();
       //leads.add(l2);
       //extras.add(l1);
       CastingOffice office = new CastingOffice(adjacencyList);
       //Scene curr = new Scene(4,3, "Wild West",20, leads);
       //ActingSet here = new ActingSet("Salooon", 3, 3, curr, extras, adjacencyList);
       //Player p1 = new Player(officialDice,1);
       //Player p2 = new Player(officialDice,2);
       //ActingSet []list = {here};
       Trailer trailer = new Trailer();
       //p1.setDollars(4);


       //Actual Gameplay:
        while(true){
            for (int i=0;i<playernum;i++) {
                turn(playersList.get(i), actingSetList, office, trailer);
                boolean dayDone = isDayDone();
                if(dayDone) {
                    boolean isGameDone = day.newDay();
                }
            }
        }

    }

    private static void isSceneDone(Player p){
        if(p.getLocal() != "Trailers" && p.getLocal() != "Casting Office"){
            if(p.getActingSet().getShotsLeft() == 0){
                activeScenes --;
            }
        }
    }
    private static boolean isDayDone(){
        if(activeScenes == 1){
            return true;
        }
        else{
            return false;
        }
    }
    private static void turn(Player p, Map<String,ActingSet> list, CastingOffice office, Trailer trailer){
        boolean stillATurn = true;
        Scanner console = new Scanner(System.in);
        while(stillATurn){
            String cmd = JOptionPane.showInputDialog("What is your command? : ");
            //String cmd = console.nextLine();
            CommandExec(p,cmd,list,office,trailer);
            if(cmd.equals("end")){
                stillATurn = false;
            }
        }
    }


    private static Lead findLead(String part, List<Lead> leadList){
        for(int i =0; i < leadList.size(); i ++){
            if(leadList.get(i).getName().equals(part)){
                return leadList.get(i);
            }
        }
        return null;
    }
    private static Extra findExtra(String part, List<Extra> extraList){
        for(int i =0; i < extraList.size(); i ++){
            if(extraList.get(i).getName().equals(part)){
                return extraList.get(i);
            }
        }
        return null;
    }


    private static List<Scene> populateSceneList() {

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

        List<Lead> sc1Leads = Arrays.asList(sc1L1, sc1L2, sc1L3);
        List<Lead> sc2Leads = Arrays.asList(sc2L1, sc2L2, sc2L3);
        List<Lead> sc3Leads = Arrays.asList(sc3L1, sc3L2, sc3L3);
        List<Lead> sc4Leads = Arrays.asList(sc4L1, sc4L2);
        List<Lead> sc5Leads = Arrays.asList(sc5L1, sc5L2);
        List<Lead> sc6Leads = Arrays.asList(sc6L1, sc6L2, sc6L3);
        List<Lead> sc7Leads = Arrays.asList(sc7L1, sc7L2, sc7L3);
        List<Lead> sc8Leads = Arrays.asList(sc8L1, sc8L2, sc8L3);
        List<Lead> sc9Leads = Arrays.asList(sc9L1, sc9L2, sc9L3);
        List<Lead> sc10Leads = Arrays.asList(sc10L1, sc10L2, sc10L3);
        List<Lead> sc11Leads = Arrays.asList(sc11L1, sc11L2, sc11L3);
        List<Lead> sc12Leads = Arrays.asList(sc12L1, sc12L2, sc12L3);
        List<Lead> sc13Leads = Arrays.asList(sc13L1);
        List<Lead> sc14Leads = Arrays.asList(sc14L1, sc14L2, sc14L3);
        List<Lead> sc15Leads = Arrays.asList(sc15L1, sc15L2);
        List<Lead> sc16Leads = Arrays.asList(sc16L1, sc16L2, sc16L3);
        List<Lead> sc17Leads = Arrays.asList(sc17L1, sc17L2);
        List<Lead> sc18Leads = Arrays.asList(sc18L1, sc18L2);
        List<Lead> sc19Leads = Arrays.asList(sc19L1, sc19L2);
        List<Lead> sc20Leads = Arrays.asList(sc20L1, sc20L2, sc20L3);
        List<Lead> sc21Leads = Arrays.asList(sc21L1, sc21L2, sc21L3);
        List<Lead> sc22Leads = Arrays.asList(sc22L1, sc22L2, sc22L3);
        List<Lead> sc23Leads = Arrays.asList(sc23L1, sc23L2, sc23L3);
        List<Lead> sc24Leads = Arrays.asList(sc24L1, sc24L2, sc24L3);
        List<Lead> sc25Leads = Arrays.asList(sc25L1, sc25L2);
        List<Lead> sc26Leads = Arrays.asList(sc26L1, sc26L2, sc26L3);
        List<Lead> sc27Leads = Arrays.asList(sc27L1, sc27L2);
        List<Lead> sc28Leads = Arrays.asList(sc28L1, sc28L2, sc28L3);
        List<Lead> sc29Leads = Arrays.asList(sc29L1, sc29L2, sc29L3);
        List<Lead> sc30Leads = Arrays.asList(sc30L1, sc30L2, sc30L3);
        List<Lead> sc31Leads = Arrays.asList(sc31L1, sc31L2);
        List<Lead> sc32Leads = Arrays.asList(sc32L1, sc32L2, sc32L3);
        List<Lead> sc33Leads = Arrays.asList(sc33L1, sc33L2, sc33L3);
        List<Lead> sc34Leads = Arrays.asList(sc34L1, sc34L2, sc34L3);
        List<Lead> sc35Leads = Arrays.asList(sc35L1, sc35L2, sc35L3);
        List<Lead> sc36Leads = Arrays.asList(sc36L1, sc36L2, sc36L3);
        List<Lead> sc37Leads = Arrays.asList(sc37L1, sc37L2, sc37L3);
        List<Lead> sc38Leads = Arrays.asList(sc38L1, sc38L2);
        List<Lead> sc39Leads = Arrays.asList(sc39L1, sc39L2, sc39L3);
        List<Lead> sc40Leads = Arrays.asList(sc40L1, sc40L2, sc40L3);

        Scene scene1 = new Scene(4,3,"Evil Wears a Hat",7,sc1Leads);
        Scene scene2 = new Scene(6,3,"Square Deal City",14,sc2Leads);
        Scene scene3 = new Scene(3,3,"Law and the Old West",20,sc3Leads);
        Scene scene4 = new Scene(4,2,"Davy Crockett: A Drunkard's Tale",31,sc4Leads);
        Scene scene5 = new Scene(5,2,"The Life and Times of John Skywater",22,sc5Leads);
        Scene scene6 = new Scene(4,3,"The Way the West was Run",34,sc6Leads);
        Scene scene7 = new Scene(5,3,"My Years on the Prairie",32,sc7Leads);
        Scene scene8 = new Scene(3,3,"Down in the Valley",24,sc8Leads);
        Scene scene9 = new Scene(4,3,"Buffalo Bill: The Lost Years",12,sc9Leads);
        Scene scene10 = new Scene(4,3,"Ol' Shooter and Little Doll",14,sc10Leads);
        Scene scene11 = new Scene(4,3,"The Robber of Trains",19,sc11Leads);
        Scene scene12 = new Scene(5,3,"Jesse James: Man of Action",8,sc12Leads);
        Scene scene13 = new Scene(2,1,"Beyond the Pail: Life without Lactose",12,sc13Leads);
        Scene scene14 = new Scene(5,3,"Disaster at Flying J",6,sc14Leads);
        Scene scene15 = new Scene(3,2,"A Man Called Cow",16,sc15Leads);
        Scene scene16 = new Scene(3,3,"Shakespeare in Lubbock",23,sc16Leads);
        Scene scene17 = new Scene(2,2,"Taffy Commercial",2,sc17Leads);
        Scene scene18 = new Scene(3,2,"Go West, You!",30,sc18Leads);
        Scene scene19 = new Scene(2,2,"Gum Commercial",3,sc19Leads);
        Scene scene20 = new Scene(5,3,"The Life and Times of John Skywater",15,sc20Leads);
        Scene scene21 = new Scene(6,3,"Gun! The Musical",25,sc21Leads);
        Scene scene22 = new Scene(6,3,"One false Step for Mankind",21,sc22Leads);
        Scene scene23 = new Scene(5,3,"Humor at the Expense of Others",16,sc23Leads);
        Scene scene24 = new Scene(5,3,"Thirteen the Hard Way",15,sc24Leads);
        Scene scene25 = new Scene(6,2,"The Search for Maggie White",12,sc25Leads);
        Scene scene26 = new Scene(4,3,"How They Get Milk",2,sc26Leads);
        Scene scene27 = new Scene(2,2,"Picante Sauce Commercial",1,sc27Leads);
        Scene scene28 = new Scene(5,3,"My Years on the Prairie",27,sc28Leads);
        Scene scene29 = new Scene(5,3,"Jesse James: Man of Action",14,sc29Leads);
        Scene scene30 = new Scene(4,3,"Davy Crockett: A Drunkard's Tale",12,sc30Leads);
        Scene scene31 = new Scene(4,2,"Czechs in the Sonora",25,sc31Leads);
        Scene scene32 = new Scene(4,3,"J. Robert Lucky, Man of Substance",13,sc32Leads);
        Scene scene33 = new Scene(6,3,"Swing 'em Wide'",19,sc33Leads);
        Scene scene34 = new Scene(5,3,"Thirteen the Hard Way",17,sc34Leads);
        Scene scene35 = new Scene(6,3,"Swing 'em Wide'",35,sc35Leads);
        Scene scene36 = new Scene(4,3,"How They Get Milk",8,sc36Leads);
        Scene scene37 = new Scene(4,3,"Trials of te First Pioneers",5,sc37Leads);
        Scene scene38 = new Scene(3,2,"Breakin' in Trick Ponies",19,sc38Leads);
        Scene scene39 = new Scene(5,3,"How the Grinch Stole Texas",9,sc39Leads);
        Scene scene40 = new Scene(5,3,"Custer's Other Stands",40,sc40Leads);

        List<Scene> sceneList = new ArrayList<Scene>();

        sceneList.add(scene1);
        sceneList.add(scene2);
        sceneList.add(scene3);
        sceneList.add(scene4);
        sceneList.add(scene5);
        sceneList.add(scene6);
        sceneList.add(scene7);
        sceneList.add(scene8);
        sceneList.add(scene9);
        sceneList.add(scene10);
        sceneList.add(scene11);
        sceneList.add(scene12);
        sceneList.add(scene13);
        sceneList.add(scene14);
        sceneList.add(scene15);
        sceneList.add(scene16);
        sceneList.add(scene17);
        sceneList.add(scene18);
        sceneList.add(scene19);
        sceneList.add(scene20);
        sceneList.add(scene21);
        sceneList.add(scene22);
        sceneList.add(scene23);
        sceneList.add(scene24);
        sceneList.add(scene25);
        sceneList.add(scene26);
        sceneList.add(scene27);
        sceneList.add(scene28);
        sceneList.add(scene29);
        sceneList.add(scene30);
        sceneList.add(scene31);
        sceneList.add(scene32);
        sceneList.add(scene33);
        sceneList.add(scene34);
        sceneList.add(scene35);
        sceneList.add(scene36);
        sceneList.add(scene37);
        sceneList.add(scene38);
        sceneList.add(scene39);
        sceneList.add(scene40);

        return sceneList;
    }


/* Commands Method */
    private static void CommandExec(Player p, String cmd, Map<String,ActingSet> list, CastingOffice office, Trailer trailer){
        if(cmd.equals("who")){
            JOptionPane.showMessageDialog(null,"\nPlayer " + p.getId() + " has $" + p.getDollars() + " and " + p.getCredits() + " credits ");
            String name = p.getLeadRole();
            String name2 = p.getExtraRole();
            if(name != null){
                JOptionPane.showMessageDialog(null,"and is working on " + name + ".\n");
            }
            else if(name2 != null){
                JOptionPane.showMessageDialog(null,"and is working on " + name2 + ".\n");
            }
            else{
                JOptionPane.showMessageDialog(null,"and is currently not working on a role.\n");
            }
        }
        else if(cmd.length() == 0){
            return;
        }
        else if(cmd.equals("Where")){
            JOptionPane.showMessageDialog(null,"You are in the " + p.getLocal());
            if(p.getActingSet() != null){
                JOptionPane.showMessageDialog(null," where " + p.getActingSet().getScene().getName() + ", " + "scene " + p.getActingSet().getScene().getId() +" is shooting.\n");
                for(int i = 0; i < p.getActingSet().getScene().getLeadList().size(); i ++){
                    JOptionPane.showMessageDialog(null, "Lead Role #" + (i + 1) + " is " + p.getActingSet().getScene().getLeadList().get(i).getName() + " and requires level " + p.getActingSet().getScene().getLeadList().get(i).getRank());
                }
                for(int i = 0; i < extrasList.get(p.getLocal()).size(); i ++){
                    JOptionPane.showMessageDialog(null, "Extra Role #" + (i + 1) + " is " + extrasList.get(p.getLocal()).get(i).getName() + " and the level requirement is " + extrasList.get(p.getLocal()).get(i).getRank());
                }
            }
            else if(p.getLocal().equals("Casting Office")){
                JOptionPane.showMessageDialog(null," where there is no scene that is ever worked on.\n");
            }
            else if(p.getLocal().equals("Trailers")){
                JOptionPane.showMessageDialog(null," where there is no scene ever shot.\n");
            }
            else{
                JOptionPane.showMessageDialog(null," and you are not currently shooting a scene.\n");
            }
        }
        else if(cmd.equals("Rehearse")){
             if( p.getLeadRole() != null || p.getExtraRole() != null){
                if(p.haveActed == false){
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " has rehearsed this turn.\n");
                }
                p.rehearse();
            }
            else{
                JOptionPane.showMessageDialog(null, "You aren't in a role, so you cannot rehearse.");
            }
        }
        else if(cmd.equals("Act")){
            if(p.getLeadRole() != null || p.getExtraRole() != null){
                Lead LeadRole = findLead(p.getLeadRole(), p.getActingSet().getScene().getLeadList());
                Extra ExtraRole = findExtra(p.getExtraRole(), p.getActingSet().getExtrasList());
                if(LeadRole != null){
                    p.act();
                    LeadRole.reward(p);
                    JOptionPane.showMessageDialog(null,"Player " + p.getId() + " now has $" + p.getDollars() + " and " + p.getCredits() + " credits.\n");
                    p.setActSuccesful();
                }
                else if(ExtraRole != null){
                    p.act();
                    ExtraRole.reward(p);
                    JOptionPane.showMessageDialog(null,"Player " + p.getId() + " now has $" + p.getDollars() + " and " + p.getCredits() + " credits.\n");
                    p.setActSuccesful();
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "You aren't in a role, so you cannot act.");
            }
        }
         else if(cmd.equals("end")){
            JOptionPane.showMessageDialog(null,"Player " + p.getId() + "'s turn is over... Please pass the computer to the next player.\n");
            isSceneDone(p);
            p.resetTurn();
        }
        else if((cmd.length() > 4) && (cmd.substring(0,4).equals("move"))){
            ActingSet room = list.get(cmd.substring(5));
            if(room == null && (!(cmd.substring(5).equals( "Casting Office")) && !(cmd.substring(5).equals("Trailers")))){
                // if(room == null){
                //     JOptionPane.showMessageDialog(list.get(cmd.substring(5)));
                //     JOptionPane.showMessageDialog(cmd.substring(5));
                // }
                JOptionPane.showMessageDialog(null,"The room you tried to move to was an invalid room");
                return;
            }
            else{
                if(room == null){
                    if(cmd.substring(5).equals("Casting Office")){
                        if(adjacencyList.get(p.getLocal()).contains("Casting Office")){
                            p.move(office);
                            JOptionPane.showMessageDialog(null,"Player " + p.getId() + " has moved to room " + p.getLocal() + ".\n");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "That room is not adjacent");
                        }
                    }
                    else{
                        if(adjacencyList.get(p.getLocal()).contains("Trailers")){
                            p.move(trailer);
                            JOptionPane.showMessageDialog(null,"Player " + p.getId() + " has moved to room " + p.getLocal() + ".\n");
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "That room is not adjacent");
                        }
                    }
                }
                else{
                    if(adjacencyList.get(p.getLocal()).contains(room.getName())){
                        p.move(room);
                        JOptionPane.showMessageDialog(null,"Player " + p.getId() + " has moved to room " + p.getActingSet().getName() + ".\n");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "That room is not adjacent");
                    }
                }
            }
        }
        else if((cmd.length() > 4) && (cmd.substring(0,4).equals("work"))){
            Lead isLead = null;
            Extra isExtra = null;
            if(p.getActingSet() != null){
                isLead = findLead(cmd.substring(5),p.getActingSet().getScene().getLeadList());
                isExtra = findExtra(cmd.substring(5),extrasList.get(p.getActingSet().getName()));
            }
            if(isLead == null){
                if(isExtra == null){
                    //JOptionPane.showMessageDialog(extrasList.get(p.getActingSet().getName()).get(0).getName());
                    JOptionPane.showMessageDialog(null,"I'm sorry but that role doesn't exist...\n");
//                     JOptionPane.showMessageDialog(null,p.getActingSet().getScene().getName());
//                     JOptionPane.showMessageDialog(null,p.getActingSet().getScene().getLeadList().get(0).getName());
                    return;
                }
                p.takeExtraRole(isExtra);
//                 if(p.getExtraRole() == null){
//                     return;
//                 }
                JOptionPane.showMessageDialog(null,"Player " + p.getId() + " has taken the Extra role of " + p.getExtraRole() + ".\n");
            }
            else{
                p.takeLeadRole(isLead);
//                 if(p.getLeadRole() == null){
//                     return;
//                 }
                JOptionPane.showMessageDialog(null,"Player " + p.getId() + " has taken the Lead role of " + p.getLeadRole() + ".\n");
            }
        }
        else if((cmd.length() > 7) && (cmd.substring(0,7).equals("upgrade"))){
            if((cmd.length() > 9) && (cmd.substring(8,9).equals("$"))){
                if(p.getLocal().equals("Casting Office")){
                    int dollars = Integer.parseInt(cmd.substring(10));
                    office.upgrade_wDollars(p,dollars);
                }
            }
            else if((cmd.length() > 10) && (cmd.substring(8,10).equals("cr"))){
                if(p.getLocal().equals("Casting Office")){
                    int credits = Integer.parseInt(cmd.substring(11));
                    office.upgrade_wCreds(p,credits);
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null,"That was not a valid command. Please read the README with any questions\n");
            return;
        }


    }

 }
