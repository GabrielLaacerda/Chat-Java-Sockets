package chat;

public class ChatClient {

    public static void main(String[] args) throws Exception {
        chatCli chat = new chatCli();
        chat.setVisible(true);
        chat.setLocationRelativeTo(null);
        chat.setResizable(false);
        chat.setTitle("Chat Sistemas Distribuidos");

        chat.iniciarChat();
    }
}

