package hr.fer.tel.rassus.stupidudp.client;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Cvor {



    public static void main(String args[]) throws IOException, InterruptedException {
    	
    	int port = ThreadLocalRandom.current().nextInt(1000,10000);
    	String id = Integer.toString(ThreadLocalRandom.current().nextInt(100,999));
    	
    	StartStop s = new StartStop();
    	LinkedList<CvorStruktura> listaSusjeda = new LinkedList<>();
    	CvorStruktura cvor = new CvorStruktura(id, "localhost", Integer.toString(port));

    	Runnable kafkaConsumer = new Consumer(s, listaSusjeda, cvor);
    	Thread th = new Thread(kafkaConsumer);
		th.start();
		
		
		while(!s.started)
			Thread.sleep(100);
    			
        String sendString = "Any string...";

        byte[] rcvBuf = new byte[256];
    }
	
}
