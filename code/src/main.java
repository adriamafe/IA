import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Calendar;
import java.util.Scanner;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
public class main {
	public static void main(String[] args){
		
		//////Initialization/////
		
		Search search = null;
		Problem problem = null;
		int npaq; // number of packets
		int seed; // seed for aleatory generation
		double prop; // proportion
		int HS; //Hill climbing == 1, Simulated = 0;
		int op; //operators to use
		int initial_solution;// initial solution to use
		try {
			PrintWriter writer;
			writer = new PrintWriter("initial_sol_"+3+".txt", "UTF-8");
			writer.println("seed\tinitial_sol\toperators\tinitial_packRests\tinitial_cost\tfinal_cost\tfinal_packRests\tex_time");
		for(int i = 0; i < 10; ++i){
		System.out.println("Number of packets:");
		Scanner in = new Scanner(System.in);
		npaq = in.nextInt();
		System.out.println("Seed:");
		seed = in.nextInt();
		Paquetes paq = new Paquetes(npaq, seed);
		System.out.println("Packets generated");
		System.out.println("Proporcion:");
		prop = in.nextDouble();
		System.out.println("Seed:");
		seed = in.nextInt();
	    Transporte trans = new Transporte(paq, prop, seed);
	    System.out.println("Transport generated");
		System.out.println("What initial solution do you want to use?"
				+ "\n1: aleatory assignment\n2: ordering by priority and days\n3: ordering by price\n4: ordering by weight and peso maximo");
		initial_solution = in.nextInt();
		System.out.println("What group of operators do you want to use?\n"
				+ "1 : assign\n2 : swap\n3 : assign+ swap");
		op = in.nextInt();
		/*////Use it for experiment 3
		System.out.println("Do you want to use Hill Climbing (1) or Simulated annealing(2)?");
		HS = in.nextInt();
		*/
			BLState S = new BLState(paq, trans,initial_solution);
			System.out.println(S.get_packRests());
			if(op == 1) problem = new Problem(S, new BLSuccessorFunction1(), new BLGoalTest(),new BLHeuristicFunction1());
			if(op == 2) problem = new Problem(S, new BLSuccessorFunction2(), new BLGoalTest(),new BLHeuristicFunction1());
			if(op == 3) problem = new Problem(S, new BLSuccessorFunction3(), new BLGoalTest(),new BLHeuristicFunction1());
			search = new HillClimbingSearch();
			try {
				long startTime = System.currentTimeMillis();
				SearchAgent agent = new SearchAgent(problem,search);
				long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;
				BLState goal = (BLState) search.getGoalState();
				System.out.println(goal.get_costes());
				System.out.println(goal.get_packRests());
				System.out.println(elapsedTime);
				
				writer.println(seed+"\t"+initial_solution+"\t"+op+"\t"+S.get_packRests()+"\t"+S.get_costes()+"\t"+goal.get_costes()+"\t"+ goal.get_packRests()+"\t"+elapsedTime);
			} catch (Exception e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		
		
	}
	}
}
