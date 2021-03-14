package com.application.sampleretrofit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView txtNoData;
    MyAdapter myAdapter;
    CommentsAdapter commentsAdapter;
    String strChosen="posts";
    JSONPlaceholderAPI jsonPlaceholderAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        txtNoData=findViewById(R.id.txtNoData);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        /*Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();*/

        // Actually when json data having some null variables will be not sent to the server since the null variables are neglected
        // But if we want to sent those null variables also to the server then use the below line
        Gson gson=new GsonBuilder().serializeNulls().create();

        //follow lines of code are used just to see what is happening behind the api calls, requests and responses(how they actually happen)
        //You can see the full view through the logcat
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        /*OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();*/
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest=chain.request();
                        Request newRequest=originalRequest.newBuilder()
                                .header("Interceptor-Header","xyz")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        jsonPlaceholderAPI=retrofit.create(JSONPlaceholderAPI.class);
        getData();
    }

    private void getData(){

        if(strChosen.equals("posts")){
            //Call<List<Post>> call= jsonPlaceholderAPI.getPosts();
            //Call<List<Post>> call= jsonPlaceholderAPI.getPosts(2);
            //Call<List<Post>> call= jsonPlaceholderAPI.getPosts(2,"id","desc");
            //Call<List<Post>> call= jsonPlaceholderAPI.getPosts(2,3,"id","desc");
            //Call<List<Post>> call= jsonPlaceholderAPI.getPosts(new Integer[]{2,3,4},"id","desc");

            Map<String,String> parameters=new HashMap<>();
            parameters.put("userId","1");
            parameters.put("_sort","id");
            parameters.put("_order","desc");
            Call<List<Post>> call= jsonPlaceholderAPI.getPosts(parameters);
            call.enqueue(new Callback<List<Post>>() {
                @Override
                public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                    if(!response.isSuccessful()){
                        txtNoData.setText(response.code());
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        return;
                    }
                    txtNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    List<Post> posts=response.body();
                    myAdapter=new MyAdapter(MainActivity.this,posts);
                    recyclerView.setAdapter(myAdapter);
                }

                @Override
                public void onFailure(Call<List<Post>> call, Throwable t) {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    txtNoData.setText(t.getMessage());
                }
            });
        }
        else if(strChosen.equals("comments")){
            Call<List<Comment>> call;
            call= jsonPlaceholderAPI.getComments("posts/3/comments");
            call.enqueue(new Callback<List<Comment>>() {
                @Override
                public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                    if(!response.isSuccessful()){
                        txtNoData.setText(response.code());
                        txtNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                        return;
                    }
                    txtNoData.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    List<Comment> commentList=response.body();
                    commentsAdapter=new CommentsAdapter(MainActivity.this,commentList);
                    recyclerView.setAdapter(commentsAdapter);
                }

                @Override
                public void onFailure(Call<List<Comment>> call, Throwable t) {
                    txtNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    txtNoData.setText(t.getMessage());
                }
            });
        }


    }

    void postData(){
        //Post post=new Post(15,"Post","It is a post");

        //Call<Post> call=jsonPlaceholderAPI.sendPost(15,"Title","Text");
        Map<String,String> fields=new HashMap<>();
        fields.put("userId","15");
        fields.put("title","Title");
        fields.put("body","Text");
        Call<Post> call=jsonPlaceholderAPI.sendPost(fields);
        recyclerView.setVisibility(View.GONE);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                txtNoData.setVisibility(View.VISIBLE);
                if(!response.isSuccessful()){
                    txtNoData.setText(response.code());
                    return;
                }
                Post post=response.body();
                String content="";
                content+="Code:"+response.code()+"\n";
                content+="ID:"+post.id+"\n";
                content+="UserId:"+post.userId+"\n";
                content+="title:"+post.title+"\n";
                content+="text:"+post.text+"\n";
                txtNoData.setText(String.format("Posted data is\n%s", content));
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId == R.id.itmComments) {
            strChosen="comments";
            getData();
        }
        else if(itemId==R.id.itmPosts){
            strChosen="posts";
            getData();
        }
        else if(itemId==R.id.itmCreatePost){
            postData();
        }
        else if(itemId==R.id.itmUpdatePost){
            updateData();
        }
        else if(itemId==R.id.itmDeletePost)
            deleteData();

        return super.onOptionsItemSelected(item);
    }

    private void deleteData() {
        Call<Void> call=jsonPlaceholderAPI.deletePost(5);
        recyclerView.setVisibility(View.GONE);
        txtNoData.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                txtNoData.setText(""+response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }


    private void updateData() {
        Post post=new Post(15,"New Title","New Text");
        //Call<Post> call=jsonPlaceholderAPI.putPost(2,post);
        //Call<Post> call=jsonPlaceholderAPI.putPost("abc",2,post);

        Map<String,String> headers=new HashMap<>();
        headers.put("Map-header1","def");
        headers.put("Map-header2","ghi");

        Call<Post> call=jsonPlaceholderAPI.patchPost(headers,2,post);
        recyclerView.setVisibility(View.GONE);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                txtNoData.setVisibility(View.VISIBLE);
                if(!response.isSuccessful()){
                    txtNoData.setText(response.code());
                    return;
                }
                Post post=response.body();
                String content="";
                content+="Code:"+response.code()+"\n";
                content+="ID:"+post.id+"\n";
                content+="UserId:"+post.userId+"\n";
                content+="title:"+post.title+"\n";
                content+="text:"+post.text+"\n";
                txtNoData.setText(String.format("Updated data is\n%s", content));
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}