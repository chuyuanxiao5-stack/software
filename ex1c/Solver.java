package ex1c;

import java.util.*;
import java.util.stream.*;

//Worldに依存せず探索を行うインターフェース
interface World extends Cloneable {
    boolean isValid();//状態が有効か
    boolean isGoal();//ゴール判定
    List<Action> actions();//可能な行動の列挙
    World successor(Action action);//行動による次状態の生成
}

//行動の抽象
interface Action {
}

//探索木ノード
class State {
    State parent;//親ノード
    Action action;//親から遷移する際の行動
    World world;//このノードが表す状態
    
    State(State parent, Action action, World child) {
	this.action = action;
	this.parent = parent;
	this.world = child;
    }
    
    public boolean isGoal() {
	return this.world.isGoal();
    }
    //子ノードを生成
    List<State> children() {
	return this.world.actions().stream()
	    .map(a -> new State(this, a, this.world.successor(a)))
	    .filter(s -> s.world.isValid())
	    .toList();
    }
    
    public String toString() {
	if (this.action != null) {
	    return String.format("%s (%s)", this.world, this.action);
	} else {
	    return this.world.toString();
	}
    }
}

//縦型探索を行うクラス
class SolverDFS{
    public void solve(World world) {
	var root = new State(null, null, world);
	var goal = search(root);
	
	if (goal != null)
	    printSolution(goal);
    }

    //探索メソッド
    State search(State root) {
	var openList = toMutable(List.of(root));
	Set<String> visitedStates = new HashSet<>(); //訪問済みリスト
	int visited = 0;//訪問ノード数
	int maxOpen = 0;//リストの最大長
	long start = System.currentTimeMillis();//実行時間
	
	while (openList.isEmpty() == false) {
	    if (openList.size() > maxOpen) maxOpen = openList.size();
	    var state = get(openList);
	    visited++;
	    
	    if (state.isGoal()){
		long end = System.currentTimeMillis();
		System.out.println("Visited nodes: " + visited);
		System.out.println("Max open list size: " + maxOpen);
		System.out.println("Execution time (ms): " + (end - start));
		return state;
	    }
	    
	    var children = state.children();
	    openList = concat(openList, children);
	}
	
	return null;
    }

    //状態を取り出す
    State get(List<State> list) {
	return list.remove(list.size() - 1);
    }

    //2つのリストを結合
 List<State> concat(List<State> xs, List<State> ys) {
     return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
 }

    //不変リストを可変リストに変換
    List<State> toMutable(List<State> list) {
	return new ArrayList<>(list);
    }

    //ゴールまでの経路を出力
    void printSolution(State goal) {
	var list = new ArrayList<State>();
	
	while (goal != null) {
	    list.add(0, goal);
	    goal = goal.parent;
	}
	
	for(int i = 0;i < list.size();i++){
	    System.out.println("Step"+i+":"+list.get(i));
	}
	System.out.println("Goal reached!");
	System.out.println("Total steps: " + (list.size() - 1));
    }
}

//横型探索を行うクラス
class SolverBFS {
    public void solve(World world) {
	var root = new State(null, null, world);
	var goal = search(root);
	
	if (goal != null)
	    printSolution(goal);
    }
    
    //探索メソッド
    State search(State root) {
	var openList = toMutable(List.of(root));
	int visited = 0;//訪問ノード数
	int maxOpen = 0;//リストの最大長
	long start = System.currentTimeMillis();//実行時間
	
	while (openList.isEmpty() == false) {
	    if (openList.size() > maxOpen) maxOpen = openList.size();
	    var state = get(openList);
	    visited++;
	    
	    if (state.isGoal()){
		long end = System.currentTimeMillis();
		System.out.println("Visited nodes: " + visited);
		System.out.println("Max open list size: " + maxOpen);
		System.out.println("Execution time (ms): " + (end - start));
		return state;
	    }
	    
	    var children = state.children();
	    openList = concat(openList, children);
	}

	return null;
    }
    
    //状態を取り出す
    State get(List<State> list) {
	return list.remove(0);//先頭から取り出す
    }
    
    //2つのリストを結合
    List<State> concat(List<State> xs, List<State> ys) {
	return toMutable(Stream.concat(xs.stream(), ys.stream()).toList());
    }
    
    //不変リストを可変リストに変換
    List<State> toMutable(List<State> list) {
	return new ArrayList<>(list);
    }
    
    //ゴールまでの経路を出力
    void printSolution(State goal) {
	var list = new ArrayList<State>();
	
	while (goal != null) {
	    list.add(0, goal);
	    goal = goal.parent;
	}
	
	for(int i = 0;i < list.size();i++){
	    System.out.println("Step"+i+":"+list.get(i));
	}
	System.out.println("Goal reached!");
	System.out.println("Total steps: " + (list.size() - 1));
    }
}

