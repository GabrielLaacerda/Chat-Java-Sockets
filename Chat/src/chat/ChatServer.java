package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ChatServer {

    static ArrayList<String> listaUsuarios = new ArrayList<>(); //lista de usuários
    static ArrayList<PrintWriter> destinatarios = new ArrayList<>(); //lista de usuários que receberão as mensagens

    public static void main(String[] args) throws Exception {
       
        JOptionPane.showMessageDialog(null, "Aguardando novos Usuários");
        
        ServerSocket serverSocket = new ServerSocket(9086); //define uma nova conexão a partir de tal porta

        while (true) { //sempre aceita novas conexões
            Socket usuario = serverSocket.accept(); //conecta usuário
            JOptionPane.showMessageDialog(null, "Usuario Conectado"); //imprime no console que usuário foi conectado
            GerenciadorConversas  conversa = new GerenciadorConversas (usuario); //entra em uma thread de manipuação de conversa
            conversa.start();
        }
    }
}

class GerenciadorConversas extends Thread { //
    Socket usuarioConversa; //socket para o usuário
    BufferedReader entrada; //entrada de dados do cliente
    PrintWriter saida; //saida de dados do servidor
    String nome; //nome do usuário

    public GerenciadorConversas (Socket usuarioConversa) throws IOException {
        this.usuarioConversa = usuarioConversa;
    }

    @Override
    public void run() {
        try {
            entrada = new BufferedReader(new InputStreamReader(usuarioConversa.getInputStream())); //recebe valores do cliente
            saida = new PrintWriter(usuarioConversa.getOutputStream(), true); //envia valores para o cliente

            int contador = 0; //auxiliar para adicionar nomes usuários
            while (true) {
                if (contador > 0) {
                    saida.println("Nome_Existente"); //envia uma mensagem a ser tratada no cliente
                } else {
                    saida.println("Nome_Requerido"); //envia uma mensagem a ser tratada no cliente
                }
                nome = entrada.readLine();
                
                if (nome == null) {
                    return;
                }

                if (!ChatServer.listaUsuarios.contains(nome)) {
                    ChatServer.listaUsuarios.add(nome); //se o nome não existir na lista de usuários, adiciona
                    break;
                }
                contador++; //incrementa o contador de usuários
            }
            saida.println("Nome_Aceito" + nome);
            ChatServer.destinatarios.add(saida); //ao receber uma mensagem de um usuário, envia-a para esta lista

            while (true) {
                String msg = entrada.readLine(); //msg recebe a msg recebida pelo servidor

                if (msg == null) {
                    return;
                }
                for (PrintWriter writer : ChatServer.destinatarios) { //todos os destinatários recebem a msg
                    writer.println( nome + ": " + msg);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro no servidor:" + e.getMessage());
        }
    }
}
