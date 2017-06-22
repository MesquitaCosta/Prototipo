package telas;

import aplicacao.Mensagem;
import aplicacao.Mensagem.Action;
import aplicacao.Pacote;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.swing.ListSelectionModel;
import service.ClienteService;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author LORD
 */
public class Inicio extends javax.swing.JInternalFrame {

    private Socket socket;
    private Mensagem mensagem;
    private ClienteService service;
    private Pacote pacote;
    
    private TelaPrincipal telaPrincipal;

    public Inicio(TelaPrincipal principal) {
        initComponents();
        
        telaPrincipal = principal;
        btnSair.setEnabled(false);
        listDispositivos.setEnabled(false);
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

                    Mensagem.Action action = mensagem.getAction();

                    if (action.equals(Mensagem.Action.CONECTAR)) {
                        //Qdo clicar em enviar mensagem e o ID da mensagem for igual a 1
                        conectou(mensagem);

                    } else if (action.equals(Mensagem.Action.DISCONECTAR)) {
                        disconectar();
                        socket.close();

                    } else if (action.equals(Mensagem.Action.ENVIAR)) {
                        receber(mensagem);

                    } else if (action.equals(Mensagem.Action.USUARIOS_ONLINE)) {

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
        this.btnIniciar.setEnabled(false);
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
        
        telaPrincipal.telaRecebimento();
        service.enviar(mensagem);
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

            setarValores();

            System.out.println(valorAleatorio());
            this.service.enviar(this.mensagem);
            this.listDispositivos.clearSelection();

        } else {

            showMessageDialog(this, "Seleciona algum, idiota");
        }

    }

    private void setarValores() {

        this.mensagem.setIpOrigem(txtIpOrigem.getText());
        this.mensagem.setIpDestino(this.listDispositivos.getSelectedValue());
        this.mensagem.setAction(Mensagem.Action.ENVIAR);
        this.mensagem.setIdMensagem(1);
        this.pacote.setNumeroSequencia(valorAleatorio());
        this.mensagem.getPacotes().add(pacote);
    }

    private int valorAleatorio() {
        Random gerador = new Random();
        return gerador.nextInt();
    }

    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnIniciar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        panelInformacoes = new javax.swing.JPanel();
        lblIpOrigem = new javax.swing.JLabel();
        txtIpOrigem = new javax.swing.JTextField();
        lblPorta = new javax.swing.JLabel();
        txtPortaOrigem = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listDispositivos = new javax.swing.JList<>();
        btnThreeWayHandshake = new javax.swing.JButton();

        jLabel4.setText("Porta Padrão: 5000");

        btnIniciar.setText("Iniciar");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
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
                    .addComponent(lblIpOrigem)
                    .addComponent(txtIpOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPortaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPorta))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelInformacoesLayout.setVerticalGroup(
            panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIpOrigem)
                    .addComponent(lblPorta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIpOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPortaOrigem))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        listDispositivos.setBorder(javax.swing.BorderFactory.createTitledBorder("Selecione o Host de Destino: "));
        jScrollPane1.setViewportView(listDispositivos);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnThreeWayHandshake.setText("<html>Three Way<br> Handshake");
        btnThreeWayHandshake.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThreeWayHandshakeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnThreeWayHandshake, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnIniciar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSair, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addGap(113, 113, 113))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(btnIniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(btnThreeWayHandshake, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        this.mensagem.setAction(Action.DISCONECTAR);
        this.service.enviar(this.mensagem);
        disconectar();
    }//GEN-LAST:event_btnSairActionPerformed

    private void txtIpOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpOrigemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpOrigemActionPerformed

    private void btnThreeWayHandshakeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThreeWayHandshakeActionPerformed

        solicitaConexao();

        this.service.enviar(this.mensagem);

        telaPrincipal.telaEnvia(this.mensagem);

    }//GEN-LAST:event_btnThreeWayHandshakeActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed

        this.mensagem = new Mensagem();
        this.pacote = new Pacote();

        this.mensagem.setAction(Action.CONECTAR);

        this.service = new ClienteService();
        this.socket = this.service.conectar();

        new Thread(new ListenerSocket(this.socket)).start();
        /*
        try {

            this.mensagem.setIpOrigem(InetAddress.getLocalHost().getHostAddress());

        } catch (UnknownHostException ex) {
            Logger.getLogger(BemVindo.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        this.mensagem.setIpOrigem(txtIpOrigem.getText());

        //this.mensagem.setNome(txtNome.getText());
        this.service.enviar(this.mensagem);

        // showMessageDialog(null, "Não conectou ao servidor. \nVerifique se existe um servidor em Atividade");
        //this.mensagem.getPacotes().add(this.pacote);
        //TelaUsuariosConectados usuarios = new TelaUsuariosConectados(this.mensagem);
        //usuarios.setVisible(true);
    }//GEN-LAST:event_btnIniciarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnIniciarCliente;
    private javax.swing.JButton btnIniciarCliente1;
    private javax.swing.JButton btnSair;
    private javax.swing.JButton btnThreeWayHandshake;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblIpOrigem;
    private javax.swing.JLabel lblPorta;
    private javax.swing.JList<String> listDispositivos;
    private javax.swing.JPanel panelInformacoes;
    private javax.swing.JTextField txtIpOrigem;
    private javax.swing.JTextField txtPortaOrigem;
    // End of variables declaration//GEN-END:variables
}
