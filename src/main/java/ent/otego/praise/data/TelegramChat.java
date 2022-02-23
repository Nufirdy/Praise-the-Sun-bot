package ent.otego.praise.data;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Builder
public class TelegramChat implements Serializable {

    private static final long serialVersionUID = 15374321L;

    private Long chatId;

    private boolean isPrivate;

    @Setter
    private Float longitude;

    @Setter
    private Float latitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramChat chat = (TelegramChat) o;
        return chatId.equals(chat.chatId) && Objects.equals(longitude, chat.longitude) && Objects.equals(latitude, chat.latitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, longitude, latitude);
    }
}
