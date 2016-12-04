package com.example.mayur.socketclient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.example.mayur.socketclient.R;
import com.example.mayur.socketclient.TCPClient;

/**
 * @author Prashant Adesara
 * Display Activity with sending messages to server
 * */

@SuppressLint("NewApi")
public class MainActivity extends Activity
{

    private String Msg = "";
    private TCPClient mTcpClient = null;
    private connectTask conctTask = null;

    private Context context;

    private String ip;

    private Stack<String> backupPath;

    private ListView listView;
    private PathListAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        final EditText editText = (EditText) findViewById(R.id.editText);
        Button connect = (Button)findViewById(R.id.button);
        Button back = (Button)findViewById(R.id.back);

        backupPath = new Stack<>();
        backupPath.push("ALL");

        listView = (ListView) findViewById(R.id.listView);
        adapter = new PathListAdapter(this);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        backupPath.push(adapter.getPath(i));
                        mTcpClient.sendMessage(adapter.getPath(i));
                    }
                }
        );

        mTcpClient = null;

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() > 0) {

                    ip = editText.getText().toString();

                    // connect to the server
                    conctTask = new connectTask();
                    conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!backupPath.isEmpty()) {
                    backupPath.pop();

                    mTcpClient.sendMessage(backupPath.peek());
                }
            }
        });
    }

    /**
     * @author Prashant Adesara
     * receive the message from server with asyncTask
     * */
    public class connectTask extends AsyncTask<String,String,TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message)
        {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(ip ,new TCPClient.OnMessageReceived()
            {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message)
                {
                    try
                    {
                        //this method calls the onProgressUpdate
                        publishProgress(message);
                        if(message!=null)
                        {
                            System.out.println("Return Message from Socket::::: >>>>> "+message);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            mTcpClient.run();
            if(mTcpClient!=null)
            {
                mTcpClient.sendMessage("Initial Message when connected with Socket Server");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            String message = values[0];

            if(message.equals("Server connected"))
                mTcpClient.sendMessage("ALL");


            try{

                try {
                    //path list
                    List<String> sources = new ArrayList<>();

                    JSONArray array = JSON.parseArray(message);

                    for (int i = 0; i < array.size(); i++) {
                        sources.add(array.get(i).toString());
                    }

                    adapter.setSources(sources);
                    adapter.notifyDataSetChanged();
                }catch(Exception e){
                    //download
                    FileBean bean = JSON.parseObject(message,FileBean.class);

                    File f = new File(FileUtil.PATH + bean.getName());

                    Toast.makeText(context,"Saved to " + f.getPath() ,Toast.LENGTH_SHORT).show();

                    FileUtil.writeTextFile(f,bean.getContent());
                }
            }catch (Exception e){

            }

        }
    }

    @Override
    protected void onDestroy()
    {
        try
        {
            System.out.println("onDestroy.");
            mTcpClient.sendMessage("bye");
            mTcpClient.stopClient();
            conctTask.cancel(true);
            conctTask = null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }


}