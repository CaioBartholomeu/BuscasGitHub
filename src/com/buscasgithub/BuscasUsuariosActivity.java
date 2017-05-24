package com.buscasgithub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug.IntToString;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class BuscasUsuariosActivity extends Activity {
	private ListView listUsuarios;
	static final String LOG_TAG = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscas_usuarios);
		
		//Atrubuição de componentes da tela
		final EditText editText = (EditText) findViewById(R.id.editBuscaUsuarios);
		final Button button = (Button) findViewById(R.id.btnUsuarios);
        listUsuarios = (ListView) findViewById(R.id.listBuscaUsuarios);

		//Realizar busca do conteúdo digitado
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
        
        	
        //Verificacao da internet 
        if (testarConexao() == true) {
        //Chamada objeto para realizar a busca e atribuição do conteúdo do Json
		final ConsumirJsonUsuarios consumirJson = new ConsumirJsonUsuarios();
		consumirJson.execute("https://api.github.com/search/users?q=" + editText.getText().toString());
            }
            else
            {
              AlertDialog.Builder builder = new AlertDialog.Builder(
    					BuscasUsuariosActivity.this)
    					.setTitle(":(")
    					.setMessage("Sem internet!")
    					.setPositiveButton("Tente mais tarde", null);
    		  builder.create().show();	
    		 
            }
            }
        });	

			     
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	class ConsumirJsonUsuarios extends AsyncTask<String, Void, List<Usuario>> {	
		ProgressDialog dialog;
		String jsonStringPrincipal;
		
		//Exibe pop-up da busca
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			  dialog = ProgressDialog.show(BuscasUsuariosActivity.this, "Só um momento",
              "Realizando a busca de usuários");
			
		}

		//Acessa o serviço do JSON e retorna a lista de usuario
		@Override
		protected List<Usuario> doInBackground(String... params) {
			String urlString = params[0];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(urlString);
			try {
				HttpResponse response = httpclient.execute(httpget);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					String json = getStringFromInputStream(instream);
					instream.close();
					List<Usuario> usuario = getUsuario(json);					
					return usuario;
				}
			} catch (Exception e) {
				Log.e("Erro", "Falha ao acessar Web service", e);
			}
			return null;
		}


		//Depois de executada a chamada do serviço 
		@Override
		protected void onPostExecute(List<Usuario> result) {
			super.onPostExecute(result);
			dialog.dismiss();
			
			//Se houver conteúdo, atribuir na lista
			if (result.size() > 0) {
				ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(
						 BuscasUsuariosActivity.this,android.R.layout.simple_list_item_multiple_choice, result);
				listUsuarios.setAdapter(adapter);
					
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BuscasUsuariosActivity.this)
						.setTitle(":(")
						.setMessage("Nenhum usuário encontrado!")
						.setPositiveButton("Buscar outros", null);
				builder.create().show();
				
			}
			
		}
		
 	    //Retorna o Array com os objetos da busca
		private List<Usuario> getUsuario(String jsonString) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			try {
			
			//items: nome do array da API Json para busca de usuários	
			JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray items = jsonObj.getJSONArray("items");
			
            //Percorre todos os objetos e atribui para o objeto usuario
			for (int i = 0; i < items.length(); i++) {				
			JSONObject c = items.getJSONObject(i); 

			//login e avatar_url: objetos do array da API Json para busca de usuários
            Usuario usuario = new Usuario();
            usuario.setlogin(c.getString("login"));
            usuario.setavatar_url(c.getString("avatar_url"));
			usuarios.add(usuario);				
				}

			} catch (JSONException e) {
				Log.e("Erro", "Erro no parsing do JSON", e);
			}
			return usuarios;
		}
		
		//Converter InputStream para String
		private String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder  sb = new StringBuilder();

			String line;
			try {

				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return sb.toString();

		}

	}
	
	public boolean testarConexao(){
    	ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); 
                if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) { 
                    return true; 
                } else { 
                    return false; 
                }  
}
}
