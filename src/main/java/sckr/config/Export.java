package sckr.config;

/**
 * Export
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 09:52
 */

public enum Export {

    CSV("csv"),
    JSOM("json"),
    XML("xml");

    private String type;

    Export(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
