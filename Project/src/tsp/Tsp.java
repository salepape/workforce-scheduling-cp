package tsp;

import java.io.IOException;
import java.io.Closeable;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.search.strategy.selectors.values.IntDomainMin;
import org.chocosolver.solver.search.strategy.selectors.variables.FirstFail;
import org.chocosolver.solver.search.strategy.selectors.variables.MaxRegret;
import org.chocosolver.solver.search.strategy.selectors.variables.DomOverWDeg;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.ESat;

public class Tsp 
{
    public static void main(String[] args) throws ContradictionException 
    {
        // DATA of the problem

//        // CASE 1
        // Number of vertices
        int n = 5;
        // Superior bound of the time
        int sup = 50;
        
        // Time matrix (beginning time = 0)
        int[][] T;
        T = new int[][]
        {
            {0, 1, 3, 5, 1},
            {1, 0, 4, 4, 3},
            {3, 4, 0, 4, 1},
            {5, 4, 4, 0, 3},
            {1, 3, 1, 3, 0}
        };
        
        int[] TWInf;
        int[] TWSup;

        TWInf = new int[]
        {
            //0, 0, 0, 0, 0
            0, 1, 4, 6, 0
            //0, 22, 5, 10, 12 
        };
        
        TWSup = new int[]
        {
            //sup, sup, sup, sup, sup
            sup, 10, 15, 20, 30
            //sup, 25, 8, 13, 22
        };
                     
        int[] dureeSoin;
        dureeSoin = new int[]
        {
            //0, 0, 0, 0, 0
            //0, 1, 3, 1, 5
            0, 1, 3, 1, 5
        };

        
        // CASE 2
//        // Number of vertices
//        int n = 5;
//        // Superior bound of the time
//        int sup = 500;
//        
//        // Time matrix (beginning time = 0)
//        int[][] T;
//        T = new int[][]
//        {
//            {0,  1,  89, 6,  17},
//            {1,  0,  40, 5,  37},
//            {89, 40, 0,  42, 18},
//            {6,  5,  42, 0,  3},
//            {17, 37, 18, 3,  0}
//        };
//        
//        int[] TWInf;
//        int[] TWSup;
//
//        TWInf = new int[]
//        {
//            //0, 0, 0, 0, 0
//            0, 10, 40, 60, 0
//            //0, 20, 40, 80, 80
//        };
//        
//        TWSup = new int[]
//        {
//            //sup, sup, sup, sup, sup
//            sup, 100, 100, 150, 150
//            //sup, 40, 60, 110, 130
//        };
//        
//        int[] dureeSoin;
//        dureeSoin = new int[]
//        {
//            //0, 0, 0, 0, 0
//            0, 1, 3, 1, 5
//            //0, 3, 3, 3, 3
//        };
        
        
        // CASE 3
//        // Number of vertices
//        int n = 10;
//        // Superior bound of the time
//        int sup = 500;
//
//        int[][] T;
//        T = new int[][]
//        {
//            {0, 1, 3, 5, 1, 8, 1, 3, 9, 1},
//            {1, 0, 4, 4, 3, 1, 5, 2, 4, 9},
//            {3, 4, 0, 4, 1, 3, 7, 7, 3, 1},
//            {5, 4, 4, 0, 3, 9, 4, 1, 8, 3},
//            {1, 3, 1, 3, 0, 1, 6, 1, 1, 1},
//            {8, 1, 3, 9, 1, 0, 6, 4, 9, 7},
//            {1, 5, 7, 4, 6, 6, 0, 7, 4, 2},
//            {3, 2, 7, 1, 1, 4, 7, 0, 1, 6},
//            {9, 4, 3, 8, 1, 9, 4, 1, 0, 3},
//            {1, 9, 1, 3, 1, 7, 2, 6, 3, 0}
//        };
//        
//        int[] TWInf;
//        int[] TWSup;
//
//        TWInf = new int[]
//        {
//            //0, 0, 0, 0, 0, 0, 0, 0, 0, 0
//            0, 2, 4, 9, 2, 5, 3, 3, 1, 1
//        };
//        
//        TWSup = new int[]
//        {
//            //sup, sup, sup, sup, sup, sup, sup, sup, sup, sup
//            sup, sup/2, sup/4, sup/10, sup/4, sup, sup/2, sup/4, sup, sup/5
//        };
//        
//        int[] dureeSoin;
//        dureeSoin = new int[]
//        {
//            0, 1, 3, 1, 1, 5, 2, 1, 1, 1
//        };


//        // CASE 4
//        // Number of vertices
//        int n = 10;
//        // Superior bound of the time
//        int sup = 1000;
//        
//        int[][] T;
//        T = new int[][]
//        {
//            {0,  1,  30, 5,  1,  8,  1,  30, 9,  1},
//            {1,  0,  4,  64, 3,  1,  3,  7,  4,  95},
//            {30, 4,  0,  4,  16, 3,  2,  7,  3,  1},
//            {5,  64, 4,  0,  32, 9,  4,  1,  8,  3},
//            {1,  3,  16, 32, 0,  1,  60, 1,  99, 1},
//            {8,  1,  3,  9,  1,  0,  6,  4,  9,  7},
//            {1,  3,  7,  4,  60, 6,  0,  7,  44, 2},
//            {30, 2,  7,  1,  1,  4,  7,  0,  1,  60},
//            {9,  4,  3,  8,  99, 9,  44, 1,  0,  3},
//            {1,  95, 1,  3,  1,  7,  2,  60, 3,  0}
//        };
//        
//        int[] TWInf;
//        int[] TWSup;
//
//        TWInf = new int[]
//        {
//           //0, 0, 0, 0, 0, 0, 0, 0, 0, 0
//            0, 2, 4, 9, 2, 5, 3, 3, 1, 1
//        };
//        
//        TWSup = new int[]
//        {
//            //sup, sup, sup, sup, sup, sup, sup, sup, sup, sup
//            sup, sup/2, sup/4, sup/10, sup/4, sup, sup/2, sup/4, sup, sup/5
//        };
//        
//        int[] dureeSoin;
//        dureeSoin = new int[]
//        {
//            0, 1, 3, 1, 1, 5, 2, 1, 1, 1
//        };

        


        
        // Definition of the model by creating an instance of the class Model
        Model TSPModel = new Model("TSP en PPC");

        
        
        
        
        // DECISION VARIABLES of the problem
        
        // Tour vector in the graph (= r like routing, NOT like rank)
        IntVar[] r;
        // Successor vector (= s like successor) in the graph --> Not the following tour vector !
        IntVar[] s;
        // Precedessor vector (= p like predecessor) in the graph --> Not the previous tour vector !
        IntVar[] p;
        // Time needed to leave the vertex and arrive to its successor
        IntVar[] tpsSuc;
        // Objective function (sum of travelling times)
        IntVar d;
        // Instant at which the nurse arrives where the patient lives
        IntVar[] dateArr;
        // Instant at which the service performed by the nurse begins
        IntVar[] dateDeb;
        // Instant at which the service performed by the nurse ends
        IntVar[] dateFin;
        // Instant at which the nurse leaves where the patient lives --> In this case, equal to dateFin (because we focus on earliest points in time)
        //IntVar[] dateDep;
        
        
        
        
        
        // CONSTRAINTS OF THE PPC
        
        // Definition domain of r (constraint 1)
        r = new IntVar[n];
        for (int i = 0; i < n; i++) 
        {
            r[i] = TSPModel.intVar("r_" + i, 0, n - 1);
        }
        // The first vertex have to be associated with the hopital
        TSPModel.arithm(r[0], "=", 0).post(); 

        // Definition domain of s (constraint 2)
        s = new IntVar[n];
        for (int i = 0; i < n; i++) 
        {
            s[i] = TSPModel.intVar("s_" + i, 0, n - 1);
        }

        // Definition domain of p (constraint 3)
        p = new IntVar[n];
        for (int i = 0; i < n; i++) 
        {
            p[i] = TSPModel.intVar("p_" + i, 0, n - 1);
        }

        // Definition domain of dSuc (constraint 4)
        tpsSuc = new IntVar[n];
        for (int i = 0; i < n; i++) 
        {
            tpsSuc[i] = TSPModel.intVar("tpsSuc_" + i, 0, sup);
        }

        // Definition domain of d (constraint 5)
        d = TSPModel.intVar("d", 0, sup * n);

        // Constraint 6 : force each vertex to appear only once in s
        TSPModel.allDifferent(s).post();
        
        // Constraint 7 : force each vertex to appear only once in r
        TSPModel.allDifferent(p).post();

        // Constraint 8 : force each vertex to appear only once in r
        TSPModel.allDifferent(r).post();

        // Constraint 9 : link r and s
        for (int i = 0; i < n - 1; i++) 
        {
            TSPModel.element(r[i + 1], s, r[i], 0).post();
        }
        
        // Constraint 10 : link r and p
        for (int i = 1; i < n; i++) 
        {
            TSPModel.element(r[i - 1], p, r[i], 0).post();
        }
        
        // Constraint 11 : link predecessors and successors
        TSPModel.inverseChanneling(s, p).post();

        // Constraint 12 : compute travelling times between a vertex and its successor
        for (int i = 0; i < n; i++) 
        {
            TSPModel.element(tpsSuc[i], T[i], s[i]).post();
        }
        
        // Constraint 13 : avoid subtours of size i 
        for (int i = 0; i < n; i++)
        {
            TSPModel.arithm(s[i], "!=", i).post();   
        }

        
        
        
        
        /////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////
        
        // CONSTRAINTS RELATIVE TO TIME WINDOWS
        
        // Definition domain of dateArr, dateDeb, dateFin and dateDep (constraints 14, 15, 16)
        dateArr = new IntVar[n];
        dateDeb = new IntVar[n];
        dateFin = new IntVar[n];     
 
        for (int i = 0; i < n; i++) 
        {
            dateArr[i] = TSPModel.intVar("dateArr_" + i, 0, TWSup[i]);
            dateDeb[i] = TSPModel.intVar("dateDeb_" + i, TWInf[i], TWSup[i]);
            dateFin[i] = TSPModel.intVar("dateFin_" + i, TWInf[i], TWSup[i]);
        }
        
        for (int i = 0; i < n; i++)
        {                    
            // Constraints 17, 18, 19, 20, 21 [SUCCESSORS USE] : be sure that the date in which the following treatment will begin is after the end of the current one + time travelling
            
            // Arrival date on the vertex which succedes to the current one
            IntVar dateArrSuc = TSPModel.intVar("dateArrSuc_" + i, 0, sup); 
            TSPModel.element(dateArrSuc, dateArr, s[i], 0).post();   

            // If the current i is the last vertex in the tour, we naturally initialize the arrival date to 0 (a new tour will begin after the current one finishes)
            BoolVar bDernier = TSPModel.boolVar("bDernier_" + i);
            TSPModel.reifyXeqC(r[n-1], i, bDernier);
            TSPModel.ifThenElse
            (
                bDernier, 
                TSPModel.arithm(dateArrSuc, "=", 0),
                TSPModel.arithm(dateArrSuc, "=", dateFin[i], "+", tpsSuc[i])
            );
               
            IntVar TWInfSucVal = TSPModel.intVar("TWInfSucVal_" + i, 0, sup);
            TSPModel.element(TWInfSucVal, TWInf, s[i], 0).post();   
            
            IntVar dateDebSuc = TSPModel.intVar("dateDebSuc_" + i, 0, sup); 
            TSPModel.element(dateDebSuc, dateDeb, s[i], 0).post(); 
            
            // Either the vehicle will arrive in advance and has to wait the TW beginning, or the vehicle will serve directly when it arrives because the time arrival is comprised in TW
            TSPModel.max(dateDebSuc, dateArrSuc, TWInfSucVal).post();

               
            
            
            
            // Constraints 22, 23, 24, 25 [PREDECESSORS USE] : be sure that the date in which the current treatment has begun is after the end of the previous one + time travelling          
 
            // End task date for the vertex which precedes the current one
            IntVar dateFinPrec = TSPModel.intVar("dateFinPrec_" + i, 0, sup); 
            TSPModel.element(dateFinPrec, dateFin, p[i], 0).post();

            // Travelling time between the previous vertex and the current one
            IntVar tpsPrec = TSPModel.intVar("tpsPrec_" + i, 0, sup);
            TSPModel.element(tpsPrec, tpsSuc, p[i], 0).post();
            
            // If the current i is the first vertex in the tour, we naturally initialize the arrival date to 0 (by convention, no trouble in the hospital)
            BoolVar bPremier = TSPModel.boolVar("bPremier_" + i);
            TSPModel.reifyXeqC(r[0], i, bPremier);
            TSPModel.ifThenElse
            (
                bPremier, 
                TSPModel.arithm(dateArr[i], "=", 0),
                TSPModel.arithm(dateArr[i], "=", dateFinPrec, "+", tpsPrec)
            );
            
            IntVar TWInfVal = TSPModel.intVar("TWInfVal_" + i, TWInf[i]);
            
            // Either the vehicle is arrived in advance and has to wait the TW beginning, or the vehicle serves directly when it arrives because the time arrival is comprised in TW
            TSPModel.max(dateDeb[i], dateArr[i], TWInfVal).post(); 
            
            
            
            
            
            // Constraint 27 : compute task end date
            TSPModel.arithm(dateFin[i], "=", dateDeb[i], "+", dureeSoin[i]).post();
        }
                  
        // Constraint 28, 29 : selective value which guides solver to choose the following vertex in the tour according to the smallest time travelling among all edges leading to successors 
        IntVar[] tpsSucDiscrim = TSPModel.intVarArray("tpsSucDiscrim_", n, 0, 100*sup);
        for (int i = 0; i < n; i++)
        {
            // Formulas to adapt to sup value
            IntVar tpsSucAugm = TSPModel.intVar("tpsSucAugm_"+i, 0, 100*sup);           
            TSPModel.arithm(tpsSucAugm, "=", tpsSuc[i], "*", 100).post();  
            
            TSPModel.arithm(tpsSucDiscrim[i], "=", tpsSucAugm, "+", s[i]).post();    
        }
        
//        // Constraint 30, 31, 32 : selective value which guides solver to choose the following vertex in the tour according to the early TWInf among all successors 
//        IntVar[] TWSupDiscrim = TSPModel.intVarArray("TWSupDiscrim_", n, 0, 100*sup);
//        for (int i = 0; i < n; i++)
//        {
//            // Formulas to adapt to sup value  
//            IntVar TWSupSuc = TSPModel.intVar("TWSupSuc_" + i, 0, sup);
//            TSPModel.element(TWSupSuc, TWSup, s[i], 0).post(); 
//            
//            IntVar TWSupAugm = TSPModel.intVar("TWSupAugm_"+i, 0, 100*sup);           
//            TSPModel.arithm(TWSupAugm, "=", TWSupSuc, "*", 100).post();  
//            
//            TSPModel.arithm(TWSupDiscrim[i], "=", TWSupAugm, "+", s[i]).post();    
//        }
  
        /////////////////////////////////////////////////////////////////////////////////////        
        /////////////////////////////////////////////////////////////////////////////////////
        
        
        
        
        
        // Objective function (last constraint) --> No need to add taskDuration because it's a constant (no changes a priori)
        TSPModel.sum(tpsSuc, "=", d).post();
        TSPModel.setObjective(Model.MINIMIZE, d);

        
        
        
        
        // RESOLUTION AND DISPLAY
        
        Solver TSPSolver = TSPModel.getSolver();
        
        // Display [Model] the main caracteristics of the current solution according to the decision taken by the algorithm
        TSPSolver.showShortStatistics();
        // Display the decision tree (which variable has been choosed for branching first)
        TSPSolver.showDecisions();  
        //TSPSolver.showContradiction();  
        
        TSPSolver.setSearch(                 
//            Search.intVarSearch(
//                 //First fail variable selector : selects the leftmost variable of smallest domain size (instantiated variables are ignored). 
//                 //Here, choose the vertex with the least successors
//                new FirstFail(TSPModel),      
//                // IntDomainMin value selector : selects the smallest domain value (lower bound)
//                new IntDomainMin(), 
//                // Variable(s) to branch on
//                tpsSuc)
                
//            Search.intVarSearch(
//                // MaxRegret variable selector : selects the variable with the largest difference between the two smallest values in its domain (instantiated variables are ignored)
//                //Here, choose the vertex with lowest difference between the 2 smallest travelling times to successors
//                new MaxRegret(),              
//                // IntDomainMin value selector : selects the smallest domain value (lower bound)
//                new IntDomainMin(), 
//                // Variable(s) to branch on
//                tpsSuc),
                             
//            Search.intVarSearch(
//                new FirstFail(TSPModel),      
//                new IntDomainMin(),           
//                s)
                
//            Search.intVarSearch(
//                new MaxRegret(),              
//                new IntDomainMin(),           
//                s)
                
            Search.intVarSearch(
                // MaxRegret variable selector : selects the variable with the largest difference between the two smallest values in its domain (instantiated variables are ignored)
                new MaxRegret(),              
                // IntDomainMin value selector : selects the smallest domain value (lower bound)
                new IntDomainMin(), 
                // Variable(s) to branch on
                tpsSucDiscrim),
//                    
            Search.intVarSearch(
                new FirstFail(TSPModel),        
                new IntDomainMin(),             
                r)
                
//            Search.intVarSearch(
//                new MaxRegret(),              
//                new IntDomainMin(),           
//                r) 
                
//            Search.intVarSearch(
//                 //First fail variable selector : selects the leftmost variable of smallest domain size (instantiated variables are ignored). 
//                new FirstFail(TSPModel),      
//                // IntDomainMin value selector : selects the smallest domain value (lower bound)
//                new IntDomainMin(), 
//                // Variable(s) to branch on
//                tpsSucDiscrim),           

//            Search.intVarSearch(
//                 //First fail variable selector : selects the leftmost variable of smallest domain size (instantiated variables are ignored). 
//                 //Prend le sommet qui a le moins de successeurs
//                new FirstFail(TSPModel),      
//                // IntDomainMin value selector : selects the smallest domain value (lower bound)
//                new IntDomainMin(), 
//                // Variable(s) to branch on
//                TWDiscrim),
                
//            Search.intVarSearch(
//                // MaxRegret variable selector : selects the variable with the largest difference between the two smallest values in its domain (instantiated variables are ignored)
//                new MaxRegret(),              
//                // IntDomainMin value selector : selects the smallest domain value (lower bound)
//                new IntDomainMin(), 
//                // Variable(s) to branch on
//                TWDiscrim),
            
//            // DomOverWDeg variable selector
//            new DomOverWDeg(tpsSucDiscrim, 314, new IntDomainMin())
        );


        Solution TSPSolution = new Solution(TSPModel);
        
        // Closeable : source or destination of data that can be closed. The close method is invoked to release resources that the object is holding (such as open files).
        Closeable to_close = TSPSolver.outputSearchTreeToGraphviz("Test.dot");   
    
        // Enumerate all solutions
        while (TSPSolver.solve()) 
        {
            TSPSolution.record();
            System.out.println(d.toString());
            //for(int a = 0; a < n; ++a)
            //    System.out.println(tpsSucDiscrim[a]);
            System.out.println("----------");
            System.out.println("Attention, le d correspond à la somme des durées de trajet (argument de la fonction min), ne prend pas en compte la durée des soins !");
        }

        if (TSPSolver.isFeasible() == ESat.TRUE) 
        {
            // Display [Solution] the solution found (ie all values that have taken decision variables of the problem)
            //System.out.println(solution.toString());
            int cout = (int) TSPSolver.getBestSolutionValue();
            System.out.println("Cmax = " + cout);
            
            // Personalisation of the display [Solution] 
            int tempsTotal = 0;
            for (int i = 0; i < n; i++) 
            {
                int tourSol = TSPSolution.getIntVal(r[i]);
                
                int sucSol = TSPSolution.getIntVal(s[tourSol]);
                int dateArrSol = TSPSolution.getIntVal(dateArr[tourSol]);
                int dateDebSol = TSPSolution.getIntVal(dateDeb[tourSol]);
                int dateFinSol = TSPSolution.getIntVal(dateFin[tourSol]);
                int tpsSucSol = TSPSolution.getIntVal(tpsSuc[tourSol]);

                System.out.println("Sommet n°" + i + " de la tournée : " + tourSol
                                        + "    |    TW = [" + TWInf[tourSol] + " ; " + TWSup[tourSol] + "]"
                                        + "    |    dateArr : " + dateArrSol
                                        + "    |    dateDeb : " + dateDebSol
                                        + "    |    dureeSoin : " + dureeSoin[tourSol]
                                        + "    |    dateFin = dateDep : " + dateFinSol
                                        + "    |    Sommet n°" + i + " de la tournée suivante : " + sucSol
                                        + "    |    Temps requis pour l'atteindre : " + tpsSucSol
                );
            
                if (i == n-1)
                {
                    tempsTotal += dateFinSol;
                    tempsTotal += tpsSucSol;
                }
            }    
            
            System.out.println("Temps total à monopoliser pour que l'infirmière puisse boucler son tour : " + tempsTotal);
        } 
        else 
        {
            System.out.println("Pas de solution !");
        }
        
        try 
        {
            to_close.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
