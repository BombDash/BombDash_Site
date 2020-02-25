package net.bombdash.core.api.methods.account.serverSetting;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.bombdash.core.api.models.Particle;
import net.bombdash.core.api.models.Prefix;

@Data
@AllArgsConstructor
public class AccountServerSettingRequest {
    private Prefix prefix;
    private Particle particle;

}
