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
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static javax.swing.JOptionPane.showMessageDialog;
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
                        //System.out.println(mensagem.getPacotes().size());
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
        janelaRecepção = mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getTamanhoJanela();
        System.out.println("Recebendo:");
        System.out.println("Qtd Pacotes: " + mensagem.getPacotes().size());
        System.out.println("NumSeq: " + mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia());
        System.out.println("NumRecp: " + mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroAvisoRecepcao());
        System.out.println("Janela:" + janelaRecepção);
        System.out.println("----------------------");

        if (JtabAbas.getTabCount() > (mensagem.getIdMensagem())) {

            JtabAbas.setSelectedIndex(mensagem.getIdMensagem());

            exibePacotesRecebidos(ultimoPacoteEnviado(mensagem));

        } else {

            aba = new PainelPacote(mensagem, service);
            JtabAbas.addTab("Mensagem " + mensagem.getIpOrigem(), adicionarNovoPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem))));
            JtabAbas.setSelectedIndex(mensagem.getIdMensagem());
            aba.setTxtOptionsX(String.valueOf(mensagem.getMssEmissor()));
        }
    }

    private int ultimoPacoteEnviado(Mensagem mensagem) {

        int indice = 0;
        for (int i = mensagem.getPacotes().size(); i > 1; i--) {
            try {
                if (mensagem.getPacotes().get((i - 1)).getIpOrigem().equals((InetAddress.getLocalHost().getHostAddress()))) {

                    indice = i - 1;
                    break;
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return indice;
    }

    private void exibePacotesRecebidos(int indice) {

        indice++;
        while (indice < mensagem.getPacotes().size()) {

            
            adicionarNovoPainel(aba, mensagem.getPacotes().get(indice));
            indice++;

        }
    }

    private void verificarACK(Mensagem mensagem) {

        if (mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).isAceito()) {
            for (Pacote pacote : mensagem.getPacotes()) {
                if ((mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroAvisoRecepcao() - defineTamanhoSegmento()) == pacote.getNumeroSequencia()) {
                    showMessageDialog(null, "Recebimento do Pacote NumSeq: " + pacote.getNumeroSequencia() + " foi CONFIRMADO");

                }
            }
        }

    }

    private void usuariosOnline(Mensagem mensagem) {

        System.out.println("Dispositivos online: " + mensagem.getUsuariosOnline().toString());

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
            setarValores(pacote);
            this.listDispositivos.clearSelection();

        } else {

            showMessageDialog(this, "Seleciona algum, idiota");
        }

    }

    private void setarValores(Pacote pacoteAtual) {

        try {
            if (mensagem.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                pacote = new Pacote();
                pacote.setIpOrigem(mensagem.getIpOrigem());
                pacote.setIpDestino(mensagem.getIpDestino());

                pacote.setPortaOrigem((short) 5000);
                pacote.setPortaDestino((short) 5000);

                if (mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroAvisoRecepcao() == 0) {
                    pacote.setNumeroSequencia(valorAleatorio());
                    pacote.setAceito(false);

                } else if (mensagem.getPacotes().size() <= 3) {
                    pacote.setNumeroSequencia(mensagem.getPacotes().get(indiceUltimoPacote(mensagem) - 1).getNumeroSequencia() + 1);
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia() + 1);
                    //janelaRecepção = (short) mensagem.getPacotes().get(ultimoPacoteEnviado(mensagem) + 1).getTamanhoJanela();
                    //System.out.println("teste numero recepção: " + janelaRecepção);
                } else if (mensagem.getPacotes().size() == 4) {
                    pacote.setNumeroSequencia(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia());
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroAvisoRecepcao());
                    //contendo os dados

                } else if (verificaSeReenvio(pacoteAtual)) {
                    pacote.setNumeroSequencia(pacoteAtual.getNumeroAvisoRecepcao());
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia() + 1);

                } else {
                    if (mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                        pacote.setNumeroSequencia(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia() + defineTamanhoSegmento());
                    } else {
                        pacote.setNumeroSequencia(mensagem.getPacotes().get(indiceUltimoPacote(mensagem) - 1).getNumeroSequencia() + defineTamanhoSegmento());
                        System.out.println("TAMANHO MSS: " + defineTamanhoSegmento());
                    }
                    pacote.setNumeroAvisoRecepcao(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia() + 1);
                }
                    
                

                mensagem.getPacotes().add(pacote);

            } else {
                pacote = new Pacote();
                pacote.setIpOrigem(mensagem.getIpDestino());
                pacote.setIpDestino(mensagem.getIpOrigem());
                pacote.setPortaOrigem((short) 5000);
                pacote.setPortaDestino((short) 5000);

                if (mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroAvisoRecepcao() == 0) {
                    pacote.setNumeroSequencia(valorAleatorio());

                } else {
                    pacote.setNumeroSequencia(mensagem.getPacotes().get(ultimoPacoteEnviado(mensagem)).getNumeroSequencia() + 1);

                }
                pacote.setNumeroAvisoRecepcao(pacoteAtual.getNumeroSequencia() + defineTamanhoSegmento());
                pacote.setTamanhoJanela(janelaRecepção);
                mensagem.getPacotes().add(pacote);
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean verificaSeReenvio(Pacote pacote) {

        boolean retorno = false;
        for (Pacote item : mensagem.getPacotes()) {
            try {
                if (pacote.getNumeroAvisoRecepcao() == item.getNumeroSequencia() && item.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                    retorno = true;
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno;
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

    private void enviarPacotes(Pacote pacoteAtual) {
        System.out.println("MSS EMISSOR: " + mensagem.getMssEmissor());
        System.out.println("MSS Receptor: " + mensagem.getMssReceptor());
        System.out.println("MSS Menor: " + defineTamanhoSegmento());
        System.out.println("Quantidade de Pacotes: " + quantidadePacotes());

        try {
            if (mensagem.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                if (this.count == (quantidadePacotes())) {

                    //Finaliza e Exibi a mensagem
                } else {

                    while (janelaRecepção > 0) {
                        setarPaineis();

                        this.count++;
                    }
                }
                this.mensagem.setAction(Action.ENVIAR);
                this.service.enviar(mensagem);
                System.out.println("Eviar Pacote \n Pacotes: " + mensagem.getPacotes().size() + "\n Count: " + this.count);

            } else {

                setarValores(pacoteAtual);

                mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).setAceito(true);

                adicionarNovoPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem)));
                this.mensagem.setAction(Action.ENVIAR);
                this.service.enviar(mensagem);
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setarPaineis() {

        setarValores(mensagem.getPacotes().get(indiceUltimoPacote(mensagem)));
        mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).setAceito(false);
        adicionarNovoPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem)));
        janelaRecepção--;

    }

    private int valorAleatorio() {
        Random gerador = new Random();
        return gerador.nextInt(11700621) + 40700621;
    }

    private int indiceUltimoPacote(Mensagem mensagem) {

        return (mensagem.getPacotes().size() - 1);
    }

    private int retornaUltimoPainel(Mensagem mensagem) {

        return (paineis.size() - 1);
    }

    private int DadosRestantes() {

        return (mensagem.getTamanhoMensagem() - defineTamanhoSegmento());
    }

    private JPanel adicionarNovoPainel(PainelPacote painel, Pacote pacote) {

        painel.painelScroll.setLayout(new GridLayout(100, 1));
        painel.painelScroll.add(BorderLayout.NORTH, painel.novoPainel());
        //preencherPainel(painel);
        preencherPainel(painel, pacote);
        painel.painelScroll.revalidate();
        painel.painelScroll.repaint();

        acionarBotaoAceitar(painel, pacote);

        paineis.add(painel);
        return (painel);
    }


    private void preencherPainel(PainelPacote painel, Pacote pacote) {

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
                if (mensagem.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())) {
                    painel.btnAceitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/Enviar1.png")));
                    painel.btnAceitar.setToolTipText("Enviar Próximo Pacote");

                }
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

    private void acionarBotaoAceitar(PainelPacote painel, Pacote pacote) {
        painel.getBtnAceitar().addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceitarActionPerformed(evt, pacote, painel);                
            }
        });
    }

    private void btnAceitarActionPerformed(java.awt.event.ActionEvent evt, Pacote pacote, PainelPacote painel) {
        painel.btnAceitar.setEnabled(false);
        painel.btnRecusar.setEnabled(false);
        System.out.println("---------------------- \n --------------");
        System.out.println("Recebendo BOTÃO ACeitar:");
        System.out.println("Qtd Pacotes: " + mensagem.getPacotes().size());
        System.out.println("NumSeq: " + mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroSequencia());
        System.out.println("NumRecp: " + mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getNumeroAvisoRecepcao());
        System.out.println("----------------------");
        
        try {
            if(!mensagem.getIpOrigem().equals(InetAddress.getLocalHost().getHostAddress())){
                janelaRecepção++;
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }
//estou enviando junto o pacote em questão, para que seja modificado com um foreach
        if (paineis.size() == 1) {
            setarValores(pacote);
            aba.painelDesktop.setVisible(true);
            adicionarNovoPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem)));
        } else if (paineis.size() <= 3) {

            setarValores(pacote);

            mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).setAceito(true);

            adicionarNovoPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem)));

            if (paineis.size() == 3) {
                enviarPacotes(pacote);
                //o primeiro pacote tem de ser igual ao ultimo NumSEQ que enviei com ACK
            }
        } else {

            enviarPacotes(pacote);
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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );

        btnThreeWayHandshake.setText("Three Way Handshake");
        btnThreeWayHandshake.setToolTipText("Selecione um dispositivo ao lado");
        btnThreeWayHandshake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThreeWayHandshakeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInicioLayout = new javax.swing.GroupLayout(pnlInicio);
        pnlInicio.setLayout(pnlInicioLayout);
        pnlInicioLayout.setHorizontalGroup(
            pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInicioLayout.createSequentialGroup()
                .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlInicioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlInicioLayout.createSequentialGroup()
                                .addComponent(btnIniciarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSair))
                            .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnThreeWayHandshake)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(btnThreeWayHandshake, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane6.setMinimumSize(new java.awt.Dimension(700, 400));

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(580, 691));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 672, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 691, Short.MAX_VALUE)
        );

        jScrollPane6.setViewportView(jPanel1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 691, Short.MAX_VALUE)
                .addGap(128, 128, 128))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(pnlInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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

    private void btnThreeWayHandshakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThreeWayHandshakeActionPerformed

        solicitaConexao();

        aba = new PainelPacote(mensagem, service);

        JtabAbas.addTab("Mensagem " + mensagem.getIpDestino(), adicionarNovoPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem))));
        preencherPainel(aba, mensagem.getPacotes().get(indiceUltimoPacote(mensagem)));
        System.out.println("Janela recp após preencher:" + mensagem.getPacotes().get(indiceUltimoPacote(mensagem)).getTamanhoJanela());
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
    private javax.swing.JButton btnIniciarCliente;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnThreeWayHandshake;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JLabel lblIpOrigem;
    private javax.swing.JLabel lblPorta;
    private javax.swing.JList<String> listDispositivos;
    private javax.swing.JPanel panelInformacoes;
    private javax.swing.JPanel pnlInicio;
    private javax.swing.JTextField txtIpOrigem;
    private javax.swing.JTextField txtPortaOrigem;
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
    short janelaRecepção = 0;
}
