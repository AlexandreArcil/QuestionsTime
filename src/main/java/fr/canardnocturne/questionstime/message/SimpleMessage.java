package fr.canardnocturne.questionstime.message;

import org.apache.commons.lang3.StringUtils;

public class SimpleMessage {

    protected String message;
    protected final String section;

    public SimpleMessage(final String section, final String message) {
        this.message = message;
        this.section = section;
    }

    public void setMessage(final String message) throws IllegalArgumentException {
        if (StringUtils.isNotEmpty(message)) {
            this.message = message;
        } else {
            throw new IllegalArgumentException("The message at the section '" + this.section + "' is empty. The default value will be used.");
        }
    }

    public String getMessage() {
        return message;
    }

    public String getSection() {
        return section;
    }
}
