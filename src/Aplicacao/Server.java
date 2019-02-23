package Aplicacao;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import Entidades.AdivinhePalavra;

public class Server {
	public static void main(String[] args) throws Exception {
		try {
			ServerSocket server = new ServerSocket(1234);
			System.out.println("Servidor em rede");
			while (true) {
				Socket client = server.accept();
				Thread thread = new Thread(new AdivinhePalavra(client));
				thread.start();
			}
		} catch (BindException e) {
			throw new BindException("Servidor já em execução");
		}
	}
}