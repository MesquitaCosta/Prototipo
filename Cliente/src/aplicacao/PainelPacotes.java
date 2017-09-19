/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import service.ClienteService;
import telas.BemVindo;
import telas.Parametros;


/**
 *
 * @author LORD
 */
public class PainelPacotes extends JPanel {

    DisplayMode monitor = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

    public JPanel painelPrincipal;
    public JPanel painelScroll;
    public PainelInterno painelInterno;
    public JScrollPane scroll;
    //public JDesktopPane painelDesktop;
    public JPanel painelParametros;
    public Parametros parametros;
    public ArrayList<JPanel> paineis = new ArrayList<>();
    //public JPanel painelJanela; Possivelmente inserir um recurso visual para mostrar a janela de recepção
    

    public PainelPacotes(Mensagem mensagem,ClienteService service) {

        painelPrincipal = new JPanel();              
        painelScroll = new JPanel();
        scroll = new JScrollPane();
        painelParametros = new JPanel();
        parametros = new Parametros(this, service, mensagem);
        
        painelPrincipal.setBackground(new java.awt.Color(0,102,51));
        painelScroll.setBackground(new java.awt.Color(0,51,0));
        scroll.setBackground(Color.BLACK);
        painelParametros.setBackground(new java.awt.Color(0,102,51));
        this.setBackground(new java.awt.Color(0,102,51));
        
        painelParametros.add(parametros);
        try {
            if (mensagem.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                scroll.setVisible(false);
            }
            else{
                painelParametros.setVisible(false);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PainelPacotes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //painelJanela = new JPanel();    
        
        //painelJanela = new JPanel();
             
        scroll.setPreferredSize(new Dimension(820, 600));
        painelParametros.setPreferredSize(new Dimension(600, 600));
                 
        this.add(painelPrincipal);
        painelPrincipal.add(scroll);
        painelPrincipal.add(painelParametros);
        scroll.add(painelScroll);
   
        //this.add(painelJanela); Possivelmente inserir um recurso visual para mostrar a janela de recepção

      
    }

    public JPanel novoPainel() {

        painelInterno = new PainelInterno();
        JPanel painelFlags = new JPanel();
        
        painelInterno.criarCampos(painelInterno, painelFlags);      

        scroll.setViewportView(painelScroll);
        return painelInterno;
    }
    
    
}
