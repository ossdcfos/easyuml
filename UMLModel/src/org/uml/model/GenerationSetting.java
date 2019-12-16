package org.uml.model;

/**
 *
 * @author Philippe-Henri Gosselin
 */
public enum GenerationSetting {
    
    ENABLED {
            @Override
            public String toString() {
                return "enabled";
            }

        },
    DISABLED {
            @Override
            public String toString() {
                return "disabled";
            }

        },
    AUTO {
        @Override
        public String toString() {
            return "auto";
        }

    },
    PROTECTED {
        @Override
        public String toString() {
            return "only protected";
        }

    },
    PRIVATE {
        @Override
        public String toString() {
            return "only private";
        }

    },
    NOTPUBLIC {
        @Override
        public String toString() {
            return "not public";
        }
    };
    
    public static GenerationSetting stringToGenerationSetting(String value) {
        if(value.equalsIgnoreCase(ENABLED.toString())) return ENABLED;
        else if(value.equalsIgnoreCase(DISABLED.toString())) return DISABLED;
        else if(value.equalsIgnoreCase(AUTO.toString())) return AUTO;
        else if(value.equalsIgnoreCase(PRIVATE.toString())) return PRIVATE;
        else if(value.equalsIgnoreCase(PROTECTED.toString())) return PROTECTED;
        else if(value.equalsIgnoreCase(NOTPUBLIC.toString())) return NOTPUBLIC;
        else return DISABLED;
    }    
}
