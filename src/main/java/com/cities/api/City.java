package com.cities.api;

/**
 * Created by GKlymenko on 3/24/2016.
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class City {
    private long id;

    @Length(max = 20)
    @NotEmpty
    private String name;

    private String longLat = "";
    private String formattedAddress;

    public City() { }

    public City(long id, String name, String longLat, String formattedAddress) {
        this.id = id;
        this.name = name;
        this.longLat = longLat;
        this.formattedAddress = formattedAddress;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getLongLat() {
        return longLat;
    }

    @JsonProperty
    public void setLongLat(String longLat) {
        this.longLat = longLat;
    }

    @JsonProperty
    public String getFormattedAddress() {
        return formattedAddress;
    }

    @JsonProperty
    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                        append(name).
                        append(longLat).
                        append(formattedAddress).
                        toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof City))
            return false;
        if (obj == this)
            return true;

        City ct = (City) obj;
        return new EqualsBuilder().
                append(name, ct.name).
                append(longLat, ct.longLat).
                append(formattedAddress, ct.formattedAddress).
                isEquals();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("longLat", longLat)
                .add("formattedAddress", formattedAddress)
                .toString();
    }
}
