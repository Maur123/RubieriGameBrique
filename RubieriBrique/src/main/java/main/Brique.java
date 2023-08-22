package main.java.main;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import java.util.Arrays;

import java.io.InputStream;


public class Brique{

	public final static int N = 15; //dimensione scacchiera
	private static final int dimX = 650; //larghezza della finestra
	private static final int dimY = 600; //altezza della finestra
	private static final int dimCasella = 30;
	private static final int dimBottoneAlt = 50;
	private static final int dimBottoneLarg = 100;
	protected Display display;
	protected int cont=0;
	protected int contPedina=0;
	protected Shell shell;
	protected static String playersIcons[] = {"","pedinanera.png","pedinarossa.png"};
	private boolean inGame = false;
	private Scacchiera scacchiera;

	Label pedina;
	Label lblTurno;
	Label vittoria;
	Image imagePedina;
	Button btnInizia;
	Button btnTermina;
	private class Scacchiera {
		Label caselle[]; //array per le immagini delle caselle della scacchiera
		Label pedine[]; // array per le immagini delle pedine (cerchio rosso/nero)
		int color[]; //array per i colori delle celle (0 chiaro, 1 scuro)
		int stato[]; // array che rappresenta lo stato della schacchiera
		int dim;

		public Scacchiera(int X){
			caselle = new Label[X*X];
			pedine = new Label[X*X];
			color =  new int[X*X];
			stato = new int[X*X];
			dim = X;
		}

		public int[] getColor() {
			return color;
		}

		public int getColor(int x) {
			return color[x];
		}

		public void setColor(int[] color) {
			this.color = color;
		}
		public int[] getStato() {
			return stato;
		}
		public int getStato(int x) {
			return stato[x];
		}

		public void setStato(int[] stato) {
			this.stato = stato;
		}

		public void setStato(int pos, int val) {
			this.stato[pos] = val;
		}

		public Label[] getCaselle() {
			return caselle;
		}
		public Label getCaselle(int x) {
			return caselle[x];
		}

		public void setCaselle(Label[] caselle) {
			this.caselle = caselle;
		}
		public void removeCaselle(int x){
			caselle[x].setBounds(0,0,0,0);
			caselle[x].setVisible(false);
			caselle[x].setEnabled(false);
		}
		public Label[] getPedine() {
			return pedine;
		}
		public Label getPedine(int x) {
			return pedine[x];
		}

		public void setPedine(Label[] pedine) {
			this.pedine = pedine;
		}

		public void setPedine(int pos, Label val) {
			this.pedine[pos] = val;
		}

		public void setPedine(int pos, int imgPos){
			this.pedine[pos] = new Label(shell, SWT.NONE);
			this.pedine[pos].setBackground(caselle[pos].getBackground());
			this.pedine[pos].setBounds(caselle[pos].getBounds());
			this.pedine[pos].setImage(getImage(playersIcons[stato[imgPos]]));
		}

		public void setImagePedine(int pos1, int pos2) {
			this.pedine[pos1].setImage(getImage(playersIcons[stato[pos2]]));
		}

		public void removePedine(int x){
			pedine[x].setBounds(0,0,0,0);
			pedine[x].setVisible(false);
			pedine[x].setEnabled(false);
		}

		public void inizializza_schema() {

			int ncella = 0;
			Label lbltemp;

			for (int i = 0; i < dim; i++) {
				int startx = 45;
				int starty = 29;
				for (int j = 0; j < dim; j++) {
					lbltemp = new Label(shell, SWT.NONE);

					if ((i + j) % 2 == 0) {
						// Cella della scacchiera scura
						Color darkColor = new Color(shell.getDisplay(), 139, 69, 19);
						lbltemp.setBackground(darkColor);
						color[ncella] = 1;
					} else {
						// Cella della scacchiera chiara
						Color lightColor = new Color(shell.getDisplay(), 222, 184, 135);
						lbltemp.setBackground(lightColor);
						color[ncella] = 0;
					}

					lbltemp.setBounds(startx + j * dimCasella, starty + i * dimCasella, dimCasella, dimCasella);
					lbltemp.addMouseListener(new Mouse(ncella));
					caselle[ncella++] = lbltemp;
				}
			}

		}

