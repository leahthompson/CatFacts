package catfacts.com.catfacts;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load the cat fact
        getCatFact(null);
    }

    public void getCatFact(View view)
    {
        final TextView catFact = (TextView) findViewById(R.id.txtCatFact);

        //Create new RequestQueue
        RequestQueue myQueue = Volley.newRequestQueue(this);

        //URL to request one cat fact from CatFacts API
        String myUrl = "http://catfacts-api.appspot.com/api/facts?number=1";

        //Request JSON response from CatFacts API
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    //Get the array of facts
                    JSONArray facts = response.getJSONArray("facts");

                    //Get the first fact (we only asked for one)
                    catFact.setText(facts.getString(0));
                }
                catch (JSONException exception)
                {
                    catFact.setText("There was an error getting a Cat Fact from the CatFacts service. Please try again later.");
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                catFact.setText("There was an error trying to request a Cat Fact for you... does Cat Facts have permission to access the internet?");
            }
        });

        //Add request to queue
        myQueue.add(myRequest);
    }

    public void sendViaSMS(View view)
    {
        //Get current cat fact
        TextView catFact = (TextView) findViewById(R.id.txtCatFact);

        //Create intent to send an SMS message
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);

        //ONLY SMS apps must respond
        smsIntent.setData(Uri.parse("smsto:"));

        //Set message body to the cat fact
        smsIntent.putExtra("sms_body", catFact.getText().toString());

        //If there is an available SMS app
        if (smsIntent.resolveActivity(getPackageManager()) != null)
        {
            //Start the messaging app
            startActivity(smsIntent);
        }
    }
}
