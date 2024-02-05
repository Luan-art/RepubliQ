package br.edu.ifsp.tcc.apprepublic.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.util.Objects;

import br.edu.ifsp.tcc.apprepublic.mvp.MainActivityMVP;
import br.edu.ifsp.tcc.apprepublic.presenter.MainActivityPresenter;
import br.edu.ifsp.tcc.apptherrepubliq.R;

public class LoginActivity extends AppCompatActivity implements MainActivityMVP.View {

    private EditText textUser;
    private EditText textPassword;
    private Button enterButton;
    private TextView cadastrarTextView;
    private SharedPreferences sharedPreferences;
    private CheckBox lembrarDeMim;
    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_main);
        findById();
        setListeners();
        presenter = new MainActivityPresenter(this, this);

        sharedPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        boolean lembrarDeMimChecked = sharedPreferences.getBoolean("lembrarDeMim", false);
        lembrarDeMim.setChecked(lembrarDeMimChecked);
        if (lembrarDeMimChecked) {
            textUser.setText(sharedPreferences.getString("usuario", ""));
        }
    }

    private void setListeners() {
        enterButton.setOnClickListener(v -> {
            String user = textUser.getText().toString();
            String password = textPassword.getText().toString();

            if (user.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                presenter.login(user, password);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("lembrarDeMim", isLembrarDeMimChecked());
                editor.apply();

                if (isLembrarDeMimChecked()) {
                    editor.putString("usuario", user);
                } else {
                    editor.remove("usuario");
                }
                editor.apply();
            }
        });

        cadastrarTextView.setOnClickListener(v -> presenter.cadast());

        lembrarDeMim.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("lembrarDeMim", isChecked);
            editor.apply();
        });

    }

    private void findById() {
        Objects.requireNonNull(getSupportActionBar()).hide();
        textUser = findViewById(R.id.edittext_user);
        textPassword = findViewById(R.id.edittext_password);
        enterButton = findViewById(R.id.button_enter);
        cadastrarTextView = findViewById(R.id.text_cadastrar);
        lembrarDeMim = findViewById(R.id.lembreDeMim);
    }

    public boolean isLembrarDeMimChecked() {
        return lembrarDeMim.isChecked();
    }

    public Context getContext() {
        return this;
    }

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
