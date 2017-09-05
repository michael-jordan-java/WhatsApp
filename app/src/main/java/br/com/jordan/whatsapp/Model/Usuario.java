package br.com.jordan.whatsapp.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import br.com.jordan.whatsapp.Configuracao.ConfiguracaoFirebase;

/**
 * Created by User on 18/08/2017.
 */

public class Usuario {
    private String id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
    }
    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("usuarios").child(getId()).setValue(this);
    }

    //Quando o firebase for salvar os dados no banco ele não salvará o Id
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    //Quando o firebase for salvar os dados no banco ele não salvará a Senha
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
