package br.com.jordan.whatsapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;

import br.com.jordan.whatsapp.Configuracao.ConfiguracaoFirebase;
import br.com.jordan.whatsapp.Model.Usuario;
import br.com.jordan.whatsapp.R;
import br.com.jordan.whatsapp.Util.Base64Custom;
import br.com.jordan.whatsapp.Util.Preference;

public class LoginActivity extends AppCompatActivity {
    private TextView tvCadastro;
    private EditText etEmail, etSenha;
    private Button btEntrar;
    private FirebaseAuth firebaseAuth;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUsuarioLogado();

        tvCadastro = (TextView) findViewById(R.id.tvCadastrar);
        tvCadastro.setOnClickListener(onClickCadastroUsuario);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etSenha = (EditText) findViewById(R.id.etSenha);
        btEntrar = (Button) findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(onClickEntrar);
    }

    private View.OnClickListener onClickCadastroUsuario = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this, CadastroUsuarioActivity.class));
        }
    };

    private View.OnClickListener onClickEntrar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usuario = new Usuario();
            usuario.setEmail(etEmail.getText().toString());
            usuario.setSenha(etSenha.getText().toString());
            validarLogin();
        }
    };

    private void validarLogin() {
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

        firebaseAuth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Preference preference = new Preference(LoginActivity.this);
                    String identificadorUsuario = Base64Custom.toBase64(usuario.getEmail());

                    abrirTelaPrincipal();
                }else{

                    String erro = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        erro = "Email inválido";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "Senha inválida";
                    } catch (Exception e) {
                        e.printStackTrace();
                        erro = "Erro ao efetuar login!";
                    }
                    Toast.makeText(LoginActivity.this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void verificarUsuarioLogado(){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