		public void makeMove(int giocatore, int icorr){

			if(giocatore!=-1) cont=giocatore+1; //make move viene chiamata dalla classe di test

			Image imageCasella;
			if(caselle[icorr].isEnabled()) {
				if(cont++%2==0) {
					imageCasella = getImage(playersIcons[1]);
					imagePedina = getImage("pedinarossa.png");
					stato[icorr]=1;
				}else {
					imageCasella = getImage(playersIcons[2]);
					imagePedina = getImage("pedinanera.png");
					stato[icorr]=2;
				}

				//setta l'immagine della pedina a seconda del giocatore e la posiziona nella scacchiera
				try {
					pedina.setImage(imagePedina);

					Label pos = new Label(shell, SWT.NONE);
					pos.setImage(imageCasella);
					pos.setBackground(caselle[icorr].getBackground());
					pos.setBounds(caselle[icorr].getBounds());
					pedine[icorr] = pos;

					caselle[icorr].setBounds(0, 0, 0, 0);
					System.out.println("Elimina " + icorr + " casella");
					caselle[icorr].setEnabled(false);
				}catch (Exception e){
					// nel caso di esecuzione di un test bypassa la visualizzazione della pedina su scacchiera
				}

				contPedina++;

				// APPLICA LE REGOLE DI CATTURA E CAMBIA IL COLORE DELLE PEDINE CORRISPONDENTI

// verifica se sono presenti due pedine in diagonale in direzione alto sinistra
				if (Utility.checkPos(icorr - dim - 1) && stato[icorr - dim - 1] != 0 && stato[icorr - dim - 1] == stato[icorr]) {

					// se le celle della scacchiera sono chiare catturo verso l'alto
					if (Utility.checkPos(icorr - dim) && pedine[icorr - dim] != null && color[icorr] == 0) {
						stato[icorr - dim] = stato[icorr];
						pedine[icorr - dim].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr - dim) && (pedine[icorr - dim] == null && color[icorr] == 0)) {
						stato[icorr - dim] = stato[icorr];
						pedine[icorr - dim] = new Label(shell, SWT.NONE);
						pedine[icorr - dim].setBackground(caselle[icorr - dim].getBackground());
						pedine[icorr - dim].setBounds(caselle[icorr - dim].getBounds());
						pedine[icorr - dim].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr - dim].setBounds(0, 0, 0, 0);
						caselle[icorr - dim].setEnabled(false);
					}

					// se le celle della scacchiera sono scure catturo verso il basso
					if (Utility.checkPos(icorr - 1) && pedine[icorr - 1] != null && color[icorr] == 1) {
						stato[icorr - 1] = stato[icorr];
						pedine[icorr - 1].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr - 1) && (pedine[icorr - 1] == null && color[icorr] == 1)) {
						stato[icorr - 1] = stato[icorr];
						pedine[icorr - 1] = new Label(shell, SWT.NONE);
						pedine[icorr - 1].setBackground(caselle[icorr - 1].getBackground());
						pedine[icorr - 1].setBounds(caselle[icorr - 1].getBounds());
						pedine[icorr - 1].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr - 1].setBounds(0, 0, 0, 0);
						caselle[icorr - 1].setEnabled(false);
					}
				}

