package com.jsuchinski.galeria.model;

public class FotoTileData {
    int idAlbum;
    int idPhoto;
    String description;
    String author;
    String date;

    public FotoTileData(int idAlbum, int idPhoto, String description, String author, String date) {
        this.idAlbum = idAlbum;
        this.idPhoto = idPhoto;
        this.description = description;
        this.author = author;
        this.date = date;
    }
    public int getIdAlbum() {
        return idAlbum;
    }
    public int getIdPhoto() {
        return idPhoto;
    }

    public String getDescShort() {
        if(description.length() > 110) {
            return description.substring(0,110) + "...";
        }
        return description;
    }

    public String getDescFull() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public boolean hasDesc() {
        return !description.isBlank();
    }
}
