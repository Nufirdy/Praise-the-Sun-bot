package ent.otego.praise.data.files;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
class LocalFileBotDataRepository implements BotDataRepository {

    private final Path serializedChatsFile;

    private final Map<Long, TelegramChat> chatsCache;

    public LocalFileBotDataRepository(@Value("data.localFile.path") String filePath) {
        serializedChatsFile = Path.of(filePath);
        chatsCache = readSerializedCacheFromFile();
    }

    public Map<Long, TelegramChat> readSerializedCacheFromFile() {
        if (Files.exists(serializedChatsFile)) {
            try (FileInputStream streamIn = new FileInputStream(serializedChatsFile.toFile());
                 ObjectInputStream objectStream = new ObjectInputStream(streamIn)) {
                return (Map<Long, TelegramChat>) objectStream.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                log.info("Не удалось найти файл со списком чатов, создан новый");
                return new ConcurrentHashMap<>();
            }
        }
        return new ConcurrentHashMap<>();
    }

    public synchronized void writeToSerializedCacheFile() throws IOException {
        FileOutputStream streamOut = new FileOutputStream(serializedChatsFile.toFile());
        ObjectOutputStream objectStream = new ObjectOutputStream(streamOut);
        objectStream.writeObject(chatsCache);
    }

    @Override
    public List<TelegramChat> getChatsList() {
        return List.copyOf(chatsCache.values());
    }

    @Override
    public TelegramChat getByChatId(long telegramChatId) {
        return chatsCache.get(telegramChatId);
    }

    @Override
    public void saveOrUpdateChat(TelegramChat chat) {
        TelegramChat savedChat = chatsCache.get(chat.getChatId());
        if (chat.equals(savedChat)) {
            return;
        }
        chatsCache.put(chat.getChatId(), chat);
        try {
            writeToSerializedCacheFile();
        } catch (IOException e) {
            log.error("Error writing chat to file: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(long telegramChatId) {
        return chatsCache.containsKey(telegramChatId);
    }
}
