package burp;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.extension.ExtensionUnloadingHandler;
import burp.api.montoya.persistence.Preferences;
import extension.burp.BurpExtensionImpl;
import extension.burp.IBurpTab;
import extension.burp.IPropertyConfig;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.logging.Logger;
import passive.OptionProperty;
import passive.signature.ReDoSScan;
import passive.signature.ReDoSSignature;
import passive.signature.ReDoSDetectorTab;

/**
 *
 * @author isayan
 */
public class BurpExtension extends BurpExtensionImpl implements ExtensionUnloadingHandler {

    private final static Logger logger = Logger.getLogger(BurpExtension.class.getName());

    private final ReDoSDetectorTab tabReDoSDetector = new ReDoSDetectorTab();

    private final ReDoSSignature signature = new ReDoSSignature();

    @Override
    public void initialize(MontoyaApi api) {
        super.initialize(api);
        api().extension().setName("ReDoSDetector");
        IBurpTab tab = this.signature.getBurpTab();
        if (tab != null) {
            api().userInterface().registerSuiteTab(tab.getTabCaption(), tab.getUiComponent());
        }
        api().scanner().registerScanCheck(this.signature.getSignatureScan().passiveScanCheck());
        api.extension().registerUnloadingHandler(this);

        Map<String, String> settings = this.option.loadConfigSetting();
        Preferences pref = api().persistence().preferences();
        String value = pref.getString(ReDoSScan.SIGNATURE_PROPERTY);
        IPropertyConfig config = signature.getSignatureConfig();
        if (config != null) {
            settings.put(ReDoSScan.SIGNATURE_PROPERTY, value == null ? config.defaultSetting() : value);
            String settingValue = settings.getOrDefault(config.getSettingName(), config.defaultSetting());
            config.saveSetting(settingValue);
        }
        this.tabReDoSDetector.addPropertyChangeListener(newPropertyChangeListener());
    }

    @Override
    public void extensionUnloaded() {
        this.applyOptionProperty();
    }

    public PropertyChangeListener newPropertyChangeListener() {
        return new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                IPropertyConfig config = signature.getSignatureConfig();
                if (config != null) {
                    BurpExtension.helpers().outPrintln(config.getSettingName() + "<=>" + evt.getPropertyName());
                    if (config.getSettingName().equals(evt.getPropertyName())) {
                        Map<String, String> settings = option.loadConfigSetting();
                        settings.put(config.getSettingName(), config.loadSetting());
                        applyOptionProperty();
                    }
                }
            }
        };
    }

    private final OptionProperty option = new OptionProperty();

    public OptionProperty getProperty() {
        return this.option;
    }

    private void applyOptionProperty() {
        Map<String, String> settings = option.loadConfigSetting();
        Preferences pref = api().persistence().preferences();
        for (String key : settings.keySet()) {
            pref.setString(key, settings.get(key));
        }
    }

}
