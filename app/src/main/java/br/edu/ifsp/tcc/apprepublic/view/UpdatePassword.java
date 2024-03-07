package br.edu.ifsp.tcc.apprepublic.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import br.edu.ifsp.tcc.apprepublic.mvp.UpdatePasswordMVP;
import br.edu.ifsp.tcc.apprepublic.presenter.UpdatePasswordPresenter;
import br.edu.ifsp.tcc.apptherrepubliq.R;

public class UpdatePassword extends AppCompatActivity implements UpdatePasswordMVP.View {

    private EditText edittextEmail;
    private EditText edittextNovaSenha;
    private EditText edittextConfirmarNovaSenha;
    private Button btnAlterarSenha;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        findById();
        setListeners();
    }

    private void setListeners() {
        btnAlterarSenha.setOnClickListener(v -> {
            UpdatePasswordPresenter presenter = new UpdatePasswordPresenter(this, this);
            String Email = edittextEmail.getText().toString();
            String novaSenha = edittextNovaSenha.getText().toString();
            String confirmarNovaSenha = edittextConfirmarNovaSenha.getText().toString();
            if(novaSenha.equals(confirmarNovaSenha)){
                presenter.alterSenha(Email, novaSenha, getUserId());

            }
         });

    }

    private void findById() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Alterar Senha");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edittextEmail = findViewById(R.id.edittext_email);
        edittextNovaSenha = findViewById(R.id.edittext_novaSenha);
        edittextConfirmarNovaSenha = findViewById(R.id.edittext_confirmarNovaSenha);
        btnAlterarSenha = findViewById(R.id.btn_alterarSenha);
    }

    public Context getContext() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private long getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("Prefes", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("userId", -1); // Retorne -1 se o ID não estiver disponível
    }
}
