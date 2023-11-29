package com.mdolli.biblitera.models;

import com.mdolli.biblitera.database.DBConnection;

import java.sql.*;

public class Reservation {
    private int id;
    private User usuario;
    private Book livro;
    private Date dataEmprestimo;
    private Date dataDevEsperada;
    private Date dataDevEfetiva;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Book getLivro() {
        return livro;
    }

    public void setLivro(Book livro) {
        this.livro = livro;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataDevEsperada() {
        return dataDevEsperada;
    }

    public void setDataDevEsperada(Date dataDevEsperada) {
        this.dataDevEsperada = dataDevEsperada;
    }

    public Date getDataDevEfetiva() {
        return dataDevEfetiva;
    }

    public void setDataDevEfetiva(Date dataDevEfetiva) {
        this.dataDevEfetiva = dataDevEfetiva;
    }

    private void fetchUser(int id) {
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL get_user(?) }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, id);
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            if(result.next()) {
                User user = new User(result.getInt(1), result.getString(2), result.getString(3), result.getString(4), result.getInt(5));
                this.usuario = user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchBook(int id) {
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL get_book(?) }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, id);
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            if(result.next()) {
                Book book = new Book(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), result.getString(5), result.getString(6));
                this.livro = book;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Reservation(int id, int idUsuario, int idLivro, Date dataEmprestimo, Date dataDevEsperada, Date dataDevEfetiva, int status) {
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevEsperada = dataDevEsperada;
        this.dataDevEfetiva = dataDevEfetiva;
        this.status = status;
        fetchBook(idLivro);
        fetchUser(idUsuario);
    }
}
