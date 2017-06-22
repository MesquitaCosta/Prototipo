/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacao;

import java.io.Serializable;

/**
 *
 * @author LORD
 */
public class Pacote implements Serializable{
    
    String ipOrigem;
    String ipDestino;
    short portaOrigem;
    short portaDestino;    
    short tamanhoJanela;
    boolean aceito;
    boolean encerra;
    int numeroSequencia;
    int numeroAvisoRecepcao;
    int idMensagem = 0;
    String mensagem;

    public int getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }

    
    public String getIpOrigem() {
        return ipOrigem;
    }

    public void setIpOrigem(String ipOrigem) {
        this.ipOrigem = ipOrigem;
    }

    public String getIpDestino() {
        return ipDestino;
    }

    public void setIpDestino(String ipDestino) {
        this.ipDestino = ipDestino;
    }

    public short getPortaOrigem() {
        return portaOrigem;
    }

    public void setPortaOrigem(short portaOrigem) {
        this.portaOrigem = portaOrigem;
    }

    public short getPortaDestino() {
        return portaDestino;
    }

    public void setPortaDestino(short portaDestino) {
        this.portaDestino = portaDestino;
    }

    public short getTamanhoJanela() {
        return tamanhoJanela;
    }

    public void setTamanhoJanela(short tamanhoJanela) {
        this.tamanhoJanela = tamanhoJanela;
    }

    public boolean isAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }

    public boolean isEncerra() {
        return encerra;
    }

    public void setEncerra(boolean encerra) {
        this.encerra = encerra;
    }

    public int getNumeroSequencia() {
        return numeroSequencia;
    }

    public void setNumeroSequencia(int numeroSequencia) {
        this.numeroSequencia = numeroSequencia;
    }

    public int getNumeroAvisoRecepcao() {
        return numeroAvisoRecepcao;
    }

    public void setNumeroAvisoRecepcao(int numeroAvisoRecepcao) {
        this.numeroAvisoRecepcao = numeroAvisoRecepcao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    
}
