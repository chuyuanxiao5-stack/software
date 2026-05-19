package ex1c;

import java.util.*;
/**
 * MisCan Problem (DFS / BFS)
 * 宣教師と人食い問題の探索プログラム
 */
public class MisCanProblem {
    public static void main(String[] args) {
	System.out.println("DFS（深さ優先探索）開始");
	var solverDFS = new SolverDFS();
	//左岸に宣教師3人、人食い3人、船あり
	solverDFS.solve(new MisCanWorld(3, 3, 1));

	System.out.println("BFS（幅優先探索）開始");
	var solverBFS = new SolverBFS();
	solverBFS.solve(new MisCanWorld(3,1,1));
	
    }
}

 class MisCanAction implements Action {
     int missionary;//船で移動する宣教師の人数
     int cannibal;//船で移動する人食いの人数
     int boat;//船の移動方向(-1が右岸、+1が左岸へ)
     
     static List<Action> all = List.of(
				       new MisCanAction(-2, 0, -1),
				       new MisCanAction(-1, -1, -1),
				       new MisCanAction(0, -2, -1),
				       new MisCanAction(-1, 0, -1),
				       new MisCanAction(0, -1, -1),
				       new MisCanAction(+2, 0, +1),
				       new MisCanAction(+1, +1, +1),
				       new MisCanAction(0, +2, +1),
				       new MisCanAction(+1, 0, +1),
				       new MisCanAction(0, +1, +1));
     
     MisCanAction(int missionary, int cannibal, int boat) {
	 this.missionary = missionary;
	 this.cannibal = cannibal;
	 this.boat = boat;
     }
     
     public String toString() {
	 var dir = this.boat < 0 ? "right" : "left ";
	 var m = Math.abs(this.missionary);
	 var c = Math.abs(this.cannibal);
	 return String.format("move (%d, %d) to %s", m, c, dir);
     }
 }

class MisCanWorld implements World {
    int missionary;
    int cannibal;
    int boat;
    
    public MisCanWorld(int missionary, int cannibal, int boat) {
	this.missionary = missionary;
	
	this.cannibal = cannibal;
	this.boat = boat;
    }
    
    public MisCanWorld clone() {
	return new MisCanWorld(this.missionary, this.cannibal, this.boat);
    }

    //状態チェック
    public boolean isValid() {
	if (this.missionary < 0 || this.missionary > 3)
	    return false;
	if (this.cannibal < 0 || this.cannibal > 3)
	    return false;
	if (this.boat < 0 || this.boat > 1)
	    return false;
	if (this.missionary > 0 && this.missionary < this.cannibal)
	    return false;
	if ((3 - this.missionary) > 0 && (3 - this.missionary) < (3 - this.cannibal))
	    return false;
	return true;
    }
    
    public boolean isGoal() {
	return this.missionary == 0 && this.cannibal == 0;
    }
    //行動返却
    public List<Action> actions() {
	return MisCanAction.all;
    }
    //次状態
    public World successor(Action action) {
	var a = (MisCanAction) action;
	var next = clone();
	next.missionary += a.missionary;
	next.cannibal += a.cannibal;
	next.boat += a.boat;
	return next;
    }
    
    public String toString() {
	return String.format("(%d, %d, %d)", this.missionary, this.cannibal, this.boat);
    }
}

