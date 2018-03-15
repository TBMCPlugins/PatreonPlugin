import buttondevteam.website.io.IOHelper;
import buttondevteam.website.io.Response;
import buttondevteam.website.page.Page;
import com.patreon.PatreonAPI;
import com.patreon.PatreonOAuth;
import com.sun.net.httpserver.HttpExchange;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;

@RequiredArgsConstructor
public class PatreonPage extends Page {
    private final FileConfiguration config;

    @Override
    public String GetName() {
        return "patreon";
    }

    @Override
    public Response handlePage(HttpExchange httpExchange) {
        val qkv = IOHelper.parseQueryString(httpExchange);
        boolean connected = qkv.containsKey("code") && qkv.containsKey("state");
        try {
            if (connected) {
                String secret = config.getString("secret", null);
                if (secret == null)
                    throw new RuntimeException("Cannot find secret in config file!");
                PatreonOAuth poac = new PatreonOAuth("zXFpLkmoTd9ex0RhdhNMoz42lLx4_okULp08EqBRcuGVnEn7e6Pq7zk-a2gMXqSX", secret, "https://server.figytuna.com/patreon");
                val tokens = poac.getTokens(qkv.get("code")); //TODO: Save user data, handle rewards
                //TODO: Implement login page in BWM and add redirect param to it
            /*String acctoken=config.getString("accesstoken", null);
            if(acctoken==null)
                throw new RuntimeException("Cannot find access token in config file!");*/
                PatreonAPI papi = new PatreonAPI(tokens.getAccessToken());
                try {
                    papi.fetchCampaigns().get().get(0).getPledges().get(0).getReward().getAmountCents();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return IOHelper.Redirect("https://tbmcplugins.github.io/patreon?connected=" + (connected ? "yess" : "noo"), httpExchange);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