// verifica se sono presenti due pedine in diagonale in direzione alto destra
				if (Utility.checkPos(icorr - dim + 1) && stato[icorr - dim + 1] != 0 && stato[icorr - dim + 1] == stato[icorr]) {

					// se le celle della scacchiera sono chiare catturo verso l'alto
					if (Utility.checkPos(icorr - dim) && pedine[icorr - dim] != null && color[icorr] == 0) {
						stato[icorr - dim] = stato[icorr];
						pedine[icorr - dim].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr - dim) && (pedine[icorr - dim] == null && color[icorr] == 0)) {
						stato[icorr - dim] = stato[icorr];
						pedine[icorr - dim] = new Label(shell, SWT.NONE);
						pedine[icorr - dim].setBackground(caselle[icorr - dim].getBackground());
						pedine[icorr - dim].setBounds(caselle[icorr - dim].getBounds());
						pedine[icorr - dim].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr - dim].setBounds(0, 0, 0, 0);
						caselle[icorr - dim].setEnabled(false);
					}

					// se le celle della scacchiera sono scure catturo verso il basso
					if (Utility.checkPos(icorr + 1) && pedine[icorr + 1] != null && color[icorr] == 1) {
						stato[icorr + 1] = stato[icorr];
						pedine[icorr + 1].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr + 1) && (pedine[icorr + 1] == null && color[icorr] == 1)) {
						stato[icorr + 1] = stato[icorr];
						pedine[icorr + 1] = new Label(shell, SWT.NONE);
						pedine[icorr + 1].setBackground(caselle[icorr + 1].getBackground());
						pedine[icorr + 1].setBounds(caselle[icorr + 1].getBounds());
						pedine[icorr + 1].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr + 1].setBounds(0, 0, 0, 0);
						caselle[icorr + 1].setEnabled(false);
					}
				}

// verifica se sono presenti due pedine in diagonale in direzione basso sinistra
				if (Utility.checkPos(icorr + dim - 1) && stato[icorr + dim - 1] != 0 && stato[icorr + dim - 1] == stato[icorr]) {

					// se le celle della scacchiera sono chiare catturo verso l'alto
					if (Utility.checkPos(icorr - 1) && pedine[icorr - 1] != null && color[icorr] == 0) {
						stato[icorr - 1] = stato[icorr];
						pedine[icorr - 1].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr - 1) && (pedine[icorr - 1] == null && color[icorr] == 0)) {
						stato[icorr - 1] = stato[icorr];
						pedine[icorr - 1] = new Label(shell, SWT.NONE);
						pedine[icorr - 1].setBackground(caselle[icorr - 1].getBackground());
						pedine[icorr - 1].setBounds(caselle[icorr - 1].getBounds());
						pedine[icorr - 1].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr - 1].setBounds(0, 0, 0, 0);
						caselle[icorr - 1].setEnabled(false);
					}

					// se le celle della scacchiera sono scure catturo verso il basso
					if (Utility.checkPos(icorr + dim) && pedine[icorr + dim] != null && color[icorr] == 1) {
						stato[icorr + dim] = stato[icorr];
						pedine[icorr + dim].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr + dim) && (pedine[icorr + dim] == null && color[icorr] == 1)) {
						stato[icorr + dim] = stato[icorr];
						pedine[icorr + dim] = new Label(shell, SWT.NONE);
						pedine[icorr + dim].setBackground(caselle[icorr + dim].getBackground());
						pedine[icorr + dim].setBounds(caselle[icorr + dim].getBounds());
						pedine[icorr + dim].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr + dim].setBounds(0, 0, 0, 0);
						caselle[icorr + dim].setEnabled(false);
					}
				}

