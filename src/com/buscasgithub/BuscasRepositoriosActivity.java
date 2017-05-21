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
import android.content.Intent;
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


public class BuscasRepositoriosActivity extends Activity {

	private TextView txtLogin;
	private TextView txtAvatar_url;
	private Usuario  objetoPessoa;
	private ListView listUsuarios;
	List<String> opcoes;
	static final String LOG_TAG = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscas_repositorios);
		
		final EditText editText = (EditText) findViewById(R.id.editBuscaRepositorios);
		final Button button = (Button) findViewById(R.id.btnRepositorios);
        listUsuarios = (ListView) findViewById(R.id.listBuscasRepositorios);

		
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
Log.d(BuscasRepositoriosActivity.LOG_TAG,"https://api.github.com/search/repositories?q=" + editText.getText().toString());	
 	
		final ConsumirJsonUsuarios consumirJson = new ConsumirJsonUsuarios();
		consumirJson.execute("https://api.github.com/search/repositories?q=" + editText.getText().toString());

            }
        });			     
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	class ConsumirJsonUsuarios extends AsyncTask<String, Void, List<Usuario>> {	
		ProgressDialog dialog;
		String jsonStringPrincipal;
		
		//Exibe pop-up indicando que est� sendo feito o download do JSON
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			  dialog = ProgressDialog.show(BuscasRepositoriosActivity.this, "Aguarde",
              "Fazendo download do JSON");
			
		}

		//Acessa o servi�o do JSON e retorna a lista de usuario
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


		//Depois de executada a chamada do servi�o 
		@Override
		protected void onPostExecute(List<Usuario> result) {
			super.onPostExecute(result);
			dialog.dismiss();
			
			if (result.size() > 0) {
				ArrayAdapter<Usuario> adapter = new ArrayAdapter<Usuario>(
						 BuscasRepositoriosActivity.this,android.R.layout.simple_list_item_multiple_choice, result);
				listUsuarios.setAdapter(adapter);
					
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BuscasRepositoriosActivity.this)
						.setTitle("Erro")
						.setMessage("N�o foi poss�vel acessar as informa��es!!")
						.setPositiveButton("OK", null);
				builder.create().show();
				
			}
			
		}
		
 	//Retorna uma lista de pessoas com as informa��es retornadas do JSON
		private List<Usuario> getUsuario(String jsonString) {
			List<Usuario> usuarios = new ArrayList<Usuario>();
			try {
Log.d(BuscasRepositoriosActivity.LOG_TAG,"PASSO FINAL");					
			JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray items = jsonObj.getJSONArray("items");
			
			for (int i = 0; i < items.length(); i++) {
				
			JSONObject c = items.getJSONObject(i); 
				
Log.d(BuscasRepositoriosActivity.LOG_TAG,"LOGIN FINAL uhu= "  + c.getString("login"));	
Log.d(BuscasRepositoriosActivity.LOG_TAG,"AVATAR FINAL uhu= " + c.getString("avatar_url"));	

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
		
		//Converte objeto InputStream para String
		private String getStringFromInputStream(InputStream is) {

			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();

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
}