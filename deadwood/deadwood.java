/* Marshall Tanis, Matt Watkins, Elijah Baldwin
 *
 * Deadwood
 *
 * Modified 2/19/2016
 */

import java.util.*;
import java.lang.*;

 
 public class deadwood{

  public class Player{
    private int dollars;
    private int credits;
    private int rehearseCount;
    private int rank;
    private int score;
    private boolean role;
    //private Location local;
   // public Player(){

    //}
}
//     public boolean act(){
//       int getRoll = Dice.roll();
//       int budget = Scene.getBudget();   //Identify which scene based on the players position
//       if(getRoll > budget - rehearseCount){
//         return true;
//       }
//       else{
//         return false;
//       }
//     }
   /* public void takeRole(){
      if(role == false && rank >= Role.rankReq){           // Specific Role from get statement in Role Class, reinitialize rehearsal count to zero
        role = true;
      }
      else{
        System.out.print("Invalid Action");
      }
    }*/
   /* public void move(){
      /* If location is in adjacent set, move, else choose a different room */
    /*}
    public void rehearse(){
      /*add 1 to rehearseCount */
    //}

  public abstract class Role{
    private int rankReq;

    public abstract void reward() {
      /* provide money and credit reward for extras, just credit reward for leads on success.
      Also on success, shotsLeft for ActingSet is decremented.
      on failure, money for extras, nothing for leads. */
    }

  }
  public class Lead extends Role{

    public Lead(int val) {
      rankReq = val;
    }

    public void reward() {
      //reward on success only:s
      if (Player.act() == true)
        Player.setCredits(Player.getCredits + 2);
    }
    /*see Abstract class Role */
  }
  public class Extra extends Role{

    public Extra(int val) {
      rankReq = val;
    }

    public void reward() {
      //reward on success:
      if (Player.act() == true) {
        Player.setCredits(Player.getCredits + 1);
        Player.setDollars(Player.getDollars + 1);
      //reward on failure:
      } else {
        Player.setDollars(Player.getDollars + 1);
      }

    }
    /*see Abstract class Role */
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
//   public abstract class Role{
//     private int rankReq;
// 
//     public abstract int reward() {
//       /* provide money and credit reward for extras, just credit reward for leads on success.
//       Also on success, shotsLeft for ActingSet is decremented.
//       on failure, money for extras, nothing for leads. */
//       return 0;
//     }
// 
//   }
//   public class Lead extends Role{
//     /*see Abstract class Role */
//   }
//   public class Extra extends Role{
//     /*see Abstract class Role */
//   }
//   public class Scene implements Day{
//     private int budget;
// 
//   }
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
   }
 }
