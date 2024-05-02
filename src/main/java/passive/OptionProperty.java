package passive;

import extension.burp.IOptionProperty;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author isayan
 */
public class OptionProperty implements IOptionProperty {

    private final Map<String, String> config = new HashMap();

    @Override
    public void saveConfigSetting(final Map<String, String> value) {
        this.config.putAll(value);
    }

    @Override
    public Map<String, String> loadConfigSetting() {
        return this.config;
    }

}
