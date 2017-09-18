/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
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
public class PainelPacote extends JPanel {

    public JPanel painelPrincipal;
    public JPanel painelScroll;
    public JPanel painelInterno;
    public JScrollPane scroll;
    public JDesktopPane painelDesktop;
    public JInternalFrame painelParametros;
    public Parametros parametros;
    //public JPanel painelJanela; Possivelmente inserir um recurso visual para mostrar a janela de recepção

    JLabel lbl1;
    JLabel lbl2;
    JLabel lbl3;
    JLabel lbl4;
    JLabel lbl5;
    JLabel lbl6;
    JLabel lbl7;
    JLabel lbl8;
    //JLabel lbl9 = new JLabel(" ");
    JLabel lbl10;
    JLabel lbl11;
    JLabel lbl12;
    JLabel lbl13;
    JLabel lbl14;
    JLabel lbl15;
    JLabel lbl16;

    JLabel lblIpOrigemX;
    JLabel lblPortaOrigemX;
    JLabel lblNumeroSequenciaX;
    JLabel lblJanelaRecpcaoX;
    JLabel lblIpDestinoX;
    JLabel lblPortaDestinoX;
    JLabel lblNumeroRecepcaoX;
    JLabel lblTamanhoCabecalhoX;
    JLabel lblDadosX;
    JLabel lblChecsumX;
    JLabel lblOptionsX;
    JLabel lblFLAGS;

    JCheckBox URG;
    JCheckBox PSH;
    JCheckBox SYN;
    JCheckBox ACK;
    JCheckBox RST;
    JCheckBox FIN;

    JTextField txtIpOrigemX;
    JTextField txtPortaOrigemX;
    JTextField txtNumeroSequenciaX;
    JTextField txtJanelaRecepcaoX;
    JTextField txtIpDestinoX;
    JTextField txtPortaDestinoX;
    JTextField txtNumeroRecepcaoX;
    JTextField txtTamanhoCabecalhoX;
    JTextField txtDadosX;
    JTextField txtChecsumX;
    JTextField txtOptionsX;
    public JButton btnAceitar;
    public JButton btnRecusar;
    public JProgressBar barraProgresso;

    public PainelPacote(Mensagem mensagem,ClienteService service) {

        painelPrincipal = new JPanel();
        painelScroll = new JPanel();
        scroll = new JScrollPane();
        painelDesktop = new JDesktopPane();
        painelDesktop.setLayout(null);
        painelParametros = new JInternalFrame();
        parametros = new Parametros(this, service, mensagem);
        painelParametros.add(parametros);
        //painelJanela = new JPanel();
        

             
        painelDesktop.setPreferredSize(new Dimension(600, 600));      
        scroll.setPreferredSize(new Dimension(820, 600));
        
        painelPrincipal.setBackground(new java.awt.Color(0,102,51));
        painelPrincipal.setPreferredSize(new Dimension(820, 800)); 
        painelDesktop.setOpaque(false);
        painelDesktop.add(painelParametros);
        painelParametros.setVisible(true);
        painelParametros.pack();
        
        painelPrincipal.add(scroll);
        scroll.add(painelScroll);
   
        //this.add(painelJanela); Possivelmente inserir um recurso visual para mostrar a janela de recepção
        
        this.add(painelPrincipal);
        this.add(painelDesktop);        
        this.setBackground(new java.awt.Color(0,102,51));
        
        //painel.add(scroll);
        /*painel.add(lbl1);
        painel.add(lbl2);
        painel.add(lbl3);
        painel.add(lbl4);*/
        //UTILIZAR UM FOR PARA RODAR O HASHMAP PARA VER QUAL O PACOTE
        ////////////PREENCHENDO PAINEL DE BAIXO/////// 
        /*
        SYN.setSelected(true);
        txtIpOrigemX.setText(mensagem.getIpOrigem());      
        txtPortaOrigemX.setText("5000");
        txtPortaDestinoX.setText("5000");
        mensagem.setAction(Action.ENVIAR);
        this.service.enviar(mensagem);*/
    }

    public JPanel novoPainel() {

        painelInterno = new JPanel();

        JPanel painelFlags = new JPanel();

        criarCampos(painelInterno, painelFlags);

        scroll.setViewportView(painelScroll);
        return painelInterno;
    }
    
