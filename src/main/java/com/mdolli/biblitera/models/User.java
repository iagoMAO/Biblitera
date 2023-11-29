package com.mdolli.biblitera.models;

public class User {
    private int id;
    private String name;
    private String email;
    private String cpf;
    private int cargo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public User(int id, String name, String email, String cpf, int cargo) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.cargo = cargo;
    }
}
