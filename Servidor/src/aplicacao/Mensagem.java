package aplicacao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//Ao invés de enviar uma string como mensagem, envia um objeto desta classe, que
//contém vários atributos

public class Mensagem implements Serializable{ 
    
    private int idMensagem;
    private int tamanhoMensagem;
    private String nome;
    private String IpOrigem;
    private String IpDestino;
    private String texto;
    private String mensagem;
    private Set<String> usuariosOnline = new HashSet<String>();
    private Action action;//para cada mensagem, já é informada a ação desejada
    private ArrayList<Pacote> pacotes = new ArrayList<Pacote>(); //cada mensagem enviada é dividida em pacotes. Cada item desta lista corresponde a um pacote enviado
    private int mssEmissor = 536;
    private int mssReceptor = 536;
    public int getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(int idMensagem) {
        this.idMensagem = idMensagem;
    }

    public int getTamanhoMensagem() {
        return tamanhoMensagem;
    }

    public void setTamanhoMensagem(int tamanhoMensagem) {
        this.tamanhoMensagem = tamanhoMensagem;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIpOrigem() {
        return IpOrigem;
    }

    public void setIpOrigem(String IpOrigem) {
        this.IpOrigem = IpOrigem;
    }

    public String getIpDestino() {
        return IpDestino;
    }

    public void setIpDestino(String IpDestino) {
        this.IpDestino = IpDestino;
    }
    
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Set<String> getUsuariosOnline() {
        return usuariosOnline;
    }

    public void setUsuariosOnline(Set<String> usuariosOnline) {
        this.usuariosOnline = usuariosOnline;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    
    public enum Action {
        CONECTAR, DISCONECTAR, ENVIAR, USUARIOS_ONLINE;
    }

    public ArrayList<Pacote> getPacotes() {
        return pacotes;
    }

    public void setPacotes(ArrayList<Pacote> pacotes) {
        this.pacotes = pacotes;
    }

    public int getMssEmissor() {
        return mssEmissor;
    }

    public void setMssEmissor(int mssEmissor) {
        this.mssEmissor = mssEmissor;
    }

    public int getMssReceptor() {
        return mssReceptor;
    }

    public void setMssReceptor(int mssReceptor) {
        this.mssReceptor = mssReceptor;
    }
    
    
}