package service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import aplicacao.Mensagem;
import aplicacao.Mensagem.Action;
import aplicacao.Pacote;

public class ServidorService {

    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String, ObjectOutputStream> usuariosOnlines = new HashMap<String, ObjectOutputStream>();

    public ServidorService() {

        try {
            serverSocket = new ServerSocket(5000);

            System.out.println("Servidor Ativo");

            while (true) {
                socket = serverSocket.accept();

                new Thread(new ListenerSocket(socket)).start();
            }
        } catch (IOException ex) {

        }
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream input;
        private ObjectOutputStream output;

        public ListenerSocket(Socket socket) {

            try {

                input = new ObjectInputStream(socket.getInputStream());
                output = new ObjectOutputStream(socket.getOutputStream());

            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {

            Mensagem mensagem = null;

            try {
                while ((mensagem = (Mensagem) input.readObject()) != null) {

                    Action action = mensagem.getAction();

                    if (action.equals(Action.CONECTAR)) {

                        boolean isConnect = conectar(mensagem, output);

                        if (isConnect) {

                            usuariosOnlines.put(mensagem.getIpOrigem(), output);
                            listaOnlines();
                        }

                    } else if (action.equals(Action.DISCONECTAR)) {

                        disconectar(mensagem, output);
                        listaOnlines();
                        return; //força a saída do while pra não estourar exceção. Senão qdo fechar o socket, o while dá pau, pois não tem object para ser lido

                    } else if (action.equals(Action.ENVIAR)) {
                        enviarMensagem(mensagem);
                    }
                }
            } catch (IOException ex) {

                disconectar(mensagem, output);
                listaOnlines();
                System.out.println(mensagem.getIpOrigem() + " saiu do sistema");

            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private boolean conectar(Mensagem mensagem, ObjectOutputStream output) {

        if (usuariosOnlines.size() == 0) {

            mensagem.setTexto("YES");

            enviar(mensagem, output);

            return true;
        }

        for (Map.Entry<String, ObjectOutputStream> kv : usuariosOnlines.entrySet()) {

            if (kv.getKey().equals(mensagem.getIpOrigem())) {

                mensagem.setTexto("NO");
                enviar(mensagem, output);

                return false;

            } else {

                mensagem.setTexto("YES");
                enviar(mensagem, output);

                return true;
            }

        }
        return false;
    }

    private void disconectar(Mensagem mensagem, ObjectOutputStream output) {

        usuariosOnlines.remove(mensagem.getIpOrigem());

        mensagem.setTexto("Falô otário");

        mensagem.setAction(Action.ENVIAR);

        System.out.println("Usuário: " + mensagem.getIpOrigem() + " saiu");

    }

    private void enviar(Mensagem mensagem, ObjectOutputStream output) {

        try {

            output.writeObject(mensagem);

        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void enviarMensagem(Mensagem mensagem) {

        for (Map.Entry<String, ObjectOutputStream> kv : usuariosOnlines.entrySet()) {
            
            if (kv.getKey().equals(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getIpDestino())) {
                try {

                    kv.getValue().writeObject(mensagem);

                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void listaOnlines() {

        Set<String> setUsuarios = new HashSet<String>();
        for (Map.Entry<String, ObjectOutputStream> kv : usuariosOnlines.entrySet()) {

            setUsuarios.add(kv.getKey());
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setAction(Action.USUARIOS_ONLINE);
        mensagem.setUsuariosOnline(setUsuarios);

        for (Map.Entry<String, ObjectOutputStream> kv : usuariosOnlines.entrySet()) {
            
            mensagem.setIpOrigem(kv.getKey());//adicionado o IP para o REMOVE funcionar na lista(antes de transforma-la em array)
            
            try {

                System.out.println("Endereço IP -- " + kv.getKey());
                kv.getValue().writeObject(mensagem);

            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
    
    
    
    
    private int retornaUltimoPacote(Mensagem mensagem) {

        return (mensagem.getPacotes().size() - 1);

    }
}
