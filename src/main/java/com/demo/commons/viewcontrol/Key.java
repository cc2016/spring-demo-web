package com.demo.commons.viewcontrol;

/**
 *
 */
public class Key {
    private String ip;

    public Key(String ip) {
        this.ip = ip;
    }

    public int hashCode() {
        return ip.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof Key) {
            return ip.equals(((Key) o).ip);
        }

        return false;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
