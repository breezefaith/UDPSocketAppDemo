package com.example.breezefaith.udpsocketdemo;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Button button_send;
    private EditText editText_address,
            editText_port,
            editText_sendMessage;
    private TextView textView_receiveMessage;
    private DatagramSocket udpSocket;
    private DatagramPacket sendDatagramPacket;
    private DatagramPacket receiveDatagramPacket;
    private static final int MAX_DATA_PACKET_LENGTH = 256;
    private byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];
    private byte[] receiveBuffer = new byte[MAX_DATA_PACKET_LENGTH];
    private boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        button_send=(Button)findViewById(R.id.button_send);
        editText_address=(EditText)findViewById(R.id.editText_address);
        editText_port=(EditText)findViewById(R.id.editText_port);
        editText_sendMessage=(EditText)findViewById(R.id.editText_sendMessage);
        textView_receiveMessage=(TextView)findViewById(R.id.textView_receiveMessage);

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address=editText_address.getText().toString();

                int port=Integer.parseInt(editText_port.getText().toString());;
                Toast.makeText(MainActivity.this,String.valueOf(port),Toast.LENGTH_SHORT).show();
                String sendMessage=editText_sendMessage.getText().toString();
                String receiveMessage=null;
                try {
                    udpSocket=new DatagramSocket();
                    udpSocket.setSoTimeout(5000);
                    sendDatagramPacket=new DatagramPacket(buffer,MAX_DATA_PACKET_LENGTH);
                    byte[] out=sendMessage.getBytes();
                    sendDatagramPacket.setData(out);
                    sendDatagramPacket.setLength(out.length);

                    sendDatagramPacket.setAddress(InetAddress.getByName(address));
                    sendDatagramPacket.setPort(port);
                    udpSocket.send(sendDatagramPacket);
                    Toast.makeText(MainActivity.this,"发送成功",Toast.LENGTH_SHORT).show();
                    receiveDatagramPacket=new DatagramPacket(receiveBuffer,MAX_DATA_PACKET_LENGTH);
                    udpSocket.receive(receiveDatagramPacket);
                    receiveMessage=new String(receiveDatagramPacket.getData());
                    textView_receiveMessage.append(receiveMessage);

                } catch (SocketException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error")
                            .setMessage(e.getMessage())
                            .create()
                            .show();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error")
                            .setMessage(e.getMessage())
                            .create()
                            .show();
                } catch (IOException e) {
                    e.printStackTrace();
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Error")
                            .setMessage(e.getMessage())
                            .create()
                            .show();
                }
           }
        });
    }
}

