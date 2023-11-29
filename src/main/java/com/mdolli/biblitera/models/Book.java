package com.mdolli.biblitera.models;

public class Book {
    private int id;
    private String isbn;
    private String name;
    private int copies;
    private String author;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public Book(int id, String isbn, String name, int copies, String author, String description) {
        this.id = id;
        this.isbn = isbn;
        this.copies = copies;
        this.name = name;
        this.author = author;
        this.description = description;
    }
}