    public void criarCampos(JPanel painel, JPanel painelFlags) {

        lbl1 = new JLabel("");
        lbl2 = new JLabel("");
        lbl3 = new JLabel("");
        lbl4 = new JLabel("");
        lbl5 = new JLabel(" ");
        lbl6 = new JLabel(" ");
        lbl7 = new JLabel(" ");
        lbl8 = new JLabel(" ");
        //JLabel lbl9 = new JLabel(" ");
        lbl10 = new JLabel(" ");
        lbl11 = new JLabel(" ");
        lbl12 = new JLabel(" ");
        lbl13 = new JLabel(" ");
        lbl14 = new JLabel(" ");
        lbl15 = new JLabel(" ");
        lbl16 = new JLabel(" ");

        lblIpOrigemX = new JLabel("IP de Origem");
        lblPortaOrigemX = new JLabel("Porta Origem");
        lblNumeroSequenciaX = new JLabel("Número de Sequência");
        lblJanelaRecpcaoX = new JLabel("Janela Recepção");
        lblIpDestinoX = new JLabel("IP de Destino");
        lblPortaDestinoX = new JLabel("Porta Destino");
        lblNumeroRecepcaoX = new JLabel("Aviso de Recepção");
        lblTamanhoCabecalhoX = new JLabel("Tamanho Cabeçalho");
        lblDadosX = new JLabel("Dados");
        lblChecsumX = new JLabel("Cheksum");
        lblOptionsX = new JLabel("Options");
        lblFLAGS = new JLabel("FLAGS");

        Font fonte = new Font("Tahoma", Font.LAYOUT_LEFT_TO_RIGHT,8);
        URG = new JCheckBox("URG");
        URG.setFont(fonte);
        URG.setIconTextGap(0);
        URG.setEnabled(false);
        
        PSH = new JCheckBox("PSH");
        PSH.setFont(fonte);
        PSH.setIconTextGap(0);
        PSH.setEnabled(false);
        
        SYN = new JCheckBox("SYN");
        SYN.setFont(fonte);
        SYN.setIconTextGap(0);
        SYN.setEnabled(false);
        
        ACK = new JCheckBox("ACK");
        ACK.setFont(fonte);
        ACK.setIconTextGap(0);
        ACK.setEnabled(false);
        
        RST = new JCheckBox("RST");
        RST.setFont(fonte);
        RST.setIconTextGap(0);
        RST.setEnabled(false);
        
        FIN = new JCheckBox("FIN");
        FIN.setFont(fonte);
        FIN.setIconTextGap(0);
        FIN.setEnabled(false);

        txtIpOrigemX = new JTextField();
        txtPortaOrigemX = new JTextField();
        txtNumeroSequenciaX = new JTextField();
        txtJanelaRecepcaoX = new JTextField();
        txtIpDestinoX = new JTextField();
        txtPortaDestinoX = new JTextField();
        txtNumeroRecepcaoX = new JTextField();
        txtTamanhoCabecalhoX = new JTextField();
        txtDadosX = new JTextField();
        txtChecsumX = new JTextField();
        txtOptionsX = new JTextField();
        btnAceitar = new JButton();
        btnRecusar = new JButton();
        barraProgresso = new JProgressBar();

        painel.setPreferredSize(new Dimension(800, 230));
        painel.setBackground(new java.awt.Color(102, 153, 102));
        painel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 12));

        //FlowLayout layout = new FlowLayout();
        //layout.setAlignment(FlowLayout.CENTER);
        btnAceitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/Aceitar.png")));
        btnRecusar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/Recusar.png")));

        painelFlags.setLayout(new GridLayout(2, 3));
        painelFlags.add(URG);
        painelFlags.add(PSH);
        painelFlags.add(SYN);
        painelFlags.add(ACK);
        painelFlags.add(RST);
        painelFlags.add(FIN);

        painel.setLayout(new GridLayout(6, 7, 3, 3));

        painel.add(lblIpOrigemX);
        painel.add(lblPortaOrigemX);
        painel.add(lblNumeroSequenciaX);
        painel.add(lblJanelaRecpcaoX);
        painel.add(lbl1);
        painel.add(lbl2);
        painel.add(lbl3);

        painel.add(txtIpOrigemX);
        painel.add(txtPortaOrigemX);
        painel.add(txtNumeroSequenciaX);
        painel.add(txtJanelaRecepcaoX);
        painel.add(lbl4);
        painel.add(lbl5);
        painel.add(lbl6);

        painel.add(lblIpDestinoX);
        painel.add(lblPortaDestinoX);
        painel.add(lblNumeroRecepcaoX);
        painel.add(lblTamanhoCabecalhoX);
        painel.add(lbl7);
        painel.add(btnAceitar);
        painel.add(btnRecusar);

        painel.add(txtIpDestinoX);
        painel.add(txtPortaDestinoX);
        painel.add(txtNumeroRecepcaoX);
        painel.add(txtTamanhoCabecalhoX);
        painel.add(lbl8);
        painel.add(barraProgresso);
        painel.add(lbl10);

        painel.add(lblFLAGS);
        painel.add(lblDadosX);
        painel.add(lblChecsumX);
        painel.add(lblOptionsX);
        painel.add(lbl11);
        painel.add(lbl12);
        painel.add(lbl13);

        painel.add(painelFlags);
        painel.add(txtDadosX);
        painel.add(txtChecsumX);
        painel.add(txtOptionsX);
        painel.add(lbl14);
        painel.add(lbl15);
        painel.add(lbl16);

    }

    public JCheckBox getURG() {
        return URG;
    }

    public void setURG(JCheckBox URG) {
        this.URG = URG;
    }

    public JCheckBox getPSH() {
        return PSH;
    }

    public void setPSH(JCheckBox PSH) {
        this.PSH = PSH;
    }

    public JCheckBox getSYN() {
        return SYN;
    }

    public void setSYN(JCheckBox SYN) {
        this.SYN = SYN;
    }

    public JCheckBox getACK() {
        return ACK;
    }

    public void setACK(JCheckBox ACK) {
        this.ACK = ACK;
    }

    public JCheckBox getRST() {
        return RST;
    }

    public void setRST(JCheckBox RST) {
        this.RST = RST;
    }

    public JCheckBox getFIN() {
        return FIN;
    }

    public void setFIN(JCheckBox FIN) {
        this.FIN = FIN;
    }

    public JTextField getTxtIpOrigemX() {
        return txtIpOrigemX;
    }

    public void setTxtIpOrigemX(String txtIpOrigemX) {
        this.txtIpOrigemX.setText(txtIpOrigemX);
    }

    public JTextField getTxtPortaOrigemX() {
        return txtPortaOrigemX;
    }

    public void setTxtPortaOrigemX(JTextField txtPortaOrigemX) {
        this.txtPortaOrigemX = txtPortaOrigemX;
    }

    public String getTxtNumeroSequenciaX() {
        return String.valueOf(txtNumeroSequenciaX);
    }

    public void setTxtNumeroSequenciaX(String txtNumeroSequenciaX) {
        this.txtNumeroSequenciaX.setText(txtNumeroSequenciaX);
    }

    public JTextField getTxtJanelaRecepcaoX() {
        return txtJanelaRecepcaoX;
    }

    public void setTxtJanelaRecepcaoX(String txtJanelaRecepcaoX) {
        this.txtJanelaRecepcaoX.setText(txtJanelaRecepcaoX);
    }

    public JTextField getTxtIpDestinoX() {
        return txtIpDestinoX;
    }

    public void setTxtIpDestinoX(String txtIpDestinoX) {
        this.txtIpDestinoX.setText(txtIpDestinoX);
    }

    public JTextField getTxtPortaDestinoX() {
        return txtPortaDestinoX;
    }

    public void setTxtPortaDestinoX(JTextField txtPortaDestinoX) {
        this.txtPortaDestinoX = txtPortaDestinoX;
    }

    public JTextField getTxtNumeroRecepcaoX() {
        return txtNumeroRecepcaoX;
    }

    public void setTxtNumeroRecepcaoX(String txtNumeroRecepcaoX) {
        this.txtNumeroRecepcaoX.setText(txtNumeroRecepcaoX);
    }

    public JTextField getTxtTamanhoCabecalhoX() {
        return txtTamanhoCabecalhoX;
    }

    public void setTxtTamanhoCabecalhoX(String txtTamanhoCabecalhoX) {
        this.txtTamanhoCabecalhoX.setText(txtTamanhoCabecalhoX);
    }

    public JTextField getTxtDadosX() {
        return txtDadosX;
    }

    public void setTxtDadosX(String txtDadosX) {
        this.txtDadosX.setText(txtDadosX);
    }

    public JTextField getTxtChecsumX() {
        return txtChecsumX;
    }

    public void setTxtChecsumX(String txtChecsumX) {
        this.txtChecsumX.setText(txtChecsumX);
    }

    public String getTxtOptionsX() {
        return txtOptionsX.getText();
    }

    public void setTxtOptionsX(String txtOptionsX) {
        this.txtOptionsX.setText(txtOptionsX);
    }

    public JButton getBtnAceitar() {
        return btnAceitar;
    }

    public void setBtnAceitar(JButton btnIconeAceitar) {
        this.btnAceitar = btnIconeAceitar;
    }

    public JButton getBtnRecusar() {
        return btnRecusar;
    }

    public void setBtnRecusar(JButton btnRecusar) {
        this.btnRecusar = btnRecusar;
    }

    public JProgressBar getBarraProgresso() {
        return barraProgresso;
    }

    public void setBarraProgresso(JProgressBar barraProgresso) {
        this.barraProgresso = barraProgresso;
    }

}
