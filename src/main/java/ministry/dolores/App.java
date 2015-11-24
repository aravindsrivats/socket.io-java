package ministry.dolores;

import io.socket.client.*;
import io.socket.emitter.*;
import io.socket.emitter.Emitter.Listener;
import io.socket.engineio.client.EngineIOException;
import io.socket.engineio.client.Transport;
import io.socket.engineio.client.transports.WebSocket;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.json.JSONObject;

public class App {
    private Socket socket;
	public App() throws Exception {
        IO.Options opts = new IO.Options();
        opts.transports = new String[] {WebSocket.NAME};
        opts.forceNew = true;
        opts.host = "http://localhost";
		socket = IO.socket("http://localhost:8001", opts);
		
		IO.setDefaultHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
			//TODO: Make this more restrictive
			return true;
			}
			});
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            	System.out.println("run");
  		        socket.emit("event", "argument1");
            }
        }).on("event", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                System.out.println(obj.toString());
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            	System.out.println("not run");
            }
        }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            	System.out.println("Connect Error");
            	EngineIOException obj = (EngineIOException) args[0];
                obj.printStackTrace();
            }
        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            	System.out.println("Connect Timeout");
                EngineIOException obj = (EngineIOException) args[0];
                obj.printStackTrace();
            }
        });
		socket.connect();
	}

    public static void main(String[] args) {
        try {
        	System.out.println("yay");
            new App();
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
}
