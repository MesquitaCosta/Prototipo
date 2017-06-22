/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telas;

import aplicacao.Mensagem;
import aplicacao.Pacote;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import service.ClienteService;

/**
 *
 * @author Lord
 */
public class TelaEnvio extends javax.swing.JFrame {

    private Mensagem mensagem = new Mensagem();
    private ClienteService service;

    public TelaEnvio() {
        initComponents();

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

    }

    private void conectar(Mensagem mensagem) {

    }

    private void disconectar(Mensagem mensagem) {

    }

    private void receber(Mensagem mensagem) {
        this.mensagem = mensagem;
        preencherCampos(this.mensagem);
    }

    private void usuariosOnline(Mensagem mensagem) {

    }

    private void preencherCampos(Mensagem mensagem) {

        txtNumeroSequencia.setText(String.valueOf(mensagem.getPacotes().get(0).getNumeroSequencia()));
        chkSYN.setSelected(true);
        txtIpOrigem.setText(mensagem.getIpOrigem());
        txtIpDestino.setText(mensagem.getIpDestino());
        txtPortaOrigem.setText("5000");
        txtPortaDestino.setText("5000");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        JPanelMensagem = new javax.swing.JPanel();
        txtIpDestino = new javax.swing.JFormattedTextField();
        lblIpDestino = new javax.swing.JLabel();
        lblPortaDestino = new javax.swing.JLabel();
        txtTamanhoDadosPacote = new javax.swing.JTextField();
        txtJanelaRecepcao = new javax.swing.JFormattedTextField();
        lblTamanhoDadosPacote = new javax.swing.JLabel();
        txtPortaDestino = new javax.swing.JFormattedTextField();
        lblJanelaRecepcao = new javax.swing.JLabel();
        lblTamanhoCabecalho = new javax.swing.JLabel();
        chkSYN = new javax.swing.JCheckBox();
        txtNumeroSequencia = new javax.swing.JFormattedTextField();
        chkACK = new javax.swing.JCheckBox();
        txtNumeroRecepcao = new javax.swing.JFormattedTextField();
        chkRST = new javax.swing.JCheckBox();
        chkFIN = new javax.swing.JCheckBox();
        lblChecksum = new javax.swing.JLabel();
        lblNumeroSequencia = new javax.swing.JLabel();
        lblUrgentPointer = new javax.swing.JLabel();
        chkURG = new javax.swing.JCheckBox();
        lblOptions = new javax.swing.JLabel();
        txtTamanhoCabecalho = new javax.swing.JTextField();
        lblNumeroRecepcao = new javax.swing.JLabel();
        txtChecksum = new javax.swing.JTextField();
        chkPSH = new javax.swing.JCheckBox();
        txtOptions = new javax.swing.JTextField();
        txtUrgentPointer = new javax.swing.JTextField();
        txtPortaOrigem = new javax.swing.JFormattedTextField();
        lblIpOrigem = new javax.swing.JLabel();
        lblPortaOrigem = new javax.swing.JLabel();
        txtIpOrigem = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 51, 0));

        JPanelMensagem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "PC1", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        txtIpDestino.setEditable(false);

        lblIpDestino.setText("IP de Destino");

        lblPortaDestino.setText("Porta Destino");

        txtTamanhoDadosPacote.setEditable(false);

        txtJanelaRecepcao.setEditable(false);

        lblTamanhoDadosPacote.setText("Tamanho dos Dados do Pacote");

        txtPortaDestino.setEditable(false);

        lblJanelaRecepcao.setText("Janela de Recepção");

        lblTamanhoCabecalho.setText("Tamanho Cabeçalho");

        chkSYN.setText("SYN");

        txtNumeroSequencia.setEditable(false);

        chkACK.setText("ACK");

        txtNumeroRecepcao.setEditable(false);

        chkRST.setText("RST");

        chkFIN.setText("FIN");

        lblChecksum.setText("Checksum");

        lblNumeroSequencia.setText("Nº de Sequência");

        lblUrgentPointer.setText("<html>Urgent<br>Pointer");

        chkURG.setText("URG");

        lblOptions.setText("Options");

        txtTamanhoCabecalho.setEditable(false);

        lblNumeroRecepcao.setText("Nº de Recepção");

        txtChecksum.setEditable(false);

        chkPSH.setText("PSH");

        txtOptions.setEditable(false);

        txtUrgentPointer.setEditable(false);

        txtPortaOrigem.setEditable(false);

        lblIpOrigem.setText("IP de Origem");

        lblPortaOrigem.setText("Porta Origem");

        txtIpOrigem.setEditable(false);
        txtIpOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIpOrigemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JPanelMensagemLayout = new javax.swing.GroupLayout(JPanelMensagem);
        JPanelMensagem.setLayout(JPanelMensagemLayout);
        JPanelMensagemLayout.setHorizontalGroup(
            JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanelMensagemLayout.createSequentialGroup()
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelMensagemLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblTamanhoDadosPacote))
                    .addGroup(JPanelMensagemLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JPanelMensagemLayout.createSequentialGroup()
                                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPanelMensagemLayout.createSequentialGroup()
                                        .addComponent(chkURG)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkPSH)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkSYN))
                                    .addGroup(JPanelMensagemLayout.createSequentialGroup()
                                        .addComponent(chkACK)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkRST)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chkFIN)))
                                .addGap(18, 18, 18)
                                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblOptions)
                                    .addComponent(txtOptions, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(JPanelMensagemLayout.createSequentialGroup()
                                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JPanelMensagemLayout.createSequentialGroup()
                                        .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lblIpOrigem)
                                                .addComponent(txtIpDestino, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                                            .addComponent(txtIpOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                            .addComponent(lblPortaOrigem)
                                            .addComponent(txtPortaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblPortaDestino)
                                            .addComponent(txtPortaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblChecksum)
                                            .addComponent(txtChecksum, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(lblJanelaRecepcao)
                                    .addComponent(txtJanelaRecepcao, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNumeroSequencia)
                                    .addComponent(lblTamanhoCabecalho)
                                    .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(lblNumeroRecepcao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtTamanhoCabecalho, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNumeroRecepcao, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblUrgentPointer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(txtUrgentPointer, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(txtNumeroSequencia, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblIpDestino)))
                    .addGroup(JPanelMensagemLayout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(txtTamanhoDadosPacote, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanelMensagemLayout.setVerticalGroup(
            JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelMensagemLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPortaOrigem)
                    .addComponent(lblNumeroSequencia)
                    .addComponent(lblIpOrigem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPortaOrigem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroSequencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtIpOrigem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtIpDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtPortaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNumeroRecepcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JPanelMensagemLayout.createSequentialGroup()
                        .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPortaDestino)
                            .addComponent(lblIpDestino)
                            .addComponent(lblNumeroRecepcao))
                        .addGap(26, 26, 26)))
                .addGap(18, 18, 18)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJanelaRecepcao)
                    .addComponent(lblChecksum)
                    .addComponent(lblTamanhoCabecalho))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJanelaRecepcao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtChecksum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTamanhoCabecalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkPSH)
                        .addComponent(chkURG)
                        .addComponent(chkSYN)
                        .addComponent(lblOptions))
                    .addComponent(lblUrgentPointer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkACK)
                    .addComponent(chkRST)
                    .addGroup(JPanelMensagemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chkFIN)
                        .addComponent(txtOptions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtUrgentPointer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(lblTamanhoDadosPacote)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTamanhoDadosPacote, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Histórico"));

        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JPanelMensagem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(JPanelMensagem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(158, 158, 158))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIpOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIpOrigemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIpOrigemActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TelaEnvio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelaEnvio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelaEnvio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelaEnvio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaEnvio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JPanelMensagem;
    private javax.swing.JCheckBox chkACK;
    private javax.swing.JCheckBox chkFIN;
    private javax.swing.JCheckBox chkPSH;
    private javax.swing.JCheckBox chkRST;
    private javax.swing.JCheckBox chkSYN;
    private javax.swing.JCheckBox chkURG;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblChecksum;
    private javax.swing.JLabel lblIpDestino;
    private javax.swing.JLabel lblIpOrigem;
    private javax.swing.JLabel lblJanelaRecepcao;
    private javax.swing.JLabel lblNumeroRecepcao;
    private javax.swing.JLabel lblNumeroSequencia;
    private javax.swing.JLabel lblOptions;
    private javax.swing.JLabel lblPortaDestino;
    private javax.swing.JLabel lblPortaOrigem;
    private javax.swing.JLabel lblTamanhoCabecalho;
    private javax.swing.JLabel lblTamanhoDadosPacote;
    private javax.swing.JLabel lblUrgentPointer;
    private javax.swing.JTextField txtChecksum;
    private javax.swing.JFormattedTextField txtIpDestino;
    private javax.swing.JTextField txtIpOrigem;
    private javax.swing.JFormattedTextField txtJanelaRecepcao;
    private javax.swing.JFormattedTextField txtNumeroRecepcao;
    private javax.swing.JFormattedTextField txtNumeroSequencia;
    private javax.swing.JTextField txtOptions;
    private javax.swing.JFormattedTextField txtPortaDestino;
    private javax.swing.JFormattedTextField txtPortaOrigem;
    private javax.swing.JTextField txtTamanhoCabecalho;
    private javax.swing.JTextField txtTamanhoDadosPacote;
    private javax.swing.JTextField txtUrgentPointer;
    // End of variables declaration//GEN-END:variables
}
