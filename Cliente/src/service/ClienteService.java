/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import aplicacao.Mensagem;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LORD
 */
public class ClienteService {
    
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket conectar(){
        
        try {
            
            this.socket = new Socket("172.16.0.210", 5000); //IP do Servidor
            this.output = new ObjectOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return socket;
    }
    
    public void enviar(Mensagem mensagem){
        try {
            
            output.writeObject(mensagem);
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
