package br.edu.ifsp.tcc.apprepublic.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import br.edu.ifsp.tcc.apprepublic.Api.HomeService;
import br.edu.ifsp.tcc.apprepublic.Api.RESTService;
import br.edu.ifsp.tcc.apprepublic.Api.RequestService;
import br.edu.ifsp.tcc.apprepublic.Api.UserService;
import br.edu.ifsp.tcc.apprepublic.model.home.HomeEntity;
import br.edu.ifsp.tcc.apprepublic.model.user.User;
import br.edu.ifsp.tcc.apprepublic.mvp.ContactPropMVP;
import br.edu.ifsp.tcc.apprepublic.mvp.ContactUserMVP;
import br.edu.ifsp.tcc.apprepublic.view.ContactUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUserPresenter implements ContactUserMVP.Presenter {

    private ContactUserMVP.View view;
    private Context context;
    public ContactUserPresenter(ContactUserMVP.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void recuperarUser(long userId) {
        Log.d("ContactPropPresenter", "Recuperando informações para o homeId: " + userId);

        UserService userService = RESTService.getUserService();
        Call<User> call = userService.getUserById(getAuthorizationToken(), userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // Aqui você pode usar os detalhes da casa recuperada
                    // Exemplo: view.showHomeDetails(homeEntity);
                    view.infoContat(user);
                } else {
                    view.showMessage("Erro ao recuperar informações da casa");
                    Log.d("ContactPropPresenter", "Falha na resposta do servidor: " + response.code());
                    // Adicione mais informações relevantes aos logs conforme necessário
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                view.showMessage("Erro de conexão ao recuperar informações da casa");
                Log.e("ContactPropPresenter", "Erro durante a solicitação ao servidor", t);
            }
        });
    }

    @Override
    public void messageProp(User user) {
        String telefone = user.getTelefone();

        // Construa a mensagem codificada em URL
        String mensagemCodificada = "Ol%C3%A1%2C%20tudo%20bem%3F%0AEstou%20interessado%20na%20moradia%20que%20vc%20cadastrou!%0AVamos%20conversar%3F";

        // Combine o número de telefone e a mensagem para criar o link
        String linkConversa = "https://wa.me/" + telefone + "?text=" + mensagemCodificada;

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkConversa));
        context.startActivity(intent);
    }

    private String getAuthorizationToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Prefes", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);
        String authorizationHeader = "Bearer " + accessToken;

        return authorizationHeader;
    }

}
