/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import aplicacao.Mensagem;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import telas.TelaParametros;

/**
 *
 * @author LORD
 */
public class ListenerSocket implements Runnable {

    private ObjectInputStream input;

    public ListenerSocket(Socket socket) {

        try {

            this.input = new ObjectInputStream(socket.getInputStream());

        } catch (IOException ex) {
            Logger.getLogger(TelaParametros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Mensagem mensagem = null;

        try {
            while ((mensagem = (Mensagem) input.readObject()) != null) {

                Mensagem.Action action = mensagem.getAction();

                if (action.equals(Mensagem.Action.CONECTAR)) {
                    //Qdo clicar em enviar mensagem e o ID da mensagem for igual a 1
                    conectar(mensagem);

                } else if (action.equals(Mensagem.Action.DISCONECTAR)) {
                    disconectar(mensagem);

                } else if (action.equals(Mensagem.Action.ENVIAR)) {
                    receber(mensagem);

                } else if (action.equals(Mensagem.Action.USUARIOS_ONLINE)) {
                    usuariosOnline(mensagem);

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TelaParametros.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TelaParametros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void conectar(Mensagem mensagem){
        
    }
    
    private void disconectar(Mensagem mensagem){
        
    }
    
    private void receber(Mensagem mensagem){
        preencherCampos(mensagem);
    }
    
    public void usuariosOnline(Mensagem mensagem){
        Set<String> usuarios = mensagem.getUsuariosOnline();
        
        String[] array = (String[]) usuarios.toArray(new String[usuarios.size()]); //Componente Jlist só aceita String
        
    }

    private void preencherCampos(Mensagem mensagem){
        
        
    }
}
