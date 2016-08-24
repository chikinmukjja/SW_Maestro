package com.soma.junwoo.wampusworld;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


public class LankingActivity extends AppCompatActivity {

    private Intent playIntent;
    public static String SERVER_ADRESS = "https://wampusworld.herokuapp.com";

    public interface WampusLanking {

        @GET("/users")
        Call<List<Lanking>> getLanklist();


        @POST("/users")
        Call<LankingActivity.Lanking> addLank(@Body Lanking lankingData);

    }


    public class WampusSever {

        public WampusLanking getService(){
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_ADRESS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            WampusLanking service =  retrofit.create(WampusLanking.class);
            return service;
        }
    }


    public class ListLanking{
        private List<Lanking> elements;
        public List<Lanking> getElements()
        {
            return elements;
        }
    }

    public class Lanking {

        String id;
        String name;
        String time;
        int score;

        public String getID() {
            return id;
        }

        public void setRet(String _id) {
            this.id = _id;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanking);
        playIntent = getIntent();


        final String[] result = {"null"};

        WampusLanking service = new WampusSever().getService();
        Call<List<Lanking>> c = service.getLanklist();

        c.enqueue(new Callback<List<Lanking>>() {
            @Override
            public void onResponse(Call<List<Lanking>> call, Response<List<Lanking>> response) {
                List<Lanking> elems = response.body();
                getLanking(elems);
                Log.i("KK",elems.get(0).name);
                //Toast.makeText(getApplicationContext(),response.body().toString(),Toast.LENGTH_SHORT).show();
                Log.i("KK",response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Lanking>> call, Throwable t)
            {
                t.printStackTrace();
                Log.i("KK","fail");
                result[0] = "false";
            }
        });


        if(playIntent.getStringExtra("id")!=null) {
            Lanking lankingData = new Lanking();
            lankingData.id = playIntent.getStringExtra("id");
            lankingData.name = playIntent.getStringExtra("name");
            lankingData.time = playIntent.getStringExtra("time");
            lankingData.score = playIntent.getIntExtra("score", 100);
            //Toast.makeText(getApplicationContext(), "" + playIntent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
            Call<Lanking> post = service.addLank(lankingData);

            post.enqueue(new Callback<Lanking>() {
                @Override
                public void onResponse(Call<Lanking> call, Response<Lanking> response) {
                    //Toast.makeText(getApplicationContext(), response.body().toString(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Lanking> call, Throwable t) {

                }
            });

        }




    }


    public void getLanking(List<Lanking> elems) {
        final String[] setting = {
                " 1 ", " 2 ", " 3 ", " 4 ",
                " 5 ", " 6 ", " 7 ", " 8 ",
                " 9 ", "10 ", "11 ", "12 ",
                "13 ", "14 ", "15 ", "16 ",
                "17 ", "18 ", "19 ", "20 ",
        };

//DESC 내림차순

        Collections.sort(elems, new Comparator<Lanking>() {

            public int compare(Lanking obj1, Lanking obj2)

            {

                // TODO Auto-generated method stub
                return (obj1.score < obj2.score) ? -1 : (obj1.score > obj2.score) ? 1 : 0;

            }

        });

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.inner_layout);
        // setContentView(linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < setting.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.each_item, null);
            ViewHolder holder = new ViewHolder();
            view.setPadding(0, 20, 0, 20);
            view.setBackgroundColor(Color.WHITE);
            holder.rank = (TextView) view.findViewById(R.id.lanktext); // title
            holder.rank.setText(setting[i]);
            holder.rank.setTextColor(Color.RED);
            holder.rank.setTypeface(Typeface.DEFAULT_BOLD);
            holder.rank.setPaintFlags(holder.rank.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            if (i < elems.size()) {

                holder.time = (TextView) view.findViewById(R.id.timetext); // artist name
                holder.time.setText(elems.get(i).time);

                holder.name = (TextView) view.findViewById(R.id.nametext); // duration
                holder.name.setText(elems.get(i).name);
                holder.name.setTextColor(Color.BLUE);

                holder.score = (TextView) view.findViewById(R.id.scoretext); // duration
                holder.score.setText(" " + elems.get(i).score + "s");
                holder.score.setTextColor(Color.DKGRAY);
            } else {

            }
            linearLayout.addView(view);
        }

        LinearLayout myScoreLayout = (LinearLayout) findViewById(R.id.best_score);
        myScoreLayout.setOrientation(LinearLayout.VERTICAL);
        int myRank=0;

        for (Lanking i : elems) {
            myRank++;
            Log.i("KK"," "+i.name+" " + playIntent.getStringExtra("name"));
            if (i.name.equalsIgnoreCase( playIntent.getStringExtra("name"))) {


                View view = getLayoutInflater().inflate(R.layout.my_best_score, null);
                ViewHolder bestScoreholder = new ViewHolder();
                view.setBackgroundColor(Color.WHITE);

                bestScoreholder.rank = (TextView) view.findViewById(R.id.lanktext2); // title
                bestScoreholder.rank.setText(" "+myRank+" ");
                bestScoreholder.rank.setTextColor(Color.RED);
                bestScoreholder.rank.setTypeface(Typeface.DEFAULT_BOLD);
                bestScoreholder.rank.setPaintFlags(bestScoreholder.rank.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

               // bestScoreholder.rank.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));


                bestScoreholder.time = (TextView) view.findViewById(R.id.timetext2); // artist name
                bestScoreholder.time.setText(i.time);

                bestScoreholder.name = (TextView) view.findViewById(R.id.nametext2); // duration
                bestScoreholder.name.setText(i.name);
                bestScoreholder.name.setTextColor(Color.BLUE);

                bestScoreholder.score = (TextView) view.findViewById(R.id.scoretext2); // duration
                bestScoreholder.score.setText(" " + i.score + "s");
                bestScoreholder.score.setTextColor(Color.DKGRAY);

                myScoreLayout.addView(view);
                break;
            }
        }



    }

    public final class ViewHolder {
        public TextView rank;
        public TextView time;
        public TextView name;
        public TextView score;
        //A class for the ViewHolder

    }

}
