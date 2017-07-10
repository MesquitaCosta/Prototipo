/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import aplicacao.Mensagem;
import aplicacao.Mensagem.Action;
import aplicacao.Pacote;
import aplicacao.PainelPacote;
import com.sun.org.apache.bcel.internal.generic.BREAKPOINT;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import service.ClienteService;
import javax.swing.Timer;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author LORD
 */
public class BemVindo extends javax.swing.JFrame {

    public BemVindo() {

        super("FlowLayout");
        initComponents();

        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        /*
        pnlComunicacao.setVisible(false);
        pnlComunicacao1.setVisible(false);
        pnlComunicacao2.setVisible(false);
         */
        btnSair.setEnabled(false);
        listDispositivos.setEnabled(false);

        try {
            txtIpOrigem.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable {

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

                    Action action = mensagem.getAction();

                    if (action.equals(Action.CONECTAR)) {
                        //Qdo clicar em enviar mensagem e o ID da mensagem for igual a 1
                        conectou(mensagem);

                    } else if (action.equals(Action.DISCONECTAR)) {
                        disconectar();
                        socket.close();

                    } else if (action.equals(Action.ENVIAR)) {
                        receber(mensagem);
                        System.out.println(mensagem.getPacotes().size());
                    } else if (action.equals(Action.USUARIOS_ONLINE)) {

                        usuariosOnline(mensagem);
                    }
                }
            } catch (IOException ex) {
                disconectar();
                Logger.getLogger(TelaParametros.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TelaParametros.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void conectou(Mensagem mensagem) {

        if (mensagem.getTexto().equals("NO")) {

            //this.mensagem.setNome("");
            //this.txtNome.setText("");
            JOptionPane.showMessageDialog(this, "Conexão não estabelecida \nTente novamente");

            return;
        }

        this.mensagem = mensagem;
        this.btnIniciarCliente.setEnabled(false);
        this.listDispositivos.setEnabled(true);
        this.btnSair.setEnabled(true);
        //this.panelInformacoes.setVisible(true);

        txtIpOrigem.setText(mensagem.getIpOrigem());
        //chamar a tela de usuários conectados
        //Campos recebendo suas Respectivas variáveis

        //this.mensagem.setNome(txtNome.getText());
        JOptionPane.showMessageDialog(this, "Conexão estabelecida com o Sistema");
    }

    private void disconectar() {

        JOptionPane.showMessageDialog(this, "Obrigado por utilizar");
        this.listDispositivos.setEnabled(false);
        this.btnIniciarCliente.setEnabled(true);
    }

    private void receber(Mensagem mensagem) {

        this.mensagem = mensagem;

        verificarACK(mensagem);

        if (JtabAbas.getTabCount() > (mensagem.getIdMensagem())) {

            JtabAbas.setSelectedIndex(mensagem.getIdMensagem());
            
            
            for(int i = mensagem.getPacotes().size(); i>2; i--){
                try {
                    if(mensagem.getPacotes().get((i-1)).getIpOrigem().equals((InetAddress.getLocalHost().getHostAddress()))){
                        
                        while(i<mensagem.getPacotes().size()){
                                               
                            adicionarNovoPainel(aba, mensagem.getPacotes().get(i));
                            i++;
                        }
                        break;
                    }
                } catch (UnknownHostException ex) {
                    Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            

        } else {

            aba = new PainelPacote(mensagem, service);
            JtabAbas.addTab("Mensagem " + mensagem.getIpOrigem(), adicionarNovoPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem))));
            JtabAbas.setSelectedIndex(mensagem.getIdMensagem());
            aba.setTxtOptionsX(String.valueOf(mensagem.getMssEmissor()));
        }
    }

    private void verificarACK(Mensagem mensagem) {

        if (mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).isAceito()) {
            for (Pacote pacote : mensagem.getPacotes()) {
                if ((mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroAvisoRecepcao() - 1) == pacote.getNumeroSequencia()) {
                    showMessageDialog(null, "Recebimento do Pacote NumSeq: " + pacote.getNumeroSequencia() + " foi CONFIRMADO");

                }
            }
        }

    }

    private void usuariosOnline(Mensagem mensagem) {

        System.out.println(mensagem.getUsuariosOnline().toString());

        Set<String> usuarios = mensagem.getUsuariosOnline();

        usuarios.remove(mensagem.getIpOrigem());

        String[] array = (String[]) usuarios.toArray(new String[usuarios.size()]); //Componente Jlist só aceita String
        listDispositivos.setListData(array);
        listDispositivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Aceita selecionar apenas 1
        listDispositivos.setLayoutOrientation(JList.VERTICAL); //Nomes aparecem um abaixo do outro

    }

    private void solicitaConexao() {

        if (this.listDispositivos.getSelectedIndex() > -1) {

            mensagem.setIpDestino(this.listDispositivos.getSelectedValue());
            mensagem.setIdMensagem(mensagem.getIdMensagem() + 1);
            setarValores();
            this.listDispositivos.clearSelection();

        } else {

            showMessageDialog(this, "Seleciona algum, idiota");
        }

    }

    private void setarValores() {

        try {
            if (mensagem.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                pacote = new Pacote();
                pacote.setIpOrigem(mensagem.getIpOrigem());
                pacote.setIpDestino(mensagem.getIpDestino());

                pacote.setPortaOrigem((short) 5000);
                pacote.setPortaDestino((short) 5000);

                if (mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroAvisoRecepcao() == 0) {
                    pacote.setNumeroSequencia(valorAleatorio());
                    pacote.setAceito(false);

                } else if (mensagem.getPacotes().size() <= 3) {
                    pacote.setNumeroSequencia(mensagem.getPacotes().get(retornaUltimoPacote(mensagem) - 1).getNumeroSequencia() + 1);
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia() + 1);

                } else if (mensagem.getPacotes().size() == 4) {
                    pacote.setNumeroSequencia(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia());
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroAvisoRecepcao());
                    //contendo os dados

                } else {
                    if (mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                        pacote.setNumeroSequencia(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia() + defineTamanhoSegmento());
                    } else {
                        pacote.setNumeroSequencia(mensagem.getPacotes().get(retornaUltimoPacote(mensagem) - 1).getNumeroSequencia() + defineTamanhoSegmento());
                        System.out.println("TAMANHO MSS: " + defineTamanhoSegmento());
                    }
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia() + 1);
                }

                mensagem.getPacotes().add(pacote);

            } else {
                pacote = new Pacote();
                pacote.setIpOrigem(mensagem.getIpDestino());
                pacote.setIpDestino(mensagem.getIpOrigem());
                pacote.setPortaOrigem((short) 5000);
                pacote.setPortaDestino((short) 5000);

                if (mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroAvisoRecepcao() == 0) {
                    pacote.setNumeroSequencia(valorAleatorio());

                } else {
                    pacote.setNumeroSequencia(mensagem.getPacotes().get(retornaUltimoPacote(mensagem) - 1).getNumeroSequencia() + 1);

                }
                pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia() + 1);
                mensagem.getPacotes().add(pacote);
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int defineTamanhoSegmento() {

        if (mensagem.getMssEmissor() >= mensagem.getMssReceptor()) {

            return mensagem.getMssReceptor();
        } else {
            return mensagem.getMssEmissor();
        }
    }

