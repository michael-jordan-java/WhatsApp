package br.com.jordan.whatsapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.jordan.whatsapp.Configuracao.ConfiguracaoFirebase;
import br.com.jordan.whatsapp.Model.Usuario;
import br.com.jordan.whatsapp.R;
import br.com.jordan.whatsapp.Util.Base64Custom;
import br.com.jordan.whatsapp.Util.Preference;

public class CadastroUsuarioActivity extends AppCompatActivity {
    private EditText etNome, etEmail, etSenha;
    private Button btCadastrar;
    private Usuario usuario;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        etNome = (EditText) findViewById(R.id.etNome);
        etEmail = (EditText) findViewById(R.id.etEmailCadastro);
        etSenha = (EditText) findViewById(R.id.etSenhaCadastro);
        btCadastrar = (Button) findViewById(R.id.btCadastrar);
        btCadastrar.setOnClickListener(onClickCadastrar);
    }

    private View.OnClickListener onClickCadastrar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!etNome.getText().toString().trim().isEmpty() && !etEmail.getText().toString().trim().isEmpty() && !etSenha.getText().toString().trim().isEmpty()) {
                usuario = new Usuario();
                usuario.setNome(etNome.getText().toString().trim());
                usuario.setEmail(etEmail.getText().toString().trim());
                usuario.setSenha(etSenha.getText().toString().trim());
                cadastrarUsuario();
            } else {
                Toast.makeText(CadastroUsuarioActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void cadastrarUsuario() {
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_SHORT).show();

                    String idUsuario = Base64Custom.toBase64(usuario.getEmail());

                    //Recuperando o id do usuario cadastrado no Firebase
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    Preference preference = new Preference(CadastroUsuarioActivity.this);
                    preference.salvarUsuarioPreferences(idUsuario);

                    abrirLoginUsuario();
                } else {
                    String erro = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha mais forte, contendo mais caracteres e com letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "O email digitado é inválido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esse e-mail já existe!";
                    } catch (Exception e) {
                        erro = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erro, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirLoginUsuario(){
        startActivity(new Intent(CadastroUsuarioActivity.this, LoginActivity.class));
    }
}
