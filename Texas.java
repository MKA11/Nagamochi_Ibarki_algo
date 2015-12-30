
package texas;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


public class Texas {

      private static  Random randval = new Random();
      private static int Critedge = 0;
    public static void main(String[] args) throws FileNotFoundException, IOException {

        int n = 22;
        int edges = 0 ;
   
         
        ArrayList<Double> density = new ArrayList<>();
        ArrayList<Integer> Lmda = new ArrayList<>();
       ArrayList<Integer> mindeg = new ArrayList<>();
        ArrayList<Integer> criticalEdges = new ArrayList<>();


        // graph initialization
        IbarkiGraph network = new IbarkiGraph(n);
        // experiment for m values 40 to 400
        for(int m = 40 ; m <= 400 ; m = m+5){
            
            int[] degreeMat = new int[n];
           
            if(m==40)
            edges = m - edges;
            else
            edges = 5;
            ArrayList<Integer> totnodes = new ArrayList<>();
            for(int i = 0 ; i < n ; i++)
                totnodes.add(i);
            while(edges>0){
                int i = Math.abs(randval.nextInt())%n;
                int j = Math.abs(randval.nextInt())%n;
                if(i!=j){
                    edges--;
                    network.adjointMat[i][j]++;
                    network.adjointMat[j][i]++;
                    degreeMat[i]++;
                    degreeMat[j]++;
                }
            }
            RealculateDegMatrix(degreeMat, network.adjointMat);
            
            
           
            int minDeg = degreeMat[0];
            for(int k = 0 ; k < degreeMat.length ; k++)
                if(minDeg>degreeMat[k])
                    minDeg = degreeMat[k];
            mindeg.add(minDeg);// density calculation
            density.add(2*m/(double)n);
            
            if(!network.CheckConnected())
            {
                Lmda.add(0);
                criticalEdges.add(0);
                continue;
           }

          
            // matrix initialization
            int[][] adjointMat = new int[n][n];
            cpyMat(adjointMat, network.adjointMat, n);
            Critedge = 0;
            int LG = Lambda(adjointMat, totnodes, n);
            Lmda.add(LG);
            
            criticalEdges.add(Critedge);
           
            
        }
        
        System.out.println("Lowest Degrees\t:"+mindeg);
        System.out.println("Critical Edges\t:"+criticalEdges);
       System.out.println("Lambda Values\t:"+Lmda);
        System.out.println("Densities\t:"+density);
        printToFile(Lmda, density, mindeg, criticalEdges);
    }
    private static void prntMat(int[][] adjointMat, int V) {
        for(int i = 0 ; i < V ; i++){
            for(int j = 0 ; j < V; j++){
                System.out.print(adjointMat[i][j]+"");
            }
            System.out.println("");
        }
        
    }
    private static void printHashMap(HashMap<Integer, Integer> totnodes) {
        for(Integer i : totnodes.keySet()){
            System.out.println(i+" : "+totnodes.get(i));
        }    
    
    }

    

    private static void CreateOrder(ArrayList<Integer> MA, int[][] adjointMat, ArrayList<Integer>totnodes) {
        
        int v1 = totnodes.get(Math.abs(randval.nextInt())%totnodes.size());
        MA.add(v1);
        for(int i = 0 ; i < totnodes.size() - 1 ; i++){
            int SumDeg = 0; // we start of my summing up the edges to MA for each of remaining vertices
            
            int[] degVectToMA = new int[totnodes.size()];
            
            int max = 0;
            int maxcnt = 0;
            
            for(int j = 0 ; j < totnodes.size() ; j++){
                int temp = totnodes.get(j);
                if(MA.contains(temp))
                    continue;
                for(Integer k : MA){
                    degVectToMA[j] += adjointMat[temp][k];
                    if(degVectToMA[j] > max)
                    {
                        maxcnt = temp;
                        max = degVectToMA[j];
                    }
                }
            }
            MA.add(maxcnt);
        }
        
    }

    private static int Lambda(int[][] adjointMat, ArrayList<Integer> totnodes, int n) {
        
        if(totnodes.size() == 2){
            return adjointMat[totnodes.get(0)][totnodes.get(1)];
        }
        
        ArrayList<Integer> MA = new ArrayList<>();
        CreateOrder(MA, adjointMat, totnodes);// get MA Ordering
        //System.out.println("MA Ordering:"+MA);
        int x = MA.get(MA.size() - 2);
        int y = MA.get(MA.size() - 1);
        int LambdaXY = degreeOf(y, adjointMat, n);
        int edgeCriticality = adjointMat[x][y];
        merge(x, y, adjointMat, n);
        for(int i = 0 ; i < totnodes.size() ; i++){
            if(totnodes.get(i) == y){
                totnodes.remove(i);
                break;
            }
        }
         int Lmdaxy = Lambda(adjointMat, totnodes, n);
         
         if(LambdaXY<Lmdaxy){
             // this means removing x,y decreases the connectivity of the IbarkiGraph
             Critedge += edgeCriticality;
             return LambdaXY;
         }
         else{
             return Lmdaxy;
         }
      
            
    }

    private static int degreeOf(Integer x, int[][] adjointMat, int n) {
        int degree = 0;
        for(int i = 0 ; i < n ; i++){
            degree+=adjointMat[x][i];
        }
        
        return degree;
    }

    private static void merge(int x, int y, int[][] adjointMat, int n) {
        adjointMat[x][y] = 0;
        adjointMat[y][x] = 0;
        for(int i = 0 ; i < n ; i++){
            if(i!=x){
                adjointMat[x][i]+=adjointMat[y][i];
                adjointMat[i][x] = adjointMat[x][i];
                adjointMat[y][i] = 0;
                adjointMat[i][y] = 0;
            }
        }
        
    }

    private static void RealculateDegMatrix(int[] degreeMat, int[][] adjointMat) {
        for(int i = 0 ; i < degreeMat.length ; i++){
            degreeMat[i] = 0 ;
            for(int j = 0 ; j < degreeMat.length; j++)
            {
                degreeMat[i]+= adjointMat[i][j];
            }
        }
    
    }

    private static void printToFile(ArrayList<Integer> Lmda, ArrayList<Double> density, ArrayList<Integer> mindeg, ArrayList<Integer> criticalEdges) throws FileNotFoundException, IOException {
       FileWriter fw = new FileWriter(".\\/data\\/output.csv");
       fw.write("Lmda,density,lowestDegree,criticalEdges\n");
               
        
        for(int i = 0 ; i < Lmda.size() ; i++){
            fw.write(Lmda.get(i)+","+density.get(i)+","+mindeg.get(i)+","+criticalEdges.get(i)+"\n");
        }
        fw.close();
    }

    private static void cpyMat(int[][] a, int[][] b, int n) {
        for(int i = 0 ; i < n ; i++){
            for(int j = 0 ; j < n ; j++){
                a[i][j] = b[i][j];
            }
        }
    }
    
}


