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

    public boolean act(){
      int getRoll = dice.roll();
      int budget = scene.getBudget();   //Identify which scene based on the players position
      if(getRoll > budget - rehearseCount){
        return true;
      }
      else{
        return false;
      }
    }
    public void takeRole(Role roll){
      if(role == false && rank >= roll.rankReq){           
        role = true;
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

    public Lead(int val) {
      int rankReq = val;
    }
    //provide credits reward on success:
    //shotsLeft for ActingSet is also decremented.
    public void reward(Player p) {
      //reward on success only:
      if (p.act() == true)
        p.setCredits(p.getCredits() + 2);
      //ActingSet.setShotsLeft(ActingSet.getShotsLeft() - 1);
    }
  }
  public static class Extra extends Role{
    // provide money and credit reward on Success, money only on failure:
    //shotsLeft for ActingSet is also decremented.
    public Extra(int val) {
      int rankReq = val;
    }

    public void reward(Player p) {
      //reward on success:
      if (p.act() == true) {
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
       Scene curr = new Scene(9,3);
       Player p1 = new Player(officialDice);
       p1.setCredits(4);
       Player p2 = new Player(officialDice);
       System.out.println(p1.getCredits());
       System.out.println(p2.getCredits());
   }
 }
