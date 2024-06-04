package chat;

import java.io.*;
import java.net.*;

public class Client 
{
	Socket socket;
	//bufferedreader
		BufferedReader br;
		PrintWriter out;
	public Client()
	{
		try {
			System.out.println("sending request to server");
			socket=new Socket("127.0.0.1",8877);
			System.out.println("connection done");
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
			System.out.println("reader started");
			try 
			{
				while(true)
				{
					String msg;
				
					msg = br.readLine();
				
					if(msg.equals("exit"))
					{
						System.out.println("Server terminated the chat");
						break;
					}
					else
						System.out.println("server: "+msg);
				} 
			}catch (IOException e) 
			{
				System.out.println("socket closed");
			}
		};
		new Thread(r1).start();
	}
	
	public void startWriting()
	{
		//thread data send to client
		Runnable r2=()->
		{
			System.out.println("writer started");
			try {
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
					System.out.println("socket closed");
				}
		};
		new Thread(r2).start();
	}
	public static void main(String[] args) 
	{
		System.out.println("This is client");
		new Client();
	}
}
