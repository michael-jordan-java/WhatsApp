package br.com.jordan.whatsapp.Model;

/**
 * Created by User on 20/08/2017.
 */

public class Contato {
    private String identificadoUsuario;
    private String nome;
    private String email;

    public Contato() {

    }

    public String getIdentificadoUsuario() {
        return identificadoUsuario;
    }

    public void setIdentificadoUsuario(String identificadoUsuario) {
        this.identificadoUsuario = identificadoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
