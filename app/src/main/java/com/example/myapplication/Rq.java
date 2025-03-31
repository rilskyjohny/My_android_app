package com.example.myapplication;

import java.util.ArrayList;

public class Rq {
    private String creds;
    private ArrayList<SubRq> subrqs;
    private final int id;

    public Rq(String creds, ArrayList<SubRq> subrqs, int id) {
        this.creds = creds;
        this.subrqs = subrqs;
        this.id = id;
    }

    public String getCreds() {
        return creds;
    }

    public void setCreds(String creds) {
        this.creds = creds;
    }

    public ArrayList<SubRq> getSubrqs() {
        return subrqs;
    }

    public void setSubrqs(ArrayList<SubRq> subrqs) {
        this.subrqs = subrqs;
    }

    public int getId() {
        return id;
    }

    public static class SubRq{
        private final Rq parentRq;
        private final int id;
        private final int tovarid;
        private int count;

        public SubRq(Rq parentRq, int id, int tovarid, int count) {
            this.parentRq = parentRq;
            this.id = id;
            this.tovarid = tovarid;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Rq getParentRq() {
            return parentRq;
        }

        public int getTovarid() {
            return tovarid;
        }

        public int getId() {
            return id;
        }
    }
}
