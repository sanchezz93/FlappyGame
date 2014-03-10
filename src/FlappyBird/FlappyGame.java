//
//  Created by manolo on Mar 3, 2014.
//  Copyright (c) 2014 manolo. All rights reserved.
//

package FlappyBird;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.floor;
import java.util.LinkedList;
import javax.swing.JFrame;


/**
 *
 * @author manolo
 */
public class FlappyGame extends JFrame implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
//	Se declaran las variables.
	private Image dbImage;				// Imagen a proyectar
	private Image background;			// Imagen de fondo
	private Graphics dbg;				// Objeto grafico
	private Base hank;					// Objeto hank
	private Base walter;				// Objeto walter
	private Base bala;					// Objeto bala
	private LinkedList<Base> barras;	// Objeto barras
	private Base vasovidas;				// Objeto desplegado de vidas
	private Base pausa;					// Objeto que pinta el pausa
	private Base instruc;				// Objeto que pinta las instrucciones
	private Base gameo;					// Objeto que pinta el Game over
	private Base gamew;					// Objeto que pinta el Game over
	private Base sidebar;				// Objeto desplegado de barra de lado
	private int numBarras;				// Numero de barras
	private int highestscore;           // El puntuaje mas alto
	private SoundClip choqueBarra;		// Sonido de choque con barra
	private SoundClip choqueWalter;		// Sonido de choque con walter
	private SoundClip choqueHank;		// Sonido de choque con hank
	private SoundClip paredEscudo;		// Sonido de choque con pared/escudo
	private SoundClip disparo;			// Sonido de disparo
	private SoundClip caida;			// Sonido de caida
	private char dir;					// dir es la direccion de hank
	private int estado;					// el estado actual del juego (0 = corriendo, 1 = pausa, 2 = informacion,3 = creditos)
	private int score;					// el puntaje
	private long tiempoActual;			// el tiempo actual que esta corriendo el jar
	private long tiempoInicial;			// el tiempo inicial
	private boolean sound;				// sound es para ver si el sonido esta activado
	private boolean cargar;				// variable que carga el archivo
	private boolean hankDied;

	
	public FlappyGame() {
		init();
		start();
	}
	
	
	public void init() {
//		Inicializacion de variables
		setSize(1200,720);
		
		score = 0;
		estado = 2;
		
		sound = false;
        cargar = false;
		
		choqueBarra = new SoundClip("resources/meth.wav");	// Sonido cuando chocas con un malo
		choqueWalter = new SoundClip("resources/dano1.wav");		// Sonido cuando chocas con la pared
		choqueHank = new SoundClip("resources/dano2.wav");
		paredEscudo = new SoundClip("resources/paredescudo.wav");
		disparo = new SoundClip("resources/disparo.wav");
		choqueBarra.setLooping(false);
		choqueWalter.setLooping(false);
		choqueHank.setLooping(false);
		paredEscudo.setLooping(false);
		disparo.setLooping(false);
		
		background = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/BackGroundBB.jpg"));
		
//		Se cargan las imágenes para la animación
		Image hank1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/HankWalk0.png"));
		Image hank2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/HankWalk1.png"));
                
		Image walter1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/WalterWalk0.png"));
		Image walter2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/WalterWalk1.png"));
        Image walter3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/WalterWalk2.png"));
		Image walter4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/WalterWalk3.png"));
                
		Image barra1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/meth1.png"));
		Image barra2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/meth2.png"));
		Image barra3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/meth3.png"));
		
		Image bala1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/bullet1.png"));
		Image bala2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/bullet2.png"));
		Image bala3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/bullet3.png"));
		Image bala4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/bullet4.png"));
                
		Image vasovidas1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/Hpot0.png"));
		Image vasovidas2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/Hpot1.png"));
		Image vasovidas3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/Hpot2.png"));
		Image vasovidas4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/Hpot3.png"));
		Image vasovidas5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/Hpot4.png"));

		Image sidebar1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/sidebar.jpg"));
		Image pausa1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/pausa.png"));
		Image instruc1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/instrucciones.png"));
		Image gameo1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/gameover.png"));
		Image gameo2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resources/gameover2.png"));
                
//		Se crea la animación
		Animacion animH = new Animacion(), animW = new Animacion(), animB = new Animacion();
		Animacion animBr = new Animacion(), animV = new Animacion(), animSb = new Animacion();
        Animacion animP = new Animacion(), animI = new Animacion(), animG = new Animacion(), animG2 = new Animacion();
		int hankFrameTime = 100, walterFrameTime = 150;
		animH.sumaCuadro(hank1, hankFrameTime);
		animH.sumaCuadro(hank2, hankFrameTime);
		animW.sumaCuadro(walter1, walterFrameTime);
		animW.sumaCuadro(walter2, walterFrameTime);
		animW.sumaCuadro(walter3, walterFrameTime);
		animW.sumaCuadro(walter4, walterFrameTime);
		
		animBr.sumaCuadro(barra1, 0);
		animBr.sumaCuadro(barra2, 0);
		animBr.sumaCuadro(barra3, 0);
		
		animB.sumaCuadro(bala1, walterFrameTime);
		animB.sumaCuadro(bala2, walterFrameTime);
		animB.sumaCuadro(bala3, walterFrameTime);
		animB.sumaCuadro(bala4, walterFrameTime);
		
		animV.sumaCuadro(vasovidas1, 0);
		animV.sumaCuadro(vasovidas2, 0);
		animV.sumaCuadro(vasovidas3, 0);
		animV.sumaCuadro(vasovidas4, 0);
		animV.sumaCuadro(vasovidas5, 0);
		animSb.sumaCuadro(sidebar1, 0);
		animP.sumaCuadro(pausa1, 0);
		animI.sumaCuadro(instruc1,0);
		animG.sumaCuadro(gameo1, 0);
		animG2.sumaCuadro(gameo2, 0);
                
//		Se agrega la animacion a los objetos
		hank = new Base(425,630,4,animH);
		walter = new Base(425,-170,1,animW);
		walter.setMoviendo(false);
		bala = new Base(hank.getX(),610,0,animB);
		bala.setVelX(0);
		bala.setVelY(0);
		vasovidas= new Base(1002,35,0,animV);
		sidebar = new Base(950,20,0,animSb);
		instruc = new Base(0,20,0, animI);
		gameo = new Base(0,20,0,animG);
		gamew = new Base(0,20,0,animG2);
		pausa = new Base(0,20,0,animP);
                
                
                
		Base barra;
		numBarras = 40;
		barras = new LinkedList();
		for (int i=0; i<numBarras; i++) {
			int a = i % 10;
			int x = 95 * a;
			int y = (int)(30 * floor((95 * i)/950)) + 80 + (int)floor((95 * i)/950)*20;
			barra = new Base(x,y,3,animBr);
			barras.add(barra);
		}
		
		setResizable(false);
		setBackground(new Color(43, 48, 51));
		addKeyListener(this);
		
	}
	
	/** 
	 * Metodo <I>start</I> sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
	public void start () {
//		Declaras un hilo
		Thread th = new Thread(this);
//		Empieza el hilo
		th.start();
	}
		
	/** 
	 * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
     * la posicion en x o y dependiendo de la direccion, finalmente 
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     * 
     */
	@Override
	public void run () {
		while (true) {
			actualiza();
			checaColision();
//			Se actualiza el <code>Applet</code> repintando el contenido.
			repaint();
			try	{
//				El thread se duerme.
				Thread.sleep (20);
			}
			catch (InterruptedException ex)	{
				System.out.println("Error en " + ex.toString());
			}
		}
	}
	
	/**
	 * Metodo usado para actualizar la posicion de objetos
	 * 
	 */
	public void actualiza() {
                
		if (estado == 0) {
//			Determina el tiempo que ha transcurrido desde que el Applet inicio su ejecución
			long tiempoTranscurrido = System.currentTimeMillis() - tiempoActual;
            
//			Guarda el tiempo actual
			tiempoActual += tiempoTranscurrido;

//			Actualiza la posicion y la animación en base al tiempo transcurrido
			if (dir == 'l') {
				hank.actualiza(tiempoTranscurrido);
				int newX = hank.getX()-8;
				if (newX >= 0) {
					hank.setX(newX);
				}
			} else if (dir == 'r') {
				hank.actualiza(tiempoTranscurrido);
				int newX = hank.getX()+8;
				if (newX <= 850) {
					hank.setX(newX);
				}
			}
			if (walter.getMoviendo()) {
				walter.actualiza(tiempoTranscurrido);
				walter.setX(walter.getX()+walter.getVelX());
				if (walter.getY()<20) {
					walter.setY(walter.getY()+walter.getVelY());
				} else if (walter.getVelX() == 0) {
					walter.setVelX(2);
				}
			}
			
			if (bala.getVelX() == 0) {
				bala.actualiza(tiempoTranscurrido);
				bala.setX(hank.getX());
				bala.setY(610);
			} else {
				bala.actualiza(tiempoTranscurrido);
				bala.setX(bala.getX()+bala.getVelX());
				bala.setY(bala.getY()+bala.getVelY());
			}
		}
		if (hank.getLives() == 0) {
			hankDied = true;
			estado = 3;
			try {
				grabaArchivo();
			} catch(IOException e) {
				System.out.println("Error en guardar");
			}
		}
		if (walter.getLives() == 0) {
			hankDied = false;
			estado = 3;
			try {
				grabaArchivo();
			} catch(IOException e) {
				System.out.println("Error en guardar");
			}
		}
		
		if(numBarras == 0) {
			walter.setVelY(2);
			walter.setMoviendo(true);
		}
		if(cargar){
			cargar = false;
			try {
			leeArchivo();
			} catch(IOException e) {
				System.out.println("Error en cargar");
			}
		}
		if (hank.getLives() == 1) {
			try {
				grabaArchivo();
			} catch(IOException e) {
				System.out.println("Error en guardar");
			}
		}
	}
	
	/**
	 * Metodo usado para checar las colisiones del objeto elefante y asteroid
	 * con las orillas del <code>Applet</code>.
	 */
	public void checaColision() {
//		Colision entre objetos
		if (walter.intersecta(bala)) {
			score += 3;
			bala.setX(-100);
			bala.setVelX(0);
			bala.setVelY(0);
			walter.setLives(walter.getLives()-1);
			if (sound) {
				choqueWalter.play();
			}
		}
		if (walter.getX() > 850 || walter.getX() < 0) {
			walter.setVelX(-1*walter.getVelX());
		}
		
		if (hank.intersecta(bala)) {
			if (bala.getY()<625) {
				bala.setVelY( -bala.getVelY());
				if (bala.getVelX()>0) {
					if(bala.getX() < hank.getX() + 20) {
						int a = bala.getVelX() / 2;
						bala.setVelX((a>2)?a:-2);
					} else if (bala.getX() > hank.getX() + 80) {
						bala.setVelX(2 * bala.getVelX());
					}
				} else {
					if(bala.getX() < hank.getX() + 20) {
						bala.setVelX(2 * bala.getVelX());
					} else if (bala.getX() > hank.getX() + 80) {
						int a = bala.getVelX() / 2;
						bala.setVelX((a<-2)?a:2);
					}
				}
				if (sound) {
					paredEscudo.play();
				}
			} else {
				hank.setLives(hank.getLives() - 1);
				bala.setX(hank.getX());
				bala.setY(610);
				bala.setVelX(0);
				bala.setVelY(0);
				if (sound) {
					choqueHank.play();
				}
			}
		}
		for (int i=0; i<numBarras; i++) {
			Base barra = barras.get(i);
			if (barra.intersecta(bala)) {
				if (barra.getX() < bala.getX() && barra.getX() + 95 > bala.getX()) {
					bala.setVelY( - bala.getVelY());
				} else {
					bala.setVelX( - bala.getVelX());
				}
				score += 1;
				barra.setLives(barra.getLives() - 1);
				if (sound) {
					choqueBarra.play();
				}
			}
		}
		
//		Colision con el Applet dependiendo a donde se mueve.
		if (bala.getX() > 930 || bala.getX() < 0) {
			bala.setVelX( - bala.getVelX());
			if (sound) {
				paredEscudo.play();
			}		
		}
		if (bala.getY() < 20) {
			bala.setVelY( - bala.getVelY());
			if (sound) {
				paredEscudo.play();
			}		
		}
		if (bala.getY() > 720) {
			hank.setLives(hank.getLives()-1);
			bala.setVelX(0);
			bala.setVelY(0);
			if (sound) {
				choqueHank.play();
			}
		}
	}
	
	/**
	 * Metodo <I>keyPressed</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar cualquier la tecla.
	 * @param e es el <code>evento</code> generado al presionar las teclas.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {			//Presiono flecha izquierda/a
			dir = 'l';
			hank.setMoviendo(true);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {	//Presiono flecha derecha/d
			dir = 'r';
			hank.setMoviendo(true);
		} else if (e.getKeyCode() == KeyEvent.VK_S) {		//Presiono tecla s / para quitar sonido
			sound = !sound;
		} else if (e.getKeyCode() == KeyEvent.VK_I) {
//			Mostrar/Quitar las instrucciones del juego
			if (estado == 2) {
				estado = 0;
			} else {
				estado = 2;
//				cargar=true;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_P) {	//Presiono tecla P para parar el juego en ejecuccion
			if (estado == 1) {
				estado = 0;
			} else {
				estado = 1;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (bala.getVelX() == 0) {
				bala.setVelX((int)(Math.random()*6)+3);
				bala.setVelY((int)(Math.random()*-3)-4);
				if (sound) {
					disparo.play();
				}
			}
		}
    }

    /**
	 * Metodo <I>keyReleased</I> sobrescrito de la interface <code>KeyListener</code>.<P>
	 * En este metodo maneja el evento que detiene el movimiento del walter.
	 * @param e es el <code>evento</code> que se genera al dejar de presionar la tecla izquierda o derecha.
	 */
	@Override
	public void keyReleased(KeyEvent e){
		//Si se deja de presionar la tecla izquierda el walter se dejara de mover, lo mismo sucede con la tecla derecha
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			dir = '.';
			hank.setMoviendo(false);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			dir = '.';
			hank.setMoviendo(false);
		}
	}
	@Override
	public void keyTyped(KeyEvent e){}
	
	public void leeArchivo() throws IOException {
//		Lectura del archivo el cual tiene las variables del juego guardado
		BufferedReader fileIn;
		try {
			fileIn = new BufferedReader(new FileReader("Guardado.txt"));
		} catch (FileNotFoundException e){
			File puntos = new File("Guardado.txt");
			PrintWriter fileOut = new PrintWriter(puntos);
			fileOut.println("100,demo");
			fileOut.close();
			fileIn = new BufferedReader(new FileReader("Guardado.txt"));
		}
		String 
                dato= fileIn.readLine();
		tiempoActual = (Long.parseLong(dato));
		dato = fileIn.readLine();
		hank.setX(Integer.parseInt(dato));
		dato =fileIn.readLine();
		hank.setY(Integer.parseInt(dato));
		dato = fileIn.readLine();
		walter.setX(Integer.parseInt(dato));
		dato = fileIn.readLine();
		estado = Integer.parseInt(dato);
		dato = fileIn.readLine();
		highestscore = Integer.parseInt(dato);

		fileIn.close();
	}
	
	public void grabaArchivo() throws IOException {
//		Grabar las variables necesarias para reiniciar el juego de donde se quedo el usuario en un txt llamado Guardado
		PrintWriter fileOut= new PrintWriter(new FileWriter("Guardado"));
		fileOut.println(String.valueOf(tiempoActual));
		fileOut.println(String.valueOf(hank.getX()));
		fileOut.println(String.valueOf(hank.getY()));
		fileOut.println(String.valueOf(walter.getX()));
		fileOut.println(String.valueOf(estado));
		if (score >= highestscore) {
			fileOut.println(String.valueOf(score));    
		} else {
			fileOut.println(String.valueOf(highestscore));
		}
		
		fileOut.close();
	}
	
	@Override
	public void paint(Graphics g) {
//		Inicializan el DoubleBuffer
		dbImage = createImage (this.getSize().width, this.getSize().height);
		dbg = dbImage.getGraphics ();

//		Actualiza la imagen de fondo.
		dbg.setColor(getBackground ());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

//		Actualiza el Foreground.
		dbg.setColor(getForeground());
		paint1(dbg);

//		Dibuja la imagen actualizada
		g.drawImage (dbImage, 0, 0, this);
	}
	
	/**
	 * Metodo <I>paint1</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo se dibuja la imagen con la posicion actualizada,
	 * ademas que cuando la imagen es cargada te despliega una advertencia.
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint1(Graphics g) {
		g.setFont(new Font("Helvetica", Font.PLAIN, 20));	// plain font size 20
		g.setColor(Color.white);							// black font
		
		if (hank != null && walter != null) {
//			Dibuja la imagen en la posicion actualizada
			g.drawImage(background, 0, 20, this);
			g.drawImage(sidebar.getImage(), sidebar.getX(), sidebar.getY(),this);
			for (int i=0; i<numBarras; i++) {
				Base barra = barras.get(i);
				if (barra.getLives() == 0) {
					barras.remove(i);
					numBarras-=1;
					i--;
				} else {
					g.drawImage(barra.getImage(barra.getLives()-1), barra.getX(),barra.getY(), this);
				}
			}
			
			if(estado == 0) {
//			Dibuja el estado corriendo del juego
				if (bala.getVelX() != 0) {
					g.drawImage(bala.getImage(), bala.getX(), bala.getY(), this);
				}
				if (walter.getMoviendo()) {
					g.drawImage(walter.getImage(), walter.getX(),walter.getY(), this);
				} else {
					g.drawImage(walter.getImage(0), walter.getX(),walter.getY(), this);
				}
				if (dir != '.') {
					g.drawImage(hank.getImage(), hank.getX(),hank.getY(), this);
				} else {
					g.drawImage(hank.getImage(0), hank.getX(),hank.getY(), this);
				}
				
				g.drawImage(vasovidas.getImage(hank.getLives()), vasovidas.getX(), vasovidas.getY(), this);
				g.drawString(String.valueOf(score), 1070,670);
//				g.drawString("Vidas: " + String.valueOf(hank.getLives()), 1000, 75);	// draw score at (1000,25)
			} else if (estado == 1) {
//				Dibuja el estado de pausa en el jframe
				g.drawImage(pausa.getImage(),pausa.getX(),pausa.getY(),this);
//				g.drawString("PAUSA", getWidth()/2 - 100, getHeight()/2);
			} else if (estado == 2) {
//				Dibuja el estado de informacion para el usuario en el jframe
				g.drawImage(instruc.getImage(),instruc.getX(),instruc.getY(),this);
				/*g.drawString("INSTRUCCIONES", getWidth()/2 - 210, 200);
				g.drawString("Para jugar debes mover a Hank con las", getWidth()/2 - 210, 250);
				g.drawString("teclas ← y →. Presiona la barra espaciadora", getWidth()/2 - 210, 280);
				g.drawString("para disparar, destruye todas anfetaminas y", getWidth()/2 - 210,310);
				g.drawString("luego enfrenta a Walter cara a cara.", getWidth()/2 - 210, 340);
				g.drawString("I - Instrucciones", getWidth()/2 - 210, 370);
				g.drawString("S - Sonido", getWidth()/2 - 210, 400);
				g.drawString("P - Pausa", getWidth()/2 - 210, 430);*/
			} else if (estado == 3) {
//				Dibuja el estado de creditos en el jframe
				if (hankDied) {
					g.drawImage(gameo.getImage(),gameo.getX(),gameo.getY(),this);
				} else {
					g.drawImage(gamew.getImage(),gamew.getX(),gamew.getY(),this);
				}
				/*g.setColor(new Color(78, 88, 93));
				g.fillRect(100, 100, getWidth() - 200, getHeight() - 200);
				g.setColor(Color.white);
				g.drawString("GAME OVER", getWidth()/2 - 210, 200);
				g.drawString("CREDITOS", getWidth()/2 - 210, 250);
				g.drawString("Andres Rodriguez    A00812121", getWidth()/2 - 210, 300);
				g.drawString("Alejandro Sanchez   A01191434", getWidth()/2 - 210, 350);
				g.drawString("Manuel Sañudo       A01192241", getWidth()/2 - 210, 400);*/
			}

		} else {
//			Da un mensaje mientras se carga el dibujo	
			g.drawString("No se cargo la imagen..", 20, 20);
		}
	}
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		FlappyGame examen1 = new FlappyGame();
		examen1.setVisible(true);
		examen1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}