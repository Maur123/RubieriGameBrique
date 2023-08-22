package test;

import main.java.main.Brique;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoveCheck{
    BufferedReader reader;
    BufferedWriter writer;

    public MoveCheck(){
        try {
            writer = new BufferedWriter(new FileWriter("src/test/TestResult2.text"));
        } catch (IOException e) {
            System.out.println("Errore in apertura del file di output");
        }
    }

    public static void main(String args[]){
        new MoveCheck().moving();
    }

    void moving(int type){

        try {
            switch (type) {
                case 0:
                    reader = new BufferedReader(new FileReader("src/test/MoveCheck0.text"));
                    break;
                case 1:
                    reader = new BufferedReader(new FileReader("src/test/MoveCheck1.text"));
                    break;
                case 2:
                    reader = new BufferedReader(new FileReader("src/test/MoveCheck2.text"));
                    break;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File di test non trovato");
            System.exit(1);
        }

        Brique b = new Brique();

        String line = "";
        int ntest=0;

        try {
            boolean stop=false;
            while(!stop) {
                ntest++;

                int statoPrima[] = new int[Brique.N*Brique.N];
                int statoDopo[] = new int[Brique.N*Brique.N];

                // Acquisizione dello stato precedente alla mossa
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
                        statoPrima[i++] = Integer.parseInt(row[j]);
                    }
                }

                // Lettura della mossa
                String move = reader.readLine();
                String moveSplit[] = move.split(" ");

                reader.readLine(); //riga vuota separa i due stati

                int giocatore = Integer.parseInt(moveSplit[0]);
                int posizione = Integer.parseInt(moveSplit[1]);

                System.out.println("Giocatore: "+giocatore+", posizione: "+posizione);

                // Acquisizione dello stato successivo alla mossa
                i=0;
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
                        statoDopo[i++] = Integer.parseInt(row[j]);
                    }
                }

                b.setGameState(statoPrima);
                b.makeMove(giocatore,posizione);

                //System.out.println("Stato precedente alla mossa: "+Arrays.toString(statoPrima));
                //System.out.println("Stato successivo alla mossa (ATTESO): "+Arrays.toString(statoDopo));
                //System.out.println("Stato EFFETTIVO: "+Arrays.toString(b.getGameState()));

                assertArrayEquals(statoDopo,b.getGameState());

                writer.write("MoveCheck "+type+" numero test: "+ntest+" SUPERATO \n");
                writer.flush();
                System.out.println("Test "+ntest+" superato");
            }

            reader.close();

        } catch (IOException e) {
            System.out.println("Errore nella lettura del file");
        }
    }
    void moving(){
        System.out.println("Test nessuna cattura: ");
        moving(0);
        System.out.println("Test cattura giocatore 1: ");
        moving(1);
        System.out.println("Test cattura giocatore 2: ");
        moving(2);
        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Errore nella chiusura del buffer di scrittura");
        }
    }

}