// verifica se sono presenti due pedine in diagonale in direzione basso destra
				if (Utility.checkPos(icorr + dim + 1) && stato[icorr + dim + 1] != 0 && stato[icorr + dim + 1] == stato[icorr]) {

					// se le celle della scacchiera sono chiare catturo verso l'alto
					if (Utility.checkPos(icorr + 1) && pedine[icorr + 1] != null && color[icorr] == 0) {
						stato[icorr + 1] = stato[icorr];
						pedine[icorr + 1].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr + 1) && (pedine[icorr + 1] == null && color[icorr] == 0)) {
						stato[icorr + 1] = stato[icorr];
						pedine[icorr + 1] = new Label(shell, SWT.NONE);
						pedine[icorr + 1].setBackground(caselle[icorr + 1].getBackground());
						pedine[icorr + 1].setBounds(caselle[icorr + 1].getBounds());
						pedine[icorr + 1].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr + 1].setBounds(0, 0, 0, 0);
						caselle[icorr + 1].setEnabled(false);
					}

					// se le celle della scacchiera sono scure catturo verso il basso
					if (Utility.checkPos(icorr + dim) && pedine[icorr + dim] != null && color[icorr] == 1) {
						stato[icorr + dim] = stato[icorr];
						pedine[icorr + dim].setImage(getImage(playersIcons[stato[icorr]]));
					}
					if (Utility.checkPos(icorr + dim) && (pedine[icorr + dim] == null && color[icorr] == 1)) {
						stato[icorr + dim] = stato[icorr];
						pedine[icorr + dim] = new Label(shell, SWT.NONE);
						pedine[icorr + dim].setBackground(caselle[icorr + dim].getBackground());
						pedine[icorr + dim].setBounds(caselle[icorr + dim].getBounds());
						pedine[icorr + dim].setImage(getImage(playersIcons[stato[icorr]]));
						caselle[icorr + dim].setBounds(0, 0, 0, 0);
						caselle[icorr + dim].setEnabled(false);
					}
				}
			}
		}

	}
	/**
	 * Avvia l'applicazione
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Brique window = new Brique();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
		Metodo che inizializza la scacchiera di gioco
	 */


	public Brique() {

		shell = new Shell();
		shell.setSize(dimX, dimY); //setta la dimensione della finestra
		shell.setText("BRIQUE GAME"); //imposta il titolo della finestra

		scacchiera = new Scacchiera(N);
		scacchiera.inizializza_schema();

	}

	public int[] getGameState(){
		return scacchiera.getStato();
	}
	/*
		Setta lo stato della scacchiera al nuovo stato passato come parametro
	 */
	public void setGameState(int[] newState){
		scacchiera.setStato(Arrays.copyOf(newState,newState.length));
	}

	protected Image getImage(String path) {
		InputStream stream = getClass().getResourceAsStream("/main/" + path);
		if (stream != null) {
			return new Image(display, stream);
		} else {
			return null;
		}
	}

	public void makeMove(int giocatore, int icorr){
		scacchiera.makeMove(giocatore, icorr);
	}

	//Posiziona la pedina del giocatore nella posizione specificata


	/**
	 * Apre la finestra dell'applicazione
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	/**
	 * Crea i contenuti della finestra (etichette, pulsanti, menù a tendina)
	 */
	protected void createContents() {

		btnInizia = new Button(shell, SWT.NONE);
		btnInizia.setBounds(scacchiera.getCaselle(N-1).getBounds().x+dimCasella+10, scacchiera.getCaselle(N-1).getBounds().y+dimCasella+10, dimBottoneLarg, dimBottoneAlt);
		btnInizia.setText("INIZIA");
		btnInizia.addSelectionListener(new BottonePremuto(this));

		btnTermina = new Button(shell, SWT.NONE);
		btnTermina.setBounds(Utility.sommaRect(btnInizia.getBounds(),new Rectangle(0,dimBottoneAlt,0,0)));
		btnTermina.setText("TERMINA");
		btnTermina.addSelectionListener(new BottonePremuto(this));
		btnTermina.setVisible(false);

		vittoria = new Label(shell, SWT.NONE);
		vittoria.setBounds(Utility.sommaRect(btnTermina.getBounds(), new Rectangle(0,6*btnTermina.getBounds().height,0,btnTermina.getBounds().height)));

	}

	/**
	 @return 0 se non c'è alcun vincitore oppure il giocatore che ha vinto
	 */
	public int vincitore(){

		Utility.printState(this);
		boolean[] checked = new boolean[N*N];

		for(int i=0; i<N; i++){ //verifica tutti gli stati iniziali della prima riga
			if (vinto(1,i,checked)) return 1;
		}

		checked = new boolean[N*N];

		for(int i=N; i<N*(N-1)-1; i+=N){ //verifica tutti gli stati iniziali della prima colonna
			if (vinto(2,i,checked)) return 2;
		}

		return 0;
	}

	public boolean vinto(int giocatore, int currpos, boolean[] checked){

		if (checked[currpos]) return false;
		else checked[currpos]=true;

		if (!Utility.checkPos(currpos)) return false; //la posizione non è valida

		if(scacchiera.getStato(currpos) == 0) return false; //la posizione non è occupata da alcun giocatore

		if(scacchiera.getStato(currpos) != giocatore) return false; //il cammino si è interrotto (la cella è occupata dall'altro giocatore)

		if (currpos<N*N&&currpos>N*N-N&&giocatore==1){return true;} //se il giocatore 1 raggiunge tramite un cammino valido
		//l'ultima riga della scacchiera ha vinto

		if(((currpos+1)%N==0)&&giocatore==2){return true;} //se il giocatore 2 raggiunge tramite un cammino valido
		//l'ultima colonna della scacchiera ha vinto

		return 	Utility.checkPos(currpos-1)&&(currpos%N!=0)&&vinto(giocatore, currpos-1,checked)
				||Utility.checkPos(currpos+1)&&vinto(giocatore, currpos+1,checked)
				||Utility.checkPos(currpos-N)&&vinto(giocatore, currpos-N,checked)
				||Utility.checkPos(currpos+N)&&vinto(giocatore, currpos+N,checked);
	}

	//Classe privata per gestire gli eventi generati dal mouse
	private class Mouse implements org.eclipse.swt.events.MouseListener{

		int icorr;

		public Mouse(int i) {
			icorr = i;
		}

		@Override
		public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent me) {
			System.out.println("Mouse premuto due volte");
		}

		@Override
		public void mouseDown(org.eclipse.swt.events.MouseEvent md) {
			if(inGame) {
				System.out.println("Casella: "+icorr);

				makeMove(-1,icorr); //-1 indica che la funzione makemove viene chiamata dall'applicazione

				//verifica se uno dei due giocatori ha vinto
				int vincente = vincitore();
				if (vincente>0) {
					System.out.println("Il giocatore "+vincente+" ha vinto !!!");
					vittoria.setText("GIOCATORE "+vincente+"\nHA VINTO !!!");
					inGame=false;

				}
			}

		}


		@Override
		public void mouseUp(org.eclipse.swt.events.MouseEvent arg0) {}

	}

	private class BottonePremuto implements SelectionListener{

		Brique game;
		public BottonePremuto(Brique g){
			game = g;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {


		}

		@Override
		public void widgetSelected(SelectionEvent e) {

			if(e.getSource().toString().contains("INIZIA")) {
				System.out.println("Inizia premuto");

				lblTurno = new Label(shell, SWT.NONE);
				lblTurno.setBounds(Utility.sommaRect(btnTermina.getBounds(),new Rectangle(0,dimBottoneAlt*2,0,0)));
				lblTurno.setText("TURNO");

				if (pedina!=null) {
					pedina.setVisible(false);
				}

				imagePedina = getImage("pedinanera.png");

				pedina = new Label(shell, SWT.NONE);
				pedina.setBounds(Utility.sommaRect(lblTurno.getBounds(),new Rectangle(0,dimBottoneAlt,0,0)));
				pedina.setImage(imagePedina);

				inGame = true;

				btnInizia.setVisible(false);
				btnTermina.setVisible(true);
				pedina.setVisible(true);
				lblTurno.setVisible(true);

				vittoria.setText("");
				Utility.resetState(game);

			}


			if(e.getSource().toString().contains("TERMINA")) {
				System.out.println("Termina premuto");

				try {
					for(int i=0;i<N*N;i++) {
						if(scacchiera.getPedine(i)!=null) {
							scacchiera.removePedine(i);
						}

						scacchiera.removeCaselle(i);
					}
				}catch(Exception error) {
					System.out.println(contPedina);
					System.out.println(error.getMessage());
				}

				contPedina = 0;
				cont = 0;

				scacchiera.inizializza_schema();

				inGame = false;

				btnInizia.setVisible(true);
				btnTermina.setVisible(false);
				pedina.setVisible(false);
				lblTurno.setVisible(false);

				vittoria.setText("");

			}
		}

	}

}
