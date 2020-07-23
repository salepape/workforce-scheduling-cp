package vrp;

import java.util.Date;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.SetVar;
import org.chocosolver.util.ESat;

public class Vrp 
{
    public static void main(String[] args) 
    {      
        // PARAMETERS of the problem
           
        // Number of customers (= vertices) whose fictive deposit is the customer 0
        int N = 10;
        // Number of vehicles
        int V = 4;
        // Total number of visits (vertex 0 is the deposit (erased) and we create for each vehicle 2 deposits (departure and arrival))
        int nb_total_visite = N+2*V;
        
        // DATA of the problem
            
        // Superior bound of the distance
        int H = 100;
        // Distance matrix
        int[][] T; 
        // List of requests (Demandes)
        int[] D;    
        // List of Capacities
        int[] C; 
        // Distance matrix based on beginning-end routings
        int[][] T_prime;
                  
        T = new int[][]
        {
            {0, 1, 3, 5, 1, 4, 4, 3, 4, 3},
            {1, 0, 4, 4, 3, 4, 3, 1, 2, 1},
            {3, 4, 0, 4, 1, 1, 3, 1, 1, 1},
            {5, 4, 4, 0, 3, 4, 9, 3, 1, 2},
            {1, 3, 1, 3, 0, 2, 2, 1, 3, 5},
            {4, 4, 1, 4, 2, 0, 4, 5, 1, 2},
            {4, 3, 3, 9, 2, 4, 0, 2, 1, 1},
            {3, 1, 1, 3, 1, 5, 2, 0, 2, 1},
            {4, 2, 1, 1, 3, 1, 1, 2, 0, 1},
            {3, 1, 1, 2, 4, 2, 1, 1, 1, 0}       
        };
        
        D = new int[nb_total_visite];
        D[0] = 0; D[1] = 1; D[2] = 3; D[3] = 5; D[4] = 1;
        D[5] = 4; D[6] = 4; D[7] = 3; D[8] = 4; D[9] = 3;
        for (int i = N; i < nb_total_visite; i++)
        {
            D[i] = 0; 
        }
             
        C = new int[V+1];
        C[0] = 0; C[1] = 100; C[2] = 100; C[3] = 100; C[4] = 100;
             
        T_prime = new int[nb_total_visite][nb_total_visite]; 
        
        // Add columns at the end of matrix T (for arrival deposits)
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                T_prime[i][j] = T[i][j];
            }
            
