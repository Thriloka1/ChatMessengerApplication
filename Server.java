package chat;

import java.io.*;
import java.net.*;

public class Server 
{
	ServerSocket server;
	Socket socket;
	//bufferedreader
	BufferedReader br;
	PrintWriter out;
	//constructor
	public Server() 
	{
		try {
			server=new ServerSocket(8877);
			System.out.println("server is ready to accept connection");
			System.out.println("waiting");
			socket=server.accept();
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintWriter(socket.getOutputStream());
			startReading();
			startWriting();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void startReading()
	{
		//thread reading
		Runnable r1=()->{
			System.out.println("reader start writing");
			try {
				while(true)
				{
					String msg;
					msg = br.readLine();
					if(msg.equals("exit"))
					{
						System.out.println("client has stopped the chat");
						socket.close();
						break;
					}
					else
						System.out.println("client : "+msg);
				}
			}
			catch(Exception e)
			{
				System.out.println("socket closed");
			}
		};
		new Thread(r1).start();
	}
	public void startWriting()
	{
		//thread data send to client
		Runnable r2=()->{
			System.out.println("writer started");
			try 
			{
				while(!socket.isClosed()) 
				{	
					BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
					String content=br1.readLine();
					out.println(content);
					out.flush();
					if(content.equals("exit"))
					{
						socket.close();
						break;
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("Socket Closed");
			}
		};
		new Thread(r2).start();
	}
	public static void main(String[] args) 
	{
		System.out.println("This is server.. going to start server");
		new Server();
	}
}