    private int quantidadePacotes() {

        return (mensagem.getTamanhoMensagem() / defineTamanhoSegmento());
    }

    private void enviarPacotes() {
        System.out.println("MSS EMISSOR: " + mensagem.getMssEmissor());
        System.out.println("MSS Receptor: " + mensagem.getMssReceptor());
        System.out.println("MSS Menor: " + defineTamanhoSegmento());

        System.out.println("Quantidade de Pacotes: " + quantidadePacotes());
        if (this.count == (quantidadePacotes())) {

            //Finaliza e Exibi a mensagem
        } else {

            short janelaRecepção = mensagem.getPacotes().get(retornaUltimoPacote(mensagem) - 1).getTamanhoJanela();

            while (janelaRecepção > 0) {
                setarPaineis();
                janelaRecepção--;
                this.count++;
                
            }
        }
        this.mensagem.setAction(Action.ENVIAR);
        this.service.enviar(mensagem);
        System.out.println("Pacotes: " + mensagem.getPacotes().size() + "/n Count: " + this.count);
    }

    private void setarPaineis() {

        setarValores();
        mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).setAceito(false);

        adicionarNovoPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem)));

    }

    private int valorAleatorio() {
        Random gerador = new Random();
        return gerador.nextInt(11700621) + 40700621;
    }

    private int retornaUltimoPacote(Mensagem mensagem) {

        return (mensagem.getPacotes().size() - 1);
    }

    private int retornaUltimoPainel(Mensagem mensagem) {

        return (paineis.size() - 1);
    }

    private int DadosRestantes() {

        return (mensagem.getTamanhoMensagem() - defineTamanhoSegmento());
    }

    private void aceitarPacote() {

        this.pacote.setAceito(true);

        setarValores();

        //this.pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia() + 1);
        mensagem.setAction(Action.ENVIAR);
        this.service.enviar(mensagem);
    }

    private JPanel adicionarNovoPainel(PainelPacote painel, Pacote pacote) {

        painel.painelScroll.setLayout(new GridLayout(100, 1));
        painel.painelScroll.add(BorderLayout.NORTH, painel.novoPainel());
        //preencherPainel(painel);
        preencherPainel(painel, pacote);
        painel.painelScroll.revalidate();
        painel.painelScroll.repaint();

        acionarBotaoAceitar(painel);

        paineis.add(painel);
        return (painel);
    }

    private void adicionarPainelBranco(PainelPacote painel) {
        painel.painelScroll.add(painel.novoPainelVazio());
        painel.painelScroll.revalidate();
        painel.painelScroll.repaint();
    }

    
    private void preencherPainel(PainelPacote painel, Pacote pacote){
        
        if (paineis.size() >= 2) {
            painel.setTxtOptionsX("-----");
            painel.setTxtDadosX(String.valueOf(defineTamanhoSegmento()));
        }
        
        painel.setTxtIpOrigemX(pacote.getIpOrigem());
        painel.setTxtIpDestinoX(pacote.getIpDestino());
        painel.setTxtNumeroSequenciaX(String.valueOf(pacote.getNumeroSequencia()));
        painel.setTxtNumeroRecepcaoX(String.valueOf(pacote.getNumeroAvisoRecepcao()));
        painel.setTxtJanelaRecepcaoX(String.valueOf(pacote.getTamanhoJanela()));
        
        
        try {
            if (pacote.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                painel.getBtnAceitar().setVisible(false);
                painel.getBtnRecusar().setVisible(false);
                painel.painelInterno.setBorder(BorderFactory.createLineBorder(new java.awt.Color(153, 255, 153), 5));
                carregarBarraprogresso(painel);

                timer.start();

                if (mensagem.getPacotes().size() == 2) {

                    painel.getSYN().setSelected(true);
                } else {

                    painel.getACK().setSelected(true);
                }
            } else {

                painel.getBarraProgresso().setVisible(false);
                painel.painelInterno.setBorder(BorderFactory.createLineBorder(new java.awt.Color(255, 102, 102), 5));

                if (mensagem.getPacotes().size() == 2) {
                    painel.getSYN().setSelected(true);

                } else if (mensagem.getPacotes().size() == 3) {
                    painel.getSYN().setSelected(true);
                    painel.getACK().setSelected(true);
                    painel.setTxtOptionsX(String.valueOf(mensagem.getMssReceptor()));
                } else {
                    painel.getACK().setSelected(true);
                    painel.setTxtDadosX(String.valueOf(defineTamanhoSegmento()));
                    painel.setTxtOptionsX("------");
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }

    } 
    

    private void carregarBarraprogresso(PainelPacote painel) {

        atualizaBarra = new ActionListener() {
            public void actionPerformed(ActionEvent evento) {
                if (painel.getBarraProgresso().getValue() < 100) {
                    painel.getBarraProgresso().setValue(painel.getBarraProgresso().getValue() + 1);
                } else {
                    timer.stop();
                    //System.out.println("Tempo Finalizado");
                }
            }
        };
        timer = new Timer(300, atualizaBarra);
    }

    private void acionarBotaoAceitar(PainelPacote painel) {
        painel.getBtnAceitar().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceitarActionPerformed(evt);
            }
        });
    }

    private void btnAceitarActionPerformed(java.awt.event.ActionEvent evt) {

        if (paineis.size() == 1) {
            setarValores();
            aba.painelDesktop.setVisible(true);
            adicionarNovoPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem)));
        } else if (paineis.size() <= 3) {

            setarValores();

            mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).setAceito(true);

            adicionarNovoPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem)));

            if (paineis.size() == 3) {
                enviarPacotes();
                //o primeiro pacote tem de ser igual ao ultimo NumSEQ que enviei com ACK
            }
        } else {

            enviarPacotes();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JtabAbas = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        pnlInicio = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        btnIniciarCliente = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        panelInformacoes = new javax.swing.JPanel();
        lblIpOrigem = new javax.swing.JLabel();
        txtIpOrigem = new javax.swing.JTextField();
        lblPorta = new javax.swing.JLabel();
        txtPortaOrigem = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listDispositivos = new javax.swing.JList<>();
        btnThreeWayHandshake = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        pnlComunicacao = new javax.swing.JPanel();
        pnlSegmento = new javax.swing.JPanel();
        txtIpDestino1 = new javax.swing.JFormattedTextField();
        lblIpDestino2 = new javax.swing.JLabel();
        lblPortaDestino2 = new javax.swing.JLabel();
        txtTamanhoDadosPacote1 = new javax.swing.JTextField();
        txtJanelaRecepcao1 = new javax.swing.JFormattedTextField();
        lblTamanhoDadosPacote2 = new javax.swing.JLabel();
        txtPortaDestino1 = new javax.swing.JFormattedTextField();
        lblJanelaRecepcao2 = new javax.swing.JLabel();
        lblTamanhoCabecalho2 = new javax.swing.JLabel();
        chkSYN1 = new javax.swing.JCheckBox();
        txtNumeroSequencia1 = new javax.swing.JFormattedTextField();
        chkACK1 = new javax.swing.JCheckBox();
        txtNumeroRecepcao1 = new javax.swing.JFormattedTextField();
        chkRST1 = new javax.swing.JCheckBox();
        chkFIN1 = new javax.swing.JCheckBox();
        lblChecksum2 = new javax.swing.JLabel();
        lblNumeroSequencia2 = new javax.swing.JLabel();
        lblUrgentPointer2 = new javax.swing.JLabel();
        chkURG1 = new javax.swing.JCheckBox();
        lblOptions2 = new javax.swing.JLabel();
        txtTamanhoCabecalho1 = new javax.swing.JTextField();
        lblNumeroRecepcao2 = new javax.swing.JLabel();
        txtChecksum1 = new javax.swing.JTextField();
        chkPSH1 = new javax.swing.JCheckBox();
        txtOptions1 = new javax.swing.JTextField();
        txtUrgentPointer1 = new javax.swing.JTextField();
        txtPortaOrigem1 = new javax.swing.JFormattedTextField();
        lblIpOrigem3 = new javax.swing.JLabel();
        lblPortaOrigem2 = new javax.swing.JLabel();
        txtIpOrigem1 = new javax.swing.JTextField();
        btnAceitar1 = new javax.swing.JButton();
        btnRecusar1 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        btnConfirmaEnvio1 = new javax.swing.JButton();
        pnlComunicacao1 = new javax.swing.JPanel();
        pnlSegmento1 = new javax.swing.JPanel();
        txtIpDestino2 = new javax.swing.JFormattedTextField();
        lblIpDestino3 = new javax.swing.JLabel();
        lblPortaDestino3 = new javax.swing.JLabel();
        txtTamanhoDadosPacote2 = new javax.swing.JTextField();
        txtJanelaRecepcao2 = new javax.swing.JFormattedTextField();
        lblTamanhoDadosPacote3 = new javax.swing.JLabel();
        txtPortaDestino2 = new javax.swing.JFormattedTextField();
        lblJanelaRecepcao3 = new javax.swing.JLabel();
        lblTamanhoCabecalho3 = new javax.swing.JLabel();
        chkSYN2 = new javax.swing.JCheckBox();
        txtNumeroSequencia2 = new javax.swing.JFormattedTextField();
        chkACK2 = new javax.swing.JCheckBox();
        txtNumeroRecepcao2 = new javax.swing.JFormattedTextField();
        chkRST2 = new javax.swing.JCheckBox();
        chkFIN2 = new javax.swing.JCheckBox();
        lblChecksum3 = new javax.swing.JLabel();
        lblNumeroSequencia3 = new javax.swing.JLabel();
        lblUrgentPointer3 = new javax.swing.JLabel();
        chkURG2 = new javax.swing.JCheckBox();
        lblOptions3 = new javax.swing.JLabel();
        txtTamanhoCabecalho2 = new javax.swing.JTextField();
        lblNumeroRecepcao3 = new javax.swing.JLabel();
        txtChecksum2 = new javax.swing.JTextField();
        chkPSH2 = new javax.swing.JCheckBox();
        txtOptions2 = new javax.swing.JTextField();
        txtUrgentPointer2 = new javax.swing.JTextField();
        txtPortaOrigem2 = new javax.swing.JFormattedTextField();
        lblIpOrigem4 = new javax.swing.JLabel();
        lblPortaOrigem3 = new javax.swing.JLabel();
        txtIpOrigem2 = new javax.swing.JTextField();
        btnAceitar2 = new javax.swing.JButton();
        btnRecusar2 = new javax.swing.JButton();
        jProgressBar2 = new javax.swing.JProgressBar();
        btnConfirmaEnvio2 = new javax.swing.JButton();
        pnlComunicacao2 = new javax.swing.JPanel();
        pnlSegmento2 = new javax.swing.JPanel();
        txtIpDestino3 = new javax.swing.JFormattedTextField();
        lblIpDestino4 = new javax.swing.JLabel();
        lblPortaDestino4 = new javax.swing.JLabel();
        txtTamanhoDadosPacote3 = new javax.swing.JTextField();
        txtJanelaRecepcao3 = new javax.swing.JFormattedTextField();
        lblTamanhoDadosPacote4 = new javax.swing.JLabel();
        txtPortaDestino3 = new javax.swing.JFormattedTextField();
        lblJanelaRecepcao4 = new javax.swing.JLabel();
        lblTamanhoCabecalho4 = new javax.swing.JLabel();
        chkSYN3 = new javax.swing.JCheckBox();
        txtNumeroSequencia3 = new javax.swing.JFormattedTextField();
        chkACK3 = new javax.swing.JCheckBox();
        txtNumeroRecepcao3 = new javax.swing.JFormattedTextField();
        chkRST3 = new javax.swing.JCheckBox();
        chkFIN3 = new javax.swing.JCheckBox();
        lblChecksum4 = new javax.swing.JLabel();
        lblNumeroSequencia4 = new javax.swing.JLabel();
        lblUrgentPointer4 = new javax.swing.JLabel();
        chkURG3 = new javax.swing.JCheckBox();
        lblOptions4 = new javax.swing.JLabel();
        txtTamanhoCabecalho3 = new javax.swing.JTextField();
        lblNumeroRecepcao4 = new javax.swing.JLabel();
        txtChecksum3 = new javax.swing.JTextField();
        chkPSH3 = new javax.swing.JCheckBox();
        txtOptions3 = new javax.swing.JTextField();
        txtUrgentPointer3 = new javax.swing.JTextField();
        txtPortaOrigem3 = new javax.swing.JFormattedTextField();
        lblIpOrigem5 = new javax.swing.JLabel();
        lblPortaOrigem4 = new javax.swing.JLabel();
        txtIpOrigem3 = new javax.swing.JTextField();
        btnAceitar3 = new javax.swing.JButton();
        btnRecusar3 = new javax.swing.JButton();
        jProgressBar3 = new javax.swing.JProgressBar();
        btnConfirmaEnvio3 = new javax.swing.JButton();
        btnNovoPainel = new javax.swing.JButton();
        jScrollPaneTESTE = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel3.setBackground(new java.awt.Color(0, 102, 51));

        pnlInicio.setBorder(new javax.swing.border.MatteBorder(null));

        jLabel2.setText("Porta Padrão: 5000");

        btnIniciarCliente.setText("Iniciar");
        btnIniciarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarClienteActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        panelInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Seu dispositivo", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        lblIpOrigem.setText("IP");

        txtIpOrigem.setEditable(false);
        txtIpOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpOrigemActionPerformed(evt);
            }
        });

        lblPorta.setText("Porta");

        txtPortaOrigem.setEditable(false);
        txtPortaOrigem.setText("5000");

        javax.swing.GroupLayout panelInformacoesLayout = new javax.swing.GroupLayout(panelInformacoes);
        panelInformacoes.setLayout(panelInformacoesLayout);
        panelInformacoesLayout.setHorizontalGroup(
            panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInformacoesLayout.createSequentialGroup()
                        .addComponent(lblIpOrigem)
                        .addGap(98, 98, 98)
                        .addComponent(lblPorta))
                    .addGroup(panelInformacoesLayout.createSequentialGroup()
                        .addComponent(txtIpOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPortaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelInformacoesLayout.setVerticalGroup(
            panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInformacoesLayout.createSequentialGroup()
                .addGroup(panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIpOrigem)
                    .addComponent(lblPorta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIpOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPortaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        listDispositivos.setBorder(javax.swing.BorderFactory.createTitledBorder("Dispositivos Online"));
        jScrollPane1.setViewportView(listDispositivos);

        btnThreeWayHandshake.setText("Three Way Handshake");
        btnThreeWayHandshake.setToolTipText("Selecione um dispositivo ao lado");
        btnThreeWayHandshake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThreeWayHandshakeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnThreeWayHandshake)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnThreeWayHandshake, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlInicioLayout = new javax.swing.GroupLayout(pnlInicio);
        pnlInicio.setLayout(pnlInicioLayout);
        pnlInicioLayout.setHorizontalGroup(
            pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInicioLayout.createSequentialGroup()
                .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInicioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlInicioLayout.createSequentialGroup()
                                .addComponent(btnIniciarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSair))))
                    .addGroup(pnlInicioLayout.createSequentialGroup()
                        .addGap(53, 53, 53)
                        .addComponent(jLabel2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlInicioLayout.setVerticalGroup(
            pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInicioLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnIniciarCliente)
                    .addComponent(btnSair))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane6.setMinimumSize(new java.awt.Dimension(700, 400));

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(580, 691));

        pnlComunicacao.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        pnlComunicacao.setMinimumSize(new java.awt.Dimension(400, 220));

        pnlSegmento.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lblIpDestino2.setText("IP de Destino");

        lblPortaDestino2.setText("Porta Destino");

        lblTamanhoDadosPacote2.setText("Dados");

        lblJanelaRecepcao2.setText("Janela de Recepção");

        lblTamanhoCabecalho2.setText("Tamanho Cabeçalho");

        chkSYN1.setText("SYN");

        chkACK1.setText("ACK");

        chkRST1.setText("RST");

        chkFIN1.setText("FIN");

        lblChecksum2.setText("Checksum");

        lblNumeroSequencia2.setText("Nº de Sequência");

        lblUrgentPointer2.setText("<html>Urgent<br>Pointer");

        chkURG1.setText("URG");

        lblOptions2.setText("Options");

        lblNumeroRecepcao2.setText("Nº de Recepção");

        chkPSH1.setText("PSH");

        lblIpOrigem3.setText("IP de Origem");

        lblPortaOrigem2.setText("Porta Origem");

        txtIpOrigem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpOrigem1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSegmentoLayout = new javax.swing.GroupLayout(pnlSegmento);
        pnlSegmento.setLayout(pnlSegmentoLayout);
        pnlSegmentoLayout.setHorizontalGroup(
            pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSegmentoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSegmentoLayout.createSequentialGroup()
                        .addComponent(chkURG1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPSH1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSYN1)
                        .addGap(18, 18, 18)
                        .addComponent(lblTamanhoDadosPacote2))
                    .addGroup(pnlSegmentoLayout.createSequentialGroup()
                        .addComponent(chkACK1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkRST1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFIN1)
                        .addGap(18, 18, 18)
                        .addComponent(txtTamanhoDadosPacote1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtChecksum1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtOptions1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUrgentPointer1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSegmentoLayout.createSequentialGroup()
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIpOrigem3)
                            .addComponent(txtIpOrigem1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIpDestino1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblPortaOrigem2)
                            .addComponent(txtPortaOrigem1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPortaDestino2)
                            .addComponent(txtPortaDestino1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSegmentoLayout.createSequentialGroup()
                                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNumeroSequencia2)
                                    .addComponent(txtNumeroSequencia1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblJanelaRecepcao2)
                                    .addComponent(txtJanelaRecepcao1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlSegmentoLayout.createSequentialGroup()
                                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblChecksum2)
                                    .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblNumeroRecepcao2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNumeroRecepcao1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTamanhoCabecalho1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(pnlSegmentoLayout.createSequentialGroup()
                                            .addComponent(lblOptions2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblUrgentPointer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblTamanhoCabecalho2, javax.swing.GroupLayout.Alignment.LEADING))))))
                    .addComponent(lblIpDestino2))
                .addContainerGap())
        );
        pnlSegmentoLayout.setVerticalGroup(
            pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSegmentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSegmentoLayout.createSequentialGroup()
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPortaOrigem2)
                            .addComponent(lblNumeroSequencia2)
                            .addComponent(lblIpOrigem3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPortaOrigem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNumeroSequencia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtIpOrigem1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlSegmentoLayout.createSequentialGroup()
                        .addComponent(lblJanelaRecepcao2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJanelaRecepcao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIpDestino1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPortaDestino1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroRecepcao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSegmentoLayout.createSequentialGroup()
                            .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblPortaDestino2)
                                .addComponent(lblIpDestino2)
                                .addComponent(lblNumeroRecepcao2))
                            .addGap(26, 26, 26)))
                    .addGroup(pnlSegmentoLayout.createSequentialGroup()
                        .addComponent(lblTamanhoCabecalho2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTamanhoCabecalho1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSegmentoLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkPSH1)
                            .addComponent(chkURG1)
                            .addComponent(chkSYN1)
                            .addComponent(lblTamanhoDadosPacote2)
                            .addComponent(lblChecksum2)
                            .addComponent(lblOptions2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkACK1)
                            .addComponent(chkRST1)
                            .addGroup(pnlSegmentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chkFIN1)
                                .addComponent(txtTamanhoDadosPacote1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtChecksum1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOptions1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtUrgentPointer1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSegmentoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblUrgentPointer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAceitar1.setForeground(new java.awt.Color(0, 204, 0));
        btnAceitar1.setText("ACEITAR");
        btnAceitar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceitar1ActionPerformed(evt);
            }
        });

        btnRecusar1.setForeground(new java.awt.Color(255, 51, 51));
        btnRecusar1.setText("RECUSAR");

        btnConfirmaEnvio1.setBackground(new java.awt.Color(0, 255, 0));
        btnConfirmaEnvio1.setText("Segmento Confirmado");

        javax.swing.GroupLayout pnlComunicacaoLayout = new javax.swing.GroupLayout(pnlComunicacao);
        pnlComunicacao.setLayout(pnlComunicacaoLayout);
        pnlComunicacaoLayout.setHorizontalGroup(
            pnlComunicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlComunicacaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlSegmento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(pnlComunicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlComunicacaoLayout.createSequentialGroup()
                        .addComponent(btnAceitar1)
                        .addGap(18, 18, 18)
                        .addComponent(btnRecusar1))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConfirmaEnvio1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlComunicacaoLayout.setVerticalGroup(
            pnlComunicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlComunicacaoLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(pnlComunicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceitar1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecusar1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfirmaEnvio1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlComunicacaoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlSegmento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlComunicacao1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        pnlComunicacao1.setMinimumSize(new java.awt.Dimension(400, 220));

        pnlSegmento1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lblIpDestino3.setText("IP de Destino");

        lblPortaDestino3.setText("Porta Destino");

        lblTamanhoDadosPacote3.setText("Dados");

        lblJanelaRecepcao3.setText("Janela de Recepção");

        lblTamanhoCabecalho3.setText("Tamanho Cabeçalho");

        chkSYN2.setText("SYN");

        chkACK2.setText("ACK");

        chkRST2.setText("RST");

        chkFIN2.setText("FIN");

        lblChecksum3.setText("Checksum");

        lblNumeroSequencia3.setText("Nº de Sequência");

        lblUrgentPointer3.setText("<html>Urgent<br>Pointer");

        chkURG2.setText("URG");

        lblOptions3.setText("Options");

        lblNumeroRecepcao3.setText("Nº de Recepção");

        chkPSH2.setText("PSH");

        lblIpOrigem4.setText("IP de Origem");

        lblPortaOrigem3.setText("Porta Origem");

        txtIpOrigem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpOrigem2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSegmento1Layout = new javax.swing.GroupLayout(pnlSegmento1);
        pnlSegmento1.setLayout(pnlSegmento1Layout);
        pnlSegmento1Layout.setHorizontalGroup(
            pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSegmento1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSegmento1Layout.createSequentialGroup()
                        .addComponent(chkURG2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPSH2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSYN2)
                        .addGap(18, 18, 18)
                        .addComponent(lblTamanhoDadosPacote3))
                    .addGroup(pnlSegmento1Layout.createSequentialGroup()
                        .addComponent(chkACK2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkRST2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFIN2)
                        .addGap(18, 18, 18)
                        .addComponent(txtTamanhoDadosPacote2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtChecksum2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtOptions2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUrgentPointer2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSegmento1Layout.createSequentialGroup()
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIpOrigem4)
                            .addComponent(txtIpOrigem2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIpDestino2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblPortaOrigem3)
                            .addComponent(txtPortaOrigem2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPortaDestino3)
                            .addComponent(txtPortaDestino2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSegmento1Layout.createSequentialGroup()
                                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNumeroSequencia3)
                                    .addComponent(txtNumeroSequencia2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblJanelaRecepcao3)
                                    .addComponent(txtJanelaRecepcao2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlSegmento1Layout.createSequentialGroup()
                                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblChecksum3)
                                    .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblNumeroRecepcao3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNumeroRecepcao2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTamanhoCabecalho2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(pnlSegmento1Layout.createSequentialGroup()
                                            .addComponent(lblOptions3)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblUrgentPointer3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblTamanhoCabecalho3, javax.swing.GroupLayout.Alignment.LEADING))))))
                    .addComponent(lblIpDestino3))
                .addContainerGap())
        );
        pnlSegmento1Layout.setVerticalGroup(
            pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSegmento1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSegmento1Layout.createSequentialGroup()
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPortaOrigem3)
                            .addComponent(lblNumeroSequencia3)
                            .addComponent(lblIpOrigem4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPortaOrigem2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNumeroSequencia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtIpOrigem2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlSegmento1Layout.createSequentialGroup()
                        .addComponent(lblJanelaRecepcao3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJanelaRecepcao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIpDestino2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPortaDestino2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroRecepcao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSegmento1Layout.createSequentialGroup()
                            .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblPortaDestino3)
                                .addComponent(lblIpDestino3)
                                .addComponent(lblNumeroRecepcao3))
                            .addGap(26, 26, 26)))
                    .addGroup(pnlSegmento1Layout.createSequentialGroup()
                        .addComponent(lblTamanhoCabecalho3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTamanhoCabecalho2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSegmento1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkPSH2)
                            .addComponent(chkURG2)
                            .addComponent(chkSYN2)
                            .addComponent(lblTamanhoDadosPacote3)
                            .addComponent(lblChecksum3)
                            .addComponent(lblOptions3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkACK2)
                            .addComponent(chkRST2)
                            .addGroup(pnlSegmento1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chkFIN2)
                                .addComponent(txtTamanhoDadosPacote2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtChecksum2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOptions2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtUrgentPointer2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSegmento1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblUrgentPointer3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAceitar2.setForeground(new java.awt.Color(0, 204, 0));
        btnAceitar2.setText("ACEITAR");
        btnAceitar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceitar2ActionPerformed(evt);
            }
        });

        btnRecusar2.setForeground(new java.awt.Color(255, 51, 51));
        btnRecusar2.setText("RECUSAR");

        btnConfirmaEnvio2.setBackground(new java.awt.Color(0, 255, 0));
        btnConfirmaEnvio2.setText("Segmento Confirmado");

        javax.swing.GroupLayout pnlComunicacao1Layout = new javax.swing.GroupLayout(pnlComunicacao1);
        pnlComunicacao1.setLayout(pnlComunicacao1Layout);
        pnlComunicacao1Layout.setHorizontalGroup(
            pnlComunicacao1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlComunicacao1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlSegmento1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(pnlComunicacao1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlComunicacao1Layout.createSequentialGroup()
                        .addComponent(btnAceitar2)
                        .addGap(18, 18, 18)
                        .addComponent(btnRecusar2))
                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConfirmaEnvio2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlComunicacao1Layout.setVerticalGroup(
            pnlComunicacao1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlComunicacao1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(pnlComunicacao1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceitar2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecusar2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfirmaEnvio2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlComunicacao1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlSegmento1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlComunicacao2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        pnlComunicacao2.setMinimumSize(new java.awt.Dimension(400, 220));

        pnlSegmento2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        lblIpDestino4.setText("IP de Destino");

        lblPortaDestino4.setText("Porta Destino");

        lblTamanhoDadosPacote4.setText("Dados");

        lblJanelaRecepcao4.setText("Janela de Recepção");

        lblTamanhoCabecalho4.setText("Tamanho Cabeçalho");

        chkSYN3.setText("SYN");

        chkACK3.setText("ACK");

        chkRST3.setText("RST");

        chkFIN3.setText("FIN");

        lblChecksum4.setText("Checksum");

        lblNumeroSequencia4.setText("Nº de Sequência");

        lblUrgentPointer4.setText("<html>Urgent<br>Pointer");

        chkURG3.setText("URG");

        lblOptions4.setText("Options");

        lblNumeroRecepcao4.setText("Nº de Recepção");

        chkPSH3.setText("PSH");

        lblIpOrigem5.setText("IP de Origem");

        lblPortaOrigem4.setText("Porta Origem");

        txtIpOrigem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpOrigem3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlSegmento2Layout = new javax.swing.GroupLayout(pnlSegmento2);
        pnlSegmento2.setLayout(pnlSegmento2Layout);
        pnlSegmento2Layout.setHorizontalGroup(
            pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSegmento2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSegmento2Layout.createSequentialGroup()
                        .addComponent(chkURG3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkPSH3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkSYN3)
                        .addGap(18, 18, 18)
                        .addComponent(lblTamanhoDadosPacote4))
                    .addGroup(pnlSegmento2Layout.createSequentialGroup()
                        .addComponent(chkACK3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkRST3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkFIN3)
                        .addGap(18, 18, 18)
                        .addComponent(txtTamanhoDadosPacote3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtChecksum3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtOptions3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUrgentPointer3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlSegmento2Layout.createSequentialGroup()
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblIpOrigem5)
                            .addComponent(txtIpOrigem3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIpDestino3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lblPortaOrigem4)
                            .addComponent(txtPortaOrigem3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPortaDestino4)
                            .addComponent(txtPortaDestino3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSegmento2Layout.createSequentialGroup()
                                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNumeroSequencia4)
                                    .addComponent(txtNumeroSequencia3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblJanelaRecepcao4)
                                    .addComponent(txtJanelaRecepcao3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pnlSegmento2Layout.createSequentialGroup()
                                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblChecksum4)
                                    .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblNumeroRecepcao4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtNumeroRecepcao3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTamanhoCabecalho3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(pnlSegmento2Layout.createSequentialGroup()
                                            .addComponent(lblOptions4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(lblUrgentPointer4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(lblTamanhoCabecalho4, javax.swing.GroupLayout.Alignment.LEADING))))))
                    .addComponent(lblIpDestino4))
                .addContainerGap())
        );
        pnlSegmento2Layout.setVerticalGroup(
            pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSegmento2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlSegmento2Layout.createSequentialGroup()
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPortaOrigem4)
                            .addComponent(lblNumeroSequencia4)
                            .addComponent(lblIpOrigem5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPortaOrigem3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNumeroSequencia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtIpOrigem3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlSegmento2Layout.createSequentialGroup()
                        .addComponent(lblJanelaRecepcao4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtJanelaRecepcao3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIpDestino3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPortaDestino3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroRecepcao3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSegmento2Layout.createSequentialGroup()
                            .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblPortaDestino4)
                                .addComponent(lblIpDestino4)
                                .addComponent(lblNumeroRecepcao4))
                            .addGap(26, 26, 26)))
                    .addGroup(pnlSegmento2Layout.createSequentialGroup()
                        .addComponent(lblTamanhoCabecalho4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTamanhoCabecalho3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSegmento2Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkPSH3)
                            .addComponent(chkURG3)
                            .addComponent(chkSYN3)
                            .addComponent(lblTamanhoDadosPacote4)
                            .addComponent(lblChecksum4)
                            .addComponent(lblOptions4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkACK3)
                            .addComponent(chkRST3)
                            .addGroup(pnlSegmento2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(chkFIN3)
                                .addComponent(txtTamanhoDadosPacote3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtChecksum3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtOptions3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtUrgentPointer3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlSegmento2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblUrgentPointer4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnAceitar3.setForeground(new java.awt.Color(0, 204, 0));
        btnAceitar3.setText("ACEITAR");
        btnAceitar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceitar3ActionPerformed(evt);
            }
        });

        btnRecusar3.setForeground(new java.awt.Color(255, 51, 51));
        btnRecusar3.setText("RECUSAR");

        btnConfirmaEnvio3.setBackground(new java.awt.Color(0, 255, 0));
        btnConfirmaEnvio3.setText("Segmento Confirmado");

        javax.swing.GroupLayout pnlComunicacao2Layout = new javax.swing.GroupLayout(pnlComunicacao2);
        pnlComunicacao2.setLayout(pnlComunicacao2Layout);
        pnlComunicacao2Layout.setHorizontalGroup(
            pnlComunicacao2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlComunicacao2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlSegmento2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(pnlComunicacao2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlComunicacao2Layout.createSequentialGroup()
                        .addComponent(btnAceitar3)
                        .addGap(18, 18, 18)
                        .addComponent(btnRecusar3))
                    .addComponent(jProgressBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConfirmaEnvio3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlComunicacao2Layout.setVerticalGroup(
            pnlComunicacao2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlComunicacao2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(pnlComunicacao2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceitar3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRecusar3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jProgressBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnConfirmaEnvio3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(pnlComunicacao2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(pnlSegmento2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlComunicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlComunicacao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlComunicacao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlComunicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlComunicacao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlComunicacao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane6.setViewportView(jPanel1);

        btnNovoPainel.setText("Novo Painel");
        btnNovoPainel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoPainelActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 617, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 353, Short.MAX_VALUE)
        );

        jScrollPaneTESTE.setViewportView(jPanel4);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnNovoPainel)
                        .addGap(96, 96, 96))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jScrollPaneTESTE)
                        .addGap(193, 193, 193))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 691, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 468, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(113, 113, 113)
                .addComponent(btnNovoPainel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneTESTE, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        JtabAbas.addTab("  Início  ", jPanel3);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JtabAbas)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JtabAbas)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAceitar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceitar3ActionPerformed

        telaEnvio.setIpOrigem(mensagem.getIpOrigem());
        telaEnvio.setIpDestino(mensagem.getIpDestino());
        telaEnvio.camposAtivosInicio();
        telaEnvio.setVisible(true);
        System.out.println("IP de ORIGEM:" + mensagem.getIpOrigem());
    }//GEN-LAST:event_btnAceitar3ActionPerformed

    private void txtIpOrigem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpOrigem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpOrigem3ActionPerformed

    private void btnAceitar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceitar2ActionPerformed

        aceitarPacote();
        System.out.println("Segundo aceitar: " + mensagem.getPacotes().size());


    }//GEN-LAST:event_btnAceitar2ActionPerformed

    private void txtIpOrigem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpOrigem2ActionPerformed

    }//GEN-LAST:event_txtIpOrigem2ActionPerformed

    private void btnAceitar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceitar1ActionPerformed

        this.pacote.setAceito(true);

        setarValores();

        //this.pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getNumeroSequencia() + 1);
        mensagem.setAction(Action.ENVIAR);
        this.service.enviar(mensagem);
    }//GEN-LAST:event_btnAceitar1ActionPerformed

    private void txtIpOrigem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpOrigem1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpOrigem1ActionPerformed

    private void btnThreeWayHandshakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThreeWayHandshakeActionPerformed

        solicitaConexao();

        aba = new PainelPacote(mensagem, service);

        JtabAbas.addTab("Mensagem " + mensagem.getIpDestino(), adicionarNovoPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem))));
        preencherPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem)));
        System.out.println("Janela recp após preencher:" + mensagem.getPacotes().get(retornaUltimoPacote(mensagem)).getTamanhoJanela());
        JtabAbas.setSelectedIndex(mensagem.getIdMensagem());
    }//GEN-LAST:event_btnThreeWayHandshakeActionPerformed

    private void txtIpOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpOrigemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpOrigemActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        this.mensagem.setAction(Action.DISCONECTAR);
        this.service.enviar(this.mensagem);
        disconectar();
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnIniciarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarClienteActionPerformed

        this.mensagem = new Mensagem();
        this.pacote = new Pacote();
        this.mensagem.getPacotes().add(this.pacote);

        this.mensagem.setAction(Action.CONECTAR);

        this.service = new ClienteService();
        this.socket = this.service.conectar();

        new Thread(new ListenerSocket(this.socket)).start();

        try {

            this.mensagem.setIpOrigem(InetAddress.getLocalHost().getHostAddress());

        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }

        //this.mensagem.setIpOrigem(txtIpOrigem.getText());
        this.service.enviar(mensagem);
    }//GEN-LAST:event_btnIniciarClienteActionPerformed

    private void btnNovoPainelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoPainelActionPerformed

        /*painelPrincipal.remove(painelSul);
        painelSul = new OutroPainelDoSul();
        painelPrincipal.add(BorderLayout.SOUTH, painelSul);
        painelPrincipal.revalidate();*/
        aba = new PainelPacote(mensagem, service);
        jPanel4.setLayout(new GridLayout(100, 1));
        jPanel4.add(BorderLayout.NORTH, aba.novoPainel());
        jPanel4.revalidate();
        jPanel4.repaint();
        acionarBotaoAceitar(aba);
        preencherPainel(aba, mensagem.getPacotes().get(retornaUltimoPacote(mensagem)));

    }//GEN-LAST:event_btnNovoPainelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BemVindo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BemVindo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane JtabAbas;
    private javax.swing.JButton btnAceitar1;
    private javax.swing.JButton btnAceitar2;
    private javax.swing.JButton btnAceitar3;
    private javax.swing.JButton btnConfirmaEnvio1;
    private javax.swing.JButton btnConfirmaEnvio2;
    private javax.swing.JButton btnConfirmaEnvio3;
    private javax.swing.JButton btnIniciarCliente;
    private javax.swing.JButton btnNovoPainel;
    private javax.swing.JButton btnRecusar1;
    private javax.swing.JButton btnRecusar2;
    private javax.swing.JButton btnRecusar3;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnThreeWayHandshake;
    private javax.swing.JCheckBox chkACK1;
    private javax.swing.JCheckBox chkACK2;
    private javax.swing.JCheckBox chkACK3;
    private javax.swing.JCheckBox chkFIN1;
    private javax.swing.JCheckBox chkFIN2;
    private javax.swing.JCheckBox chkFIN3;
    private javax.swing.JCheckBox chkPSH1;
    private javax.swing.JCheckBox chkPSH2;
    private javax.swing.JCheckBox chkPSH3;
    private javax.swing.JCheckBox chkRST1;
    private javax.swing.JCheckBox chkRST2;
    private javax.swing.JCheckBox chkRST3;
    private javax.swing.JCheckBox chkSYN1;
    private javax.swing.JCheckBox chkSYN2;
    private javax.swing.JCheckBox chkSYN3;
    private javax.swing.JCheckBox chkURG1;
    private javax.swing.JCheckBox chkURG2;
    private javax.swing.JCheckBox chkURG3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JProgressBar jProgressBar3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPaneTESTE;
    private javax.swing.JLabel lblChecksum2;
    private javax.swing.JLabel lblChecksum3;
    private javax.swing.JLabel lblChecksum4;
    private javax.swing.JLabel lblIpDestino2;
    private javax.swing.JLabel lblIpDestino3;
    private javax.swing.JLabel lblIpDestino4;
    private javax.swing.JLabel lblIpOrigem;
    private javax.swing.JLabel lblIpOrigem3;
    private javax.swing.JLabel lblIpOrigem4;
    private javax.swing.JLabel lblIpOrigem5;
    private javax.swing.JLabel lblJanelaRecepcao2;
    private javax.swing.JLabel lblJanelaRecepcao3;
    private javax.swing.JLabel lblJanelaRecepcao4;
    private javax.swing.JLabel lblNumeroRecepcao2;
    private javax.swing.JLabel lblNumeroRecepcao3;
    private javax.swing.JLabel lblNumeroRecepcao4;
    private javax.swing.JLabel lblNumeroSequencia2;
    private javax.swing.JLabel lblNumeroSequencia3;
    private javax.swing.JLabel lblNumeroSequencia4;
    private javax.swing.JLabel lblOptions2;
    private javax.swing.JLabel lblOptions3;
    private javax.swing.JLabel lblOptions4;
    private javax.swing.JLabel lblPorta;
    private javax.swing.JLabel lblPortaDestino2;
    private javax.swing.JLabel lblPortaDestino3;
    private javax.swing.JLabel lblPortaDestino4;
    private javax.swing.JLabel lblPortaOrigem2;
    private javax.swing.JLabel lblPortaOrigem3;
    private javax.swing.JLabel lblPortaOrigem4;
    private javax.swing.JLabel lblTamanhoCabecalho2;
    private javax.swing.JLabel lblTamanhoCabecalho3;
    private javax.swing.JLabel lblTamanhoCabecalho4;
    private javax.swing.JLabel lblTamanhoDadosPacote2;
    private javax.swing.JLabel lblTamanhoDadosPacote3;
    private javax.swing.JLabel lblTamanhoDadosPacote4;
    private javax.swing.JLabel lblUrgentPointer2;
    private javax.swing.JLabel lblUrgentPointer3;
    private javax.swing.JLabel lblUrgentPointer4;
    private javax.swing.JList<String> listDispositivos;
    private javax.swing.JPanel panelInformacoes;
    private javax.swing.JPanel pnlComunicacao;
    private javax.swing.JPanel pnlComunicacao1;
    private javax.swing.JPanel pnlComunicacao2;
    private javax.swing.JPanel pnlInicio;
    private javax.swing.JPanel pnlSegmento;
    private javax.swing.JPanel pnlSegmento1;
    private javax.swing.JPanel pnlSegmento2;
    private javax.swing.JTextField txtChecksum1;
    private javax.swing.JTextField txtChecksum2;
    private javax.swing.JTextField txtChecksum3;
    private javax.swing.JFormattedTextField txtIpDestino1;
    private javax.swing.JFormattedTextField txtIpDestino2;
    private javax.swing.JFormattedTextField txtIpDestino3;
    private javax.swing.JTextField txtIpOrigem;
    private javax.swing.JTextField txtIpOrigem1;
    private javax.swing.JTextField txtIpOrigem2;
    private javax.swing.JTextField txtIpOrigem3;
    private javax.swing.JFormattedTextField txtJanelaRecepcao1;
    private javax.swing.JFormattedTextField txtJanelaRecepcao2;
    private javax.swing.JFormattedTextField txtJanelaRecepcao3;
    private javax.swing.JFormattedTextField txtNumeroRecepcao1;
    private javax.swing.JFormattedTextField txtNumeroRecepcao2;
    private javax.swing.JFormattedTextField txtNumeroRecepcao3;
    private javax.swing.JFormattedTextField txtNumeroSequencia1;
    private javax.swing.JFormattedTextField txtNumeroSequencia2;
    private javax.swing.JFormattedTextField txtNumeroSequencia3;
    private javax.swing.JTextField txtOptions1;
    private javax.swing.JTextField txtOptions2;
    private javax.swing.JTextField txtOptions3;
    private javax.swing.JFormattedTextField txtPortaDestino1;
    private javax.swing.JFormattedTextField txtPortaDestino2;
    private javax.swing.JFormattedTextField txtPortaDestino3;
    private javax.swing.JTextField txtPortaOrigem;
    private javax.swing.JFormattedTextField txtPortaOrigem1;
    private javax.swing.JFormattedTextField txtPortaOrigem2;
    private javax.swing.JFormattedTextField txtPortaOrigem3;
    private javax.swing.JTextField txtTamanhoCabecalho1;
    private javax.swing.JTextField txtTamanhoCabecalho2;
    private javax.swing.JTextField txtTamanhoCabecalho3;
    private javax.swing.JTextField txtTamanhoDadosPacote1;
    private javax.swing.JTextField txtTamanhoDadosPacote2;
    private javax.swing.JTextField txtTamanhoDadosPacote3;
    private javax.swing.JTextField txtUrgentPointer1;
    private javax.swing.JTextField txtUrgentPointer2;
    private javax.swing.JTextField txtUrgentPointer3;
    // End of variables declaration//GEN-END:variables

    private Socket socket;
    private Mensagem mensagem;
    private ClienteService service;
    private Pacote pacote;
    EnvioDeMensagens telaEnvio = new EnvioDeMensagens();
    private ArrayList<PainelPacote> paineis = new ArrayList<>();
    private Timer timer;
    private ActionListener atualizaBarra;
    private PainelPacote aba;
    private int count = 0;
}
