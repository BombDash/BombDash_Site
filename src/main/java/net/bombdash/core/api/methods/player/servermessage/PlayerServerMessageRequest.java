package net.bombdash.core.api.methods.player.servermessage;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PlayerServerMessageRequest {
    /**
     * ID Игрока который отправил
     */
    private String id;
    /**
     * Имя сервера
     */
    @SerializedName("server_name")
    private String serverName;
    /**
     * Само сообщение
     */
    private String message;
}
