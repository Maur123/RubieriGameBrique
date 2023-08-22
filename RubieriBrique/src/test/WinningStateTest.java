package test;

import main.java.main.Brique;

import java.io.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WinningStateTest{
    BufferedReader reader;
    BufferedWriter writer;

    public WinningStateTest(){
        try {
            writer = new BufferedWriter(new FileWriter("src/test/TestResult.text"));
        } catch (IOException e) {
            System.out.println("Errore in apertura del file di output");
        }
    }

    public static void main(String args[]){
        new WinningStateTest().winning();
    }

    void winning(int type){

        try {
            switch (type) {
                case 0:
                    reader = new BufferedReader(new FileReader("src/test/WinningStateTest0.text"));
                    break;
                case 1:
                    reader = new BufferedReader(new FileReader("src/test/WinningStateTest1.text"));
                    break;
                case 2:
                    reader = new BufferedReader(new FileReader("src/test/WinningStateTest2.text"));
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File di test non trovato");
            System.exit(1);
        }

        Brique b = new Brique();
        //all'inizio non c'è alcun giocatore vincente
        //assertEquals(0,b.vincitore());

        String line = "";
        int ntest=0;
        try {
            boolean stop=false;
            while(!stop) {
                ntest++;
                int stato[] = new int[Brique.N*Brique.N];
                int i = 0;

                while (true) {
                    System.out.println(line);
                    line = reader.readLine();

                    if (line.equals("")) break;
                    if (line.contains("END")){
                        stop = true;
                        break;
                    }
                    String row[] = line.split(" ");
                    for (int j = 0; j < row.length; j++) {
                        stato[i] = Integer.parseInt(row[j]);
                        i++;
                    }
                }


                b.setGameState(stato);
                System.out.println(Arrays.toString(stato));
                assertEquals(type,b.vincitore());

                writer.write("Winning Test tipo "+type+" numero test: "+ntest+" SUPERATO \n");
                writer.flush();
                System.out.println("Test "+ntest+" superato");
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Errore nella lettura del file");
        }
    }
    void winning(){
        System.out.println("Test nessun vincitore: ");
        winning(0);
        System.out.println("Test giocatore 1 vince: ");
        winning(1);
        System.out.println("Test giocatore 2 vince: ");
        winning(2);
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Errore nella chiusura del buffer di scrittura");
        }
    }

}