            for (int j = 0; j < V*2; j++)
            {
                int position = N+j;
                T_prime[i][position] = T[i][0];
            }
        }
       
        // Add lines at the end of matrix T (for departure deposit)
        for (int i = N; i < nb_total_visite; i++)
        {
            for (int j = 0; j < V*2; j++)
            {
                int position = N+j;
                T_prime[i][position] = 0;
            }
            for (int j = 0; j < N; j++)
            {
                T_prime[i][j] = T[0][j];
            }
        }
        
        
            
        // Definition of the model by creating an instance of the class Model
	Model mon_modele = new Model("VRP en PPC");
            
        
        
        // DECISION VARIABLES of the problem
        
        // Successor of each visit
        IntVar[] s;             
	s = new IntVar[nb_total_visite];
        // Vehicle (or road ?) which is affected to the visit
        IntVar[] a;             
        a = new IntVar[nb_total_visite];
        // Rank of the visit for the travel (similar to the TSP)
        IntVar[] p;             
        p = new IntVar[nb_total_visite];
        
        
        
        // CONSTRAINTS OF THE PPC
        
        // Definition domain of s (constraints 1 and 2)     
        s[0] = mon_modele.intVar("succ_0", -1);     // Node 0 doesn't count
	for (int i = 1; i < nb_total_visite; i++)
        {
            if (i < N+V)
            {        
                // Concern the customers and departure deposits of every vehicles
                s[i] = mon_modele.intVar("succ_"+i, 1, nb_total_visite);
            }
            else
            {
                // Successors of arrival deposits are null because they cannot exist
                s[i] = mon_modele.intVar("succ_"+i, 0);  
            }
	}
       
        // Definition domain of a (constraint 3)
        a[0] = mon_modele.intVar("s_0", 0);         // Node index 0 is fictive (otherwise, if we content with index 1 to N, we cannot use global constraints)
        for (int i = 1; i < nb_total_visite; i++)
        {
            a[i] = mon_modele.intVar("s_"+i, 1, V); 
	}
        
        // Constraint 4 : case of departure and arrival deposits      
        for (int i = 1; i <= V; i++)
        {
            // departure deposit explicitely affected to the vehicle i
            int position = N+i-1;  
            a[position] = mon_modele.intVar("route_"+position, i);
            
            // arrival deposit explicitely affected to the vehicle i
            position = N+i+V-1;     
            a[position] = mon_modele.intVar("route_"+position, i);
        }
        
        // Definition domain of p (constraint 5)
        p[0] = mon_modele.intVar("rang_0", -1);     // Node 0 is fictive
	for (int i = 1; i < nb_total_visite; i++)
        {
            p[i] = mon_modele.intVar("rang_"+i, 1, N);
        }
        
        // Constraint 6 : case of departure and arrival deposits (always in rank 0)
        for (int i = 0; i < V; i++)
        {
            int position = N+i;
            p[position] = mon_modele.intVar("rang_"+position, 0); 
        }
              
        // Constraint 7 : all successors must be different (null values are not considered by the Choco constraint)
        mon_modele.allDifferentExcept0(s).post();
                        
        // Constraint 8 : vertices i are affected to the same vehicle as their respective successors a[i] (except the arrival deposits)
        for (int i = 1; i < N+V; i++)
        {
            mon_modele.element(a[i], a, s[i], 0).post();
        }
                                  
        // Constraint 9 : avoid to obtain that a successor of a vertex is itself = subtour of size 1 (optionnal -> reinforcement)
        for (int i = 1; i < N+V; i++)
        {
            mon_modele.arithm(s[i], "!=", i).post();
        }
         
        // Constraint 10 : suppress every subtour
        for (int i = 1; i < N+V; i++)
        {
            IntVar t = mon_modele.intVar(0, nb_total_visite); 
            mon_modele.arithm(t, "=", p[i], "+", 1).post();
            mon_modele.element(t, p, s[i], 0).post();
        }
        
        // Constraint 11 : Consitution of customers' partition into subsets affected to a vehicle       
        // Set of customers served by the current vehicle (SetVar adapted : look for the difference between other variable types)
        SetVar[] b;
        b = new SetVar[V+1];
        // Values that can belong to b set
        int[] x_UB = new int[nb_total_visite];   
        for (int i = 1; i < nb_total_visite; i++) 
        {
            x_UB[i] = i;
        }
        // Values that must belong to b set (empty here)
        int[] x_LB = new int[]{}; 
        
        for (int i = 0; i <= V; i++)
        {
            b[i] = mon_modele.setVar("sets_"+i, x_LB, x_UB);
        }        
        // Translation of the relation between SetVar sets and affection of travels to vehicles
        mon_modele.setsIntsChanneling(b, a).post();  
        
        // Constraint 12 : the sum of customer's demands affected to a vehicle must not exceed the vehicle capacity
        for (int i = 1; i <= V; i++)
        {
            // Sum of indexes weights
            IntVar sum = mon_modele.intVar(0, C[i]);    
            mon_modele.sumElements(b[i], D, sum).post();  
        }
              
        // Objective function (last constraint)
        IntVar d;          
        d = mon_modele.intVar("d", 0, H);
        
        // Computation of the distance between i and j (similar method to the TSP)
        IntVar[] dp;        
        dp = new IntVar[nb_total_visite];
        dp[0] = mon_modele.intVar(0);
        for (int i = 1; i < nb_total_visite; i++)
        {
            dp[i] = mon_modele.intVar("dp_"+i, 0, H);
        }
        for (int i = 1; i < nb_total_visite; i++)
        {
            mon_modele.element(dp[i], T_prime[i], s[i]).post();
	}       
        mon_modele.sum(dp, "=", d).post();       
        
        // Direction of minimization
	mon_modele.setObjective(Model.MINIMIZE, d);
        
        
        
        // RESOLUTION AND DISPLAY
        
	Solver mon_solveur = mon_modele.getSolver();
        mon_solveur.showStatistics();
        
	Solution solution = new Solution(mon_modele);
                
        Date heure_debut = new Date();
	long h_debut = heure_debut.getTime();

        // Resolution
	while (mon_solveur.solve())
        {
            solution.record(); 
            
            Date heure_fin = new Date();
            long h_fin = heure_fin.getTime();
            long duree = h_fin-h_debut;
            long duree_s = duree/1000;
            System.out.println("temps : " + duree_s + " s    " + d.toString());
            System.out.println("----------");
	}
 
        // Final display if a solution has been found
	if (mon_solveur.isFeasible() == ESat.TRUE)
        {          
            for (int i = 0; i < nb_total_visite; i++)
            {
                int suc = solution.getIntVal(s[i]);
                int affec = solution.getIntVal(a[i]);
                int ran = solution.getIntVal(p[i]);
                System.out.println("Sommet "+ i + "    Succ : "+ suc+  "    |   affec "+ affec+  "    |   rang "+ ran);
            }
            
            int cout = (int) mon_solveur.getBestSolutionValue(); 
            Date heure_fin = new Date();
            long h_fin = heure_fin.getTime();
            long duree = h_fin-h_debut;
            long duree_s = duree/1000;

            System.out.println("temps : " + duree_s + " s   d = " + cout);
            System.out.println("Fin");       
	}
	else
            System.out.println("pas de solution");
    }
}
