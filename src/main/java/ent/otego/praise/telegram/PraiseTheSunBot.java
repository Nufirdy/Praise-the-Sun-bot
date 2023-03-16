package ent.otego.praise.telegram;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import ent.otego.praise.schedule.PraiseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PraiseTheSunBot extends TelegramLongPollingBot {

    private static final String START_COMMAND = "/start";
    private static final String LOCATION_COMMAND = "/location";

    private final Set<Long> awaitReply = ConcurrentHashMap.newKeySet();

    private final String username;

    private final BotDataRepository botDataRepository;
    private final PraiseScheduler praiseScheduler;

    public PraiseTheSunBot(String token, String username,
                           BotDataRepository botDataRepository,
                           PraiseScheduler praiseScheduler) {
        super(token);
        this.username = username;
        this.botDataRepository = botDataRepository;
        this.praiseScheduler = praiseScheduler;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    //TODO рефактор и сделать более абстрактной обработку команд
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (!botDataRepository.exists(message.getChatId())) {
                TelegramChat chat = TelegramChat.builder()
                        .chatId(message.getChatId())
                        .isPrivate(message.getChat().isUserChat())
                        .build();
                botDataRepository.saveOrUpdateChat(chat);
            }
            if (message.hasText()
                    && (message.getText().contains(START_COMMAND)
                    || message.getText().contains(LOCATION_COMMAND))) {
                String[] commandChunks = message.getText().split(" ");
                if (commandChunks.length > 1) {
                    Pair<Float, Float> coordinates;
                    try {
                        coordinates = tryParseCoordinates(commandChunks[1], commandChunks[2]);
                    } catch (Exception e) {
                        SendMessage sendMessage = SendMessage.builder()
                                .chatId(message.getChatId())
                                .text("Forgive me, my friend, but I fear I do not comprehend thy "
                                        + "words. Wouldst thou be so kind as to better describe "
                                        + "thy location, so that I may when sun is highest at "
                                        + "your land.")
                                .parseMode("HTML")
                                .build();
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException ex) {
                            throw new RuntimeException(ex);
                        }
                        awaitReply.add(message.getChatId());
                        return;
                    }
                    saveCoordinatesAndSendConfirm(message, coordinates);
                    return;
                }
                SendMessage sendMessage = SendMessage.builder()
                        .chatId(message.getChatId())
                        .text("Ah, my dear friend! Pray tell, where exactly dost thou find thyself "
                                + "at this moment? And I will know when sun is highest at your "
                                + "land.")
                        .parseMode("HTML")
                        .build();
                try {
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
                awaitReply.add(message.getChatId());
                return;
            }
            if (awaitReply.contains(message.getChatId())
                    && (message.hasLocation() || message.hasText())) {
                Pair<Float, Float> coordinates;
                if (message.hasLocation()) {
                    coordinates =
                            new ImmutablePair<>(message.getLocation().getLatitude().floatValue(),
                                    message.getLocation().getLongitude().floatValue());
                } else {
                    String[] coordinatesChunks = message.getText().split(" ");
                    try {
                        coordinates = tryParseCoordinates(coordinatesChunks[0],
                                coordinatesChunks[1]);
                    } catch (Exception e) {
                        SendMessage sendMessage = SendMessage.builder()
                                .chatId(message.getChatId())
                                .text("Forgive me, my friend, but I fear I do not comprehend thy "
                                        + "words. Wouldst thou be so kind as to better describe "
                                        + "thy location, so that I may when sun is highest at "
                                        + "your land.")
                                .parseMode("HTML")
                                .build();
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException ex) {
                            throw new RuntimeException(ex);
                        }
                        return;
                    }
                }
                saveCoordinatesAndSendConfirm(message, coordinates);
            }
        }
    }

    private void saveCoordinatesAndSendConfirm(Message message, Pair<Float, Float> coordinates) {
        TelegramChat chat = TelegramChat.builder()
                .chatId(message.getChatId())
                .isPrivate(message.getChat().isUserChat())
                .latitude(coordinates.getLeft())
                .longitude(coordinates.getRight())
                .build();
        botDataRepository.saveOrUpdateChat(chat);

        praiseScheduler.schedulePraiseTaskFor(chat);

        awaitReply.remove(message.getChatId());

        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("It pleases me greatly to see you join me. I will contact you "
                        + "when sun's dazzling light is at it's highest, the perfect "
                        + "moment to appreciate the joy and vitality that it brings "
                        + "to our lives.")
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException ignored) {

        }
    }

    private Pair<Float, Float> tryParseCoordinates(String latitudeString, String longitudeString) {
        float latitude = Float.parseFloat(latitudeString);
        float longitude = Float.parseFloat(longitudeString);
        return new ImmutablePair<>(latitude, longitude);
    }
}
