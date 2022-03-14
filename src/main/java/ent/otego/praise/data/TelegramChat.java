package ent.otego.praise.data;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@EqualsAndHashCode
public class TelegramChat implements Serializable {

    private static final long serialVersionUID = 15374321L;

    private Long chatId;

    private boolean isPrivate;
}
