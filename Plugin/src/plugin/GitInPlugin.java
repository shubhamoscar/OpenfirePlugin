
package plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jivesoftware.openfire.MessageRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.event.SessionEventDispatcher;
import org.jivesoftware.openfire.event.SessionEventListener;
import org.jivesoftware.util.JiveGlobals;
import org.xmpp.packet.JID;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shubham
 */
public class GitInPlugin implements Plugin{
    private static String COMMAND="" ;
    private static final String MESSAGE = "plugin.gitin.message";
    private static final String ENABLED = "plugin.gitin.enabled";
    private static String ADDRESS = "";
    private JID serverAddress;
    private MessageRouter router;
    private GitInSessionEventListener listener = new GitInSessionEventListener();
    public void initializePlugin(PluginManager manager,File pluginDirectory){
        serverAddress = new JID(XMPPServer.getInstance().getServerInfo().getXMPPDomain());
        router = XMPPServer.getInstance().getMessageRouter();
        
        SessionEventDispatcher.addListener(listener);
    }
    
    public void destroyPlugin(){
        SessionEventDispatcher.removeListener(listener);
        
        listener = null;
        serverAddress = null;
        router = null;
    }
   
    public boolean isEnabled(){
        return JiveGlobals.getBooleanProperty(ENABLED, false);
    }
    
    public String getAddress(){
    	return JiveGlobals.getProperty(ADDRESS,"Place and dont remove the project address");
    }
    
    public void setAddress(String address){
    	ADDRESS = address;
    }
    
    public String getMessage(){
    	return JiveGlobals.getProperty(MESSAGE, "Welcome to git-interface. For response enter git commands and run.");
    }
    
    public void setMessage(String message){
    	JiveGlobals.setProperty(MESSAGE,message);
    }
    
    public String getCommand() {
      return JiveGlobals.getProperty(COMMAND, "git");
    }
    
    public void setCommand(String command){
    	COMMAND = command;
    }
    
    public void setEnabled(boolean enable){
        JiveGlobals.setProperty(ENABLED, enable ? Boolean.toString(true):Boolean.toString(false));
    }
    
    public void runCommand() throws IOException{
        if(ADDRESS.equals(null)){
        	setAddress("plugin.gitin.address.missing");
        }
        else if(COMMAND.equals(null)){
        	setCommand("plugin.gitin.command.missing");
        }
        else{
        	try{
        			String cmd = "cmd c/ cd "+getAddress()+" & "+getCommand() ;
        			Runtime run = Runtime.getRuntime();
        			Process pr = run.exec(cmd);
        			BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        			String line = "";
        			while((line=input.readLine())!=null){
        				System.out.println(line);
        				setMessage(line);
        			}
        
        			int exitVal = pr.waitFor();
        			System.out.println("Exited with error code "+exitVal);
        	}
        
        	catch(Exception e){
        		System.out.println(e.toString());
        	}
        }
    }
    private class GitInSessionEventListener implements SessionEventListener{

		@Override
		public void anonymousSessionCreated(
				org.jivesoftware.openfire.session.Session arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void anonymousSessionDestroyed(
				org.jivesoftware.openfire.session.Session arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resourceBound(org.jivesoftware.openfire.session.Session arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sessionCreated(
				org.jivesoftware.openfire.session.Session arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void sessionDestroyed(
				org.jivesoftware.openfire.session.Session arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    
}
