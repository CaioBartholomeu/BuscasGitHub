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
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewDebug.IntToString;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class BuscasRepositoriosActivity extends Activity {
	private ListView listRepositorios;
	static final String LOG_TAG = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscas_repositorios);
		
		//trava layout retrato
		try {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Atrubuição de componentes da tela
		final EditText editText = (EditText) findViewById(R.id.editBuscaRepositorios);
		final Button button = (Button) findViewById(R.id.btnRepositorios);
		listRepositorios = (ListView) findViewById(R.id.listBuscasRepositorios);

		//Realizar busca do conteúdo digitado
        button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
       
 Log.d(BuscasRepositoriosActivity.LOG_TAG,"PARTE0");
        //Verificacao da internet 
        if (testarConexao() == true) {
          //Chamada objeto para realizar a busca e atribuição do conteúdo do Json
          final ConsumirJsonRepositorios consumirJson = new ConsumirJsonRepositorios();
		  consumirJson.execute("https://api.github.com/search/repositories?q=" + editText.getText().toString());		 
          
		  //esconder teclado
		  ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					 editText.getWindowToken(), 0);
        
        }
        else
        {
          AlertDialog.Builder builder = new AlertDialog.Builder(
					BuscasRepositoriosActivity.this)
					.setTitle(":(")
					.setMessage("Sem internet!")
					.setPositiveButton("Tente mais tarde", null);
		  builder.create().show();	
		 
        }
            }
        });			     
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	class ConsumirJsonRepositorios extends AsyncTask<String, Void, List<Repositorio>> {	
		ProgressDialog dialog;
		String jsonStringPrincipal;
		
		//Exibe pop-up da busca
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			  dialog = ProgressDialog.show(BuscasRepositoriosActivity.this, "Só um momento",
              "Realizando a busca de repositórios");
			
		}

		//Acessa o serviço do JSON e retorna a lista de repositorio
		@Override
		protected List<Repositorio> doInBackground(String... params) {
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
					List<Repositorio> repositorio = getRepositorio(json);					
					return repositorio;
				}
			} catch (Exception e) {
				Log.e("Erro", "Falha ao acessar Web service", e);
			}
			return null;
		}


		//Depois de executada a chamada do serviço 
		@Override
		protected void onPostExecute(List<Repositorio> result) {
			super.onPostExecute(result);
			dialog.dismiss();
			
			//Se houver conteúdo, atribuir na lista
			if (result.size() > 0) {
				ArrayAdapter<Repositorio> adapter = new ArrayAdapter<Repositorio>(
						 BuscasRepositoriosActivity.this,android.R.layout.simple_spinner_item, result);
				listRepositorios.setAdapter(adapter);
					
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						BuscasRepositoriosActivity.this)
						.setTitle(":(")
						.setMessage("Nenhum repositório encontrado!")
						.setPositiveButton("Buscar outros", null);
				builder.create().show();
				
			}
			
		}
		
		//Retorna o Array com os objetos da busca
		private List<Repositorio> getRepositorio(String jsonString) {
			List<Repositorio> repositorios = new ArrayList<Repositorio>();
			try {

            //items: nome do array da API Json para busca de repositórios	
			JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray items = jsonObj.getJSONArray("items");
			
            //Percorre todos os objetos e atribui para o objeto repositório
			for (int i = 0; i < items.length(); i++) {	
			JSONObject c = items.getJSONObject(i); 
			
			//name e html_url: objetos do array da API Json para busca de repositório
            Repositorio repositorio = new Repositorio();
            repositorio.setHtml_url(c.getString("html_url"));
            repositorio.setName(c.getString("name"));
            repositorios.add(repositorio);				
				}

			} catch (JSONException e) {
				Log.e("Erro", "Erro no parsing do JSON", e);
			}
			return repositorios;
		}
		
		//Converter InputStream para String
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
