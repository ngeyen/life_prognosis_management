package datacompute.models;

public class Country {
    private String name;
    private String isoCode;
    private String LifeExpectancy;

    public String getIsoCode() {
        return isoCode;
    }

    public String getLifeExpectancy() {
        return LifeExpectancy;
    }

    public String getName() {
        return name;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }

    public void setLifeExpectancy(String lifeExpectancy) {
        LifeExpectancy = lifeExpectancy;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Country Iso: " + getIsoCode() + "\n"
                + "Country Name: " + getName() + "\n"
                + "Country Life Expectancy: " + getLifeExpectancy();

    }
}
