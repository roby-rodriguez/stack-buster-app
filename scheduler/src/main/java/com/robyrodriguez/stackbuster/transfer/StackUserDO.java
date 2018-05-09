package com.robyrodriguez.stackbuster.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StackUserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private int reputation;
    private int user_id;
    private String profile_image;
    private String display_name;
    private String link;

    public StackUserDO() {
    }

    public int getReputation() {
        return this.reputation;
    }

    public void setReputation(final int pReputation) {
        reputation = pReputation;
    }

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final int pUser_id) {
        user_id = pUser_id;
    }

    public String getProfile_image() {
        return this.profile_image;
    }

    public void setProfile_image(final String pProfile_image) {
        profile_image = pProfile_image;
    }

    public String getDisplay_name() {
        return this.display_name;
    }

    public void setDisplay_name(final String pDisplay_name) {
        display_name = pDisplay_name;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(final String pLink) {
        link = pLink;
    }

    @Override
    public String toString() {
        return "StackUserDO{" + "reputation=" + reputation + ", user_id=" + user_id + ", profile_image='"
                + profile_image + '\'' + ", display_name='" + display_name + '\'' + ", link='" + link + '\'' + '}';
    }
}
