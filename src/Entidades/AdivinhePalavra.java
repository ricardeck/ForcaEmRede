package Entidades;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class AdivinhePalavra implements Runnable {
	Socket client;
	String imp;
	String[] palavras = { "futebol", "capoeira", "samba", "carnaval", "acaraje" };
	String[] dicas = { "Esporte mais popular do Brasil", "Luta marcial brasileira", "Ritmo matriz da Bolsa Nova",
			"Festa popular", "Culinaria tipica da Bahia" };
	String palvFormada;
	ArrayList<Character> LetrasDigitadas = new ArrayList<Character>();
	int n;
	int tentativas;

	public AdivinhePalavra(Socket client) {
		this.client = client;
		Random r = new Random();
		n = r.nextInt(5);
		tentativas = 2;
		this.getFraseCliente();
	}

	@Override
	public void run() {
		try {
			Scanner in = new Scanner(this.client.getInputStream());
			PrintWriter out = new PrintWriter(this.client.getOutputStream(), true);
			out.println(this.dicas[n]);

			while (true) {
				char c = (in.nextLine().toLowerCase().charAt(0));
				String s = this.verificaPalavra(c);
				if (LetrasDigitadas.contains(c)) {
					imp = "Letra já digitada - ";
					out.print(imp);
				}
				else if (!LetrasDigitadas.contains(c)) {
					LetrasDigitadas.add(c);
				}
				if (tentativas <= 0) {
					out.println("Voce perdeu");
					out.println("A palavra era " + palavras[n].toUpperCase());
					break;
				}
				if (s.equals(this.palavras[n])) {
					out.println("Voce ganhou");
					break;
				} else {
					imp = s + " Tentativas: " + tentativas + " " + LetrasDigitadas.toString();
					out.println(imp);
				}
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getFraseCliente() {
		char[] palavra = new char[this.palavras[n].length()];
		for (int i = 0; i < this.palavras[n].length(); i++) {
			palavra[i] = '_';
		}
		this.palvFormada = new String(palavra);
	}

	public String verificaPalavra(char c) {
		boolean acertou = false;
		char[] palavra = this.palavras[n].toCharArray();
		char[] newPalvr = this.palvFormada.toCharArray();

		for (int i = 0; i < this.palavras[n].length(); i++) {
			if (c == palavra[i]) {
				newPalvr[i] = c;
				acertou = true;
			}
		}
		if (!acertou && !LetrasDigitadas.contains(c))
			tentativas--;
		this.palvFormada = new String(newPalvr);
		return palvFormada;
	}

}