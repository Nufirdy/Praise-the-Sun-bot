package ent.otego.praise.data.files;

import ent.otego.praise.data.BotDataRepository;
import ent.otego.praise.data.TelegramChat;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
class FileBotDataRepository implements BotDataRepository {

    private final List<TelegramChat> chatsCache;

    public FileBotDataRepository() {
        chatsCache = readSerializedListFormFile();
    }

    public List<TelegramChat> readSerializedListFormFile() {
        Path serializedObject = Path.of(System.getProperty("user.dir") + System.getProperty("file.separator") + "chats_list.ser");
        if (Files.exists(serializedObject)) {
            try (FileInputStream streamIn = new FileInputStream(serializedObject.toFile());
                 ObjectInputStream objectStream = new ObjectInputStream(streamIn)) {
                return (List<TelegramChat>) objectStream.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    public void addChatToList() {

    }

    @Override
    public List<TelegramChat> getChatsList() {
        return List.copyOf(chatsCache);
    }
}
