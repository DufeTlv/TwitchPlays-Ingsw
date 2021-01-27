package javaTwirk;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class TwitchIRC extends ListenerAdapter implements Runnable
{
    private String authToken; // Your oauth password from http://twitchapps.com/tmi
    private String channel; // Some twitch channel ex. #example
    private String name; // Twitch username
    private Configuration configuration;

    private PircBotX bot;

    private int enemiesNumber; // numero nemici
    int[] statesTiles; // il numero di voti per ogni stato della tile


    public TwitchIRC(String authToken,
              String channel,
              String name) {
        this.authToken = authToken;
        this.channel = channel;
        this.name = name;

        this.statesTiles = new int[2];

        initialize();
    }

    private void initialize() {
        this.configuration = new Configuration.Builder()
                .setAutoNickChange(false) //Twitch doesn't support multiple users
                .setOnJoinWhoEnabled(false) //Twitch doesn't support WHO command
                .addServer("irc.twitch.tv")
                .setName(this.name)
                .setServerPassword(/*"oauth:" + */this.authToken)
                .addAutoJoinChannel("#" + this.channel)
                .addListener(this)
                .buildConfiguration();

        bot = new PircBotX(this.configuration);
    }

    public void setAuthToken(String authToken) {
        this.authToken = "oauth:" + authToken;
        initialize();
    }
    public void setChannel(String channel) {
        this.channel = "#" + channel;
        initialize();
    }

    public void setName(String name) {
        this.name =name;
        initialize();
    }
    
    public String getAuthToken() {
    	return this.authToken;
    }
    
    public String getChannel() {
    	return this.channel;
    }

    public String getName() {
    	return this.name;
    }


    public Configuration getConfiguration() {
        return this.configuration;
    }

    // La funzione onGenericMessage descrive quello che succede ogni volta che viene letto un messaggio dalla chat
    @Override
    public void onGenericMessage(GenericMessageEvent event) throws Exception {
        if(event.getMessage().contentEquals("#water"))
            statesTiles[0]++;
        else if(event.getMessage().contentEquals("#ice"))
            statesTiles[1]++;
        else if(event.getMessage().contentEquals("#addenemies"))
        	enemiesNumber++;

        //System.out.println("Numero nemici: " + enemiesNumber);

    }

    public int getEnemiesNumber() {
        int tmp = enemiesNumber;

        enemiesNumber = 0; //reset

        return tmp;
    }

    public int getState() {
        //l'indice del valore più alto nell'array
        /*int maxIndex = Arrays.stream(statesTiles)
                .boxed()
                .collect(Collectors.toList())
                .indexOf(Math.max(statesTiles[0], statesTiles[1]));

        statesTiles = new int[2]; //reset

        return maxIndex;*/
    	
    	int max = 0;
    	if(statesTiles[0] < statesTiles[1])
    		max = 1;
    	
    	statesTiles[0] = statesTiles[1] = 0;
    	
    	return max;
    }

    public void startBotX() throws IOException, IrcException {
        this.bot.startBot();
    }

    public void closeBotX() {
        this.bot.close();
    }

    @Override
    public void run() {
        try {
            startBotX();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IrcException e) {
            e.printStackTrace();
        }
    }
}
