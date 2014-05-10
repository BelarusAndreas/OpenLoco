package openloco;

import openloco.datfiles.DatFileInputStream;
import openloco.datfiles.MultiLangString;
import openloco.datfiles.Sprites;

import java.io.IOException;

public class Company {

    private String name;
    private final CompanyVars companyVars;
    private final MultiLangString companyName;
    private final MultiLangString ceoName;
    private final Sprites sprites;

    public Company(String name, CompanyVars companyVars, MultiLangString ceoName, MultiLangString companyName, Sprites sprites) {
        this.name = name;
        this.companyVars = companyVars;
        this.companyName = companyName;
        this.sprites = sprites;
        this.ceoName = ceoName;
    }

    public String getName() {
        return name;
    }

    public CompanyVars getCompanyVars() {
        return companyVars;
    }

    public MultiLangString getCompanyName() {
        return companyName;
    }

    public MultiLangString getCeoName() {
        return ceoName;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public static Company load(String name, DatFileInputStream dataInputStream) {
        CompanyVars companyVars = new CompanyVars(dataInputStream);
        MultiLangString ceoName = new MultiLangString(dataInputStream);
        MultiLangString companyName = new MultiLangString(dataInputStream);
        Sprites sprites = new Sprites(dataInputStream);
        return new Company(name, companyVars, ceoName, companyName, sprites);
    }

    public static class CompanyVars {
        private byte intelligence;
        private byte aggressiveness;
        private byte competitiveness;

        public CompanyVars(DatFileInputStream in) {
            try {
                in.skipBytes(12);
                in.skipBytes(2); //spritesets
                in.skipBytes(38);
                intelligence = in.readByte();
                aggressiveness = in.readByte();
                competitiveness = in.readByte();
            }
            catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        }

        public byte getIntelligence() {
            return intelligence;
        }

        public byte getAggressiveness() {
            return aggressiveness;
        }

        public byte getCompetitiveness() {
            return competitiveness;
        }
    }
}
