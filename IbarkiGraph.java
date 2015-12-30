package texas;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


class IbarkiGraph{
    
    int[][] NxtVertex;
    int V;
    int[][] adjointMat;
    int[][] totPairs;

    int ConnectCompCnt = 0; 
    
    IbarkiGraph(int V){
        adjointMat = new int[V][V];
        totPairs = new int[V][V];
        NxtVertex = new int[V][V];
       //toVisit = new boolean[V];
        this.V = V;    
    }
 
    IbarkiGraph(int V, ArrayList<Link> E){
        adjointMat = new int[V][V];
        totPairs = new int[V][V];
        NxtVertex = new int[V][V];
            this.V = V;    
            
                for(int i = 0 ; i < V ; i++){
                    for(int j = 0 ; j < V ; j++){
                        if(i!=j){
                            adjointMat[i][j] = Integer.MAX_VALUE;
                            totPairs[i][j] = Integer.MAX_VALUE;
                            NxtVertex[i][j] = -1;
                            }
                        else
                            NxtVertex[i][j] = j;
                    }
                }
                
                for(Link eij : E){
                    
                    addLink(eij);
                    
                }
            
        
    }
    void addLink(Link E){
        adjointMat[E.a][E.b] = E.weight;
        totPairs[E.a][E.b]  = E.weight;
        NxtVertex[E.a][E.b] = E.b;
    }
    void floydWarshall(){
// finding shortest path        
        for(int k = 0 ; k < V ; k++){
            for(int i = 0 ; i < V ; i++){
                for(int j = 0 ; j < V ; j++){
                    if(totPairs[i][k]+totPairs[k][j]<totPairs[i][j]){
                        totPairs[i][j] = totPairs[i][k] + totPairs[k][j];
                        NxtVertex[i][j] = NxtVertex[i][k];
                    }
                }
            }
        }
    }

    void forceInputs() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("forcedInput.txt"));
        for(int i = 0 ; i < this.V ; i++){
            String[] inputs = sc.nextLine().split("\\t");
            for(int j = 0 ; j < this.V ; j++){
                this.adjointMat[i][j] = Integer.parseInt(inputs[j]);
            }
        }
    }

    boolean CheckConnected() {        
        this.ConnectCompCnt=1;
        boolean[] toVisit = new boolean[this.V];
        int VertCnt = this.V;
        ArrayList<Integer> next = new ArrayList<Integer>();
        next.add(0);
        while(next.size()!=0){
            int i = next.get(0);
            toVisit[i] = true;
            VertCnt--;
            next.remove(0);
            for(int k = 0 ; k < V ; k++){
                if(this.adjointMat[i][k] > 0 && !toVisit[k] && !next.contains(k)){
                    next.add(k);
                }
            }
            
        }
        
        if(VertCnt == 0){
            return true;
        }
        else
        {
            System.out.println("Unconnected are");
            for(int i = 0 ; i < this.V ; i++){
                if(!toVisit[i]){
                    System.out.println(i);
                }
            }
            return false;
        }
    
    }
    
}

