package ent.otego.praise.data.files;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository
class FileBotDataRepository implements BotDataRepository {

    private final static Path SERIALIZED_CHATS_FILE = Path.of(
            System.getProperty("user.dir")
                    + System.getProperty("file.separator")
                    + "chats_list.ser");

    private final Set<TelegramChat> chatsCache;

    public FileBotDataRepository() {
        chatsCache = readSerializedSetFormFile();
    }

    public Set<TelegramChat> readSerializedSetFormFile() {
        if (Files.exists(SERIALIZED_CHATS_FILE)) {
            try (FileInputStream streamIn = new FileInputStream(SERIALIZED_CHATS_FILE.toFile());
                 ObjectInputStream objectStream = new ObjectInputStream(streamIn)) {
                return (Set<TelegramChat>) objectStream.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                log.info("Не удалось найти файл со списком чатов, создан новый");
                return new HashSet<>();
            }
        }
        return new HashSet<>();
    }

    public void writeToSerializedSetFile() throws IOException {
        FileOutputStream streamOut = new FileOutputStream(SERIALIZED_CHATS_FILE.toFile());
        ObjectOutputStream objectStream = new ObjectOutputStream(streamOut);
        objectStream.writeObject(chatsCache);
    }

    @Override
    public List<TelegramChat> getChatsList() {
        return List.copyOf(chatsCache);
    }

    @Override
    public void saveChat(TelegramChat chat) {
        if (chatsCache.add(chat)) {
            try {
                writeToSerializedSetFile();
            } catch (IOException e) {
                log.error("Ошибка записи чата в файл: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
