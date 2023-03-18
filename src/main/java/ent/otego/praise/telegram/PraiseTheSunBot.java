package ent.otego.praise.telegram;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import ent.otego.praise.schedule.PraiseScheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class PraiseTheSunBot extends TelegramLongPollingBot {

    private final Set<Long> awaitReply = ConcurrentHashMap.newKeySet();

    private final String username;
    private final long botId;

    private final BotDataRepository botDataRepository;
    private final PraiseScheduler praiseScheduler;

    public PraiseTheSunBot(String token, String username,
                           BotDataRepository botDataRepository,
                           PraiseScheduler praiseScheduler) throws TelegramApiException {
        super(token);
        this.username = username;
        this.botDataRepository = botDataRepository;
        this.praiseScheduler = praiseScheduler;

        botId = getMe().getId();
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText() && message.getText().startsWith("/")) {
                String messageText = message.getText();
                PraiseSunBotCommand command = getBotCommand(messageText);
                switch (command) {
                    case START:
                    case LOCATION:
                        executeLocationCommand(message);
                        break;
                    case STOP:
                        executeStopCommand(message);
                        break;
                }
                return;
            }

            if (awaitReply.contains(message.getChatId())
                    && (message.hasLocation() || message.hasText())) {
                awaitCoordinatesReply(message);
            }
        }

        if (update.hasMyChatMember()) {
            ChatMemberUpdated myChatMemberUpdated = update.getMyChatMember();
            ChatMember myNewChatMember = myChatMemberUpdated.getNewChatMember();
            if (myNewChatMember.getUser().getId() == botId
                    && (myNewChatMember.getStatus().equals("kicked")
                    || myNewChatMember.getStatus().equals("left"))) {
                TelegramChat chat =
                        botDataRepository.getByChatId(myChatMemberUpdated.getChat().getId());
                if (chat != null) {
                    praiseScheduler.cancelScheduledPraiseTask(chat);
                }
            }
        }
    }

    private PraiseSunBotCommand getBotCommand(String messageText) {
        String[] textChunks = messageText.split(" ");
        String commandLiteral = textChunks[0];
        commandLiteral = commandLiteral.substring(1); //strip / at begging
        return PraiseSunBotCommand.from(commandLiteral);
    }

    private void executeLocationCommand(Message message) {
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
                                + "thy location, so that I shall know when sun is highest at "
                                + "your land.")
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
                        + "at this moment? And I shall know when sun is highest at your "
                        + "land.")
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        awaitReply.add(message.getChatId());
    }

    private void executeStopCommand(Message message) {
        TelegramChat chat = botDataRepository.getByChatId(message.getChatId());
        praiseScheduler.cancelScheduledPraiseTask(chat);

        SendMessage sendMessage = SendMessage.builder()
                .chatId(message.getChatId())
                .text("Ah, I see. Thou art determined to tread thy own path, and I respect thy "
                        + "choice. Know that I shall always be here for thee, shouldst thou ever "
                        + "require my aid. For now, I shall continue on my own quest, seeking the "
                        + "light of the sun and spreading its warmth wherever I go. May our "
                        + "paths cross again someday, my friend. Until then, stay strong and take "
                        + "care.")
                .build();
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void awaitCoordinatesReply(Message message) {
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
                                + "thy location, so that I shall know when sun is highest at "
                                + "your land.")
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

    private Pair<Float, Float> tryParseCoordinates(String latitudeString, String longitudeString) {
        float latitude = Float.parseFloat(latitudeString);
        float longitude = Float.parseFloat(longitudeString);
        return new ImmutablePair<>(latitude, longitude);
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
}
