//package ClassicKiryusha;
//
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.telegram.telegrambots.ApiContextInitializer;
//import org.telegram.telegrambots.bots.DefaultBotOptions;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.ApiContext;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
//
//public class Bot extends TelegramLongPollingBot {
//    private static String PROXY_HOST = "stanr.info";
//    private static Integer PROXY_PORT = 1488;
//    private static String PROXY_USER = "telegram";
//    private static String PROXY_PASSWORD = "jHsdfjH1112345";
//
//
//    public static void main(String[] args) {
//        try {
//            ApiContextInitializer.init();
//            TelegramBotsApi botsApi = new TelegramBotsApi();
//            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
//
//            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//            credentialsProvider.setCredentials(new AuthScope(PROXY_HOST, PROXY_PORT),
//                    new UsernamePasswordCredentials(PROXY_USER, PROXY_PASSWORD));
//
//            HttpHost httpHost = new HttpHost(PROXY_HOST, PROXY_PORT);
//            botsApi.registerBot(new Bot());
//        } catch (TelegramApiRequestException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//    public void onUpdateReceived(Update update) {
//        if (update.hasMessage() && update.getMessage().hasText()) {
//            Long chatId = update.getMessage().getChatId();
//            String recievedMessage = update.getMessage().getText();
//
//            SendMessage sendMessage = new SendMessage(chatId, recievedMessage);
//        }
//    }
//
//    public String getBotUsername() {
//        return "ClassicKiryushaBot";
//    }
//
//    public String getBotToken() {
//        return System.getenv("BOT_TOKEN");
//    }
//}
