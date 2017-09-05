package br.com.jordan.whatsapp.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.jordan.whatsapp.Adapter.ViewPagerAdapter;
import br.com.jordan.whatsapp.Configuracao.ConfiguracaoFirebase;
import br.com.jordan.whatsapp.Fragment.ContatosFragment;
import br.com.jordan.whatsapp.Fragment.ConversasFragment;
import br.com.jordan.whatsapp.Model.Contato;
import br.com.jordan.whatsapp.Model.Usuario;
import br.com.jordan.whatsapp.R;
import br.com.jordan.whatsapp.Util.Base64Custom;
import br.com.jordan.whatsapp.Util.Preference;

import static android.R.attr.data;
import static br.com.jordan.whatsapp.R.drawable.usuario;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FirebaseAuth auth;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = ConfiguracaoFirebase.getFirebaseAuth();

        toolbar = (Toolbar) findViewById(R.id.toobar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("WhatsApp");
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_search:
                Toast.makeText(this, "Pesquisar", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ic_add:
                abrirCadastroContato();
                return true;
            case R.id.ic_configuracao:
                Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ic_sair:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastroContato(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Novo contato");
        builder.setMessage("E-mail do usuário");
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton("CADASTRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = editText.getText().toString();
                if(email.isEmpty()){
                    Toast.makeText(MainActivity.this, "Insira o e-mail", Toast.LENGTH_SHORT).show();
                }else{
                    //Verificar se o usuário já está cadastrado no database
                    identificadorContato = Base64Custom.toBase64(email);

                    //Recuperando a Instância do Firebase
                    databaseReference = ConfiguracaoFirebase.getFirebase().child("usuarios").child(identificadorContato);

                    //Fazendo verificação do usuario no database apenas uma única vez
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                //recuperando dados do contato a ser adicionado
                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);

                                //Recuperando o email do usuário logado
                                Preference preference = new Preference(MainActivity.this);
                                String identificadorUsuario = preference.getIdentificador();

                                Contato contato = new Contato();
                                contato.setIdentificadoUsuario(identificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setNome(usuarioContato.getNome());

                                databaseReference = ConfiguracaoFirebase.getFirebase();
                                databaseReference.child("contatos").child(identificadorUsuario).child(identificadorContato).setValue(contato);

                            }else{
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logOut() {
        auth.signOut();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ConversasFragment(), getResources().getString(R.string.title_tab_conversas));
        adapter.addFragment(new ContatosFragment(), getResources().getString(R.string.title_tab_contatos));
        viewPager.setAdapter(adapter);

    }
}
