package ttagawa.proximitychat;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class MessageActivity extends AppCompatActivity {
    private static final String LOG_TAG = "MYTAG";
    private String userId;
    private SharedPreferences pref;
    private EditText ed;
    private List<ResultList> list = new ArrayList<>();
    private MyAdapter ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userId = pref.getString("user_id", "");
        ed = (EditText) findViewById(R.id.editText2);
        final Button button = (Button)findViewById(R.id.button2);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //should also check if bool accurate is true
                if (!ed.getText().toString().trim().equals("")) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }
        });
        List<ResultList> rl = new ArrayList<>(10);
        ResultList r = new ResultList();
        r.message = "Testing hi hi hih ihih i hi hi hih ihi hi hidjakjflajdjakldjaldjfajsdlafkldjflkjaldjfladfjk";
        r.userId = "123";
        ResultList r2 = new ResultList();
        r2.message = "Test2 hih ih ih ih i hi iadkf akdjf lajljadjfalsjfkal jkl jdklfaj edlkjdkajfkjkajf a ;a";
        r2.userId = userId;
        rl.add(r);
        rl.add(r2);
        ad = new MyAdapter(MessageActivity.this,R.layout.rowtext,rl);
        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(ad);
    }

    public void postMessage(View v){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        postService service = retrofit.create(postService.class);
        String nickname = pref.getString("nickname", "");
        SecureRandomString ran = new SecureRandomString();
        String message_id = ran.nextString();
        String message = ed.getText().toString();
        Log.i(LOG_TAG,"latitude:"+MainActivity.loc.getLatitude());
        Call<Post> queryResponseCall =
                service.post_message((float) MainActivity.loc.getLatitude(), (float) MainActivity.loc.getLongitude(), userId, nickname, message, message_id);
        ResultList temp = new ResultList();
        temp.message = message;
        temp.userId = userId;
        ad.add(temp);

        //Call retrofit asynchronously
        queryResponseCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Response<Post> response) {
                Log.i(LOG_TAG, "Code is: " + response.code());
                if(response.code() == 200) {
                    if(response.body().result.equals("nok")){
                        Toast.makeText(MessageActivity.this,"Application error, please try again.",Toast.LENGTH_LONG).show();
                        Log.i(LOG_TAG, "The result is: " + response.body());
                    }else {
                        ed.setText("");
                        Log.i(LOG_TAG, "The result is: " + response.body());
                    }
                }else if(response.code() == 500){
                    Toast.makeText(MessageActivity.this,"Server error, please try again.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Toast.makeText(MessageActivity.this,"Error please try sending again",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getMessages(View v){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://luca-teaching.appspot.com/localmessages/")
                .addConverterFactory(GsonConverterFactory.create())	//parse Gson string
                .client(httpClient)	//add logging
                .build();

        getService service = retrofit.create(getService.class);
        Log.i(LOG_TAG,"latitude2:"+MainActivity.loc.getLatitude());

        Call<GetMessages> queryResponseCall =
                service.get_messages((float)MainActivity.loc.getLatitude(),(float)MainActivity.loc.getLongitude(),userId);

        //Call retrofit asynchronously
        queryResponseCall.enqueue(new Callback<GetMessages>() {
            @Override
            public void onResponse(Response<GetMessages> response) {
                Log.i(LOG_TAG, "Code is: " + response.code());
                if(response.code() == 200) {
                    Log.i(LOG_TAG, "The result is: " + response.body());
                    if(response.body().result.equals("ok")){
                        Log.i(LOG_TAG,"response proper");
                        MyAdapter ad = new MyAdapter(MessageActivity.this,R.layout.rowtext,response.body().resultList);
                        ListView lv = (ListView) findViewById(R.id.listView);
                        lv.setAdapter(ad);
                    }else{
                        Toast.makeText(MessageActivity.this,"Application error, please try again.",Toast.LENGTH_LONG).show();
                        Log.i(LOG_TAG, "The result is: " + response.body());
                    }
                }else if(response.code() == 500){
                    Toast.makeText(MessageActivity.this,"Server error, please try again.",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                // Log error here since request failed
                Toast.makeText(MessageActivity.this,"Error, please check your connection and try again.",Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface postService{
        @POST("default/post_message")
        Call<Post> post_message(@Query("lat") float latitude,@Query("lng") float longitude,
                                @Query("user_id") String user_id,@Query("nickname") String nickname,
                                @Query("message") String message,@Query("message_id") String message_id);
    }

    public interface getService{
        @GET("default/get_messages")
        Call<GetMessages> get_messages(@Query("lat") float latitude,@Query("lng") float longitude,
                                       @Query("user_id") String user_id);
    }

}
