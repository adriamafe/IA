import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Experiment {
public static void main(String[] args) throws Exception_Sol{
		
		//////Initialization/////
		int seeds[] = {1234,111,2222,333,444,555,666,777,888,999};
		Search search = null;
		Problem problem = null;
		int npaq; // number of packets
		double prop; // proportion
		int HS; //Hill climbing == 1, Simulated = 0;
		int op; //operators to use
		int initial_solution;// initial solution to use
		Scanner in = new Scanner(System.in);
		System.out.println("Name of the experiment(no spaces:");
		String name = in.next();
		try {
		PrintWriter writer;
		writer = new PrintWriter(name+".txt", "UTF-8");
		writer.println("Initial_sol\toperators\tinitial_cost\tfinal_cost\texecution_time" );
		System.out.println("Number of packets:");
		npaq = in.nextInt();
		System.out.println("Proporcion:");
		prop = in.nextDouble();
		System.out.println("What initial solution do you want to use?"
				+ "\n1: aleatory assignment\n2: ordering by priority and days\n3: ordering by price\n4: ordering also by prio and weight");
		initial_solution = in.nextInt();
		System.out.println("What group of operators do you want to use?\n"
				+ "1 : assign\n2 : swap\n3 : assign+ swap");
		op = in.nextInt();
		System.out.println("Hill Climbing or Simulated Anealling?? 0 for HillClimbing, 1 for SImulated Anealling");
		int x = in.nextInt();
		/*////Use it for experiment 3
		System.out.println("Do you want to use Hill Climbing (1) or Simulated annealing(2)?");
		HS = in.nextInt();
		*/for(int i = 0; i < 10; ++i){
			Paquetes paq = new Paquetes(npaq, seeds[i]);
			Transporte trans = new Transporte(paq, prop, seeds[i]);
			BLState S = new BLState(paq, trans,initial_solution);
			int rest = S.get_packRests();
			if(rest>0) throw new Exception_Sol("initial_sol not correct!!");
			if(op == 1) problem = new Problem(S, new BLSuccessorFunction1(), new BLGoalTest(),new BLHeuristicFunction1());
			if(op == 2) problem = new Problem(S, new BLSuccessorFunction2(), new BLGoalTest(),new BLHeuristicFunction1());
			if(op == 3) problem = new Problem(S, new BLSuccessorFunction3(), new BLGoalTest(),new BLHeuristicFunction1());
			
			if(x == 0)search = new HillClimbingSearch();
			else search = new SimulatedAnnealingSearch();
			try {
				long startTime = System.currentTimeMillis();
				SearchAgent agent = new SearchAgent(problem,search);
				long stopTime = System.currentTimeMillis();
				long elapsedTime = stopTime - startTime;
				BLState goal = (BLState) search.getGoalState();
				System.out.println(goal.get_costes());
				System.out.println(goal.get_packRests());
				System.out.println(elapsedTime);
				writer.println(initial_solution+"\t"+op+"\t"+S.get_costes()+"\t"+goal.get_costes()+"\t"+ goal.get_packRests()+"\t"+elapsedTime);
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
