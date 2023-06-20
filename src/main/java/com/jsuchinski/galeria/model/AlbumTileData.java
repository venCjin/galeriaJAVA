package com.jsuchinski.galeria.model;

public class AlbumTileData {
    int idAlbum;
    int idPhoto;
    String description;
    String author;
    String date;

    public AlbumTileData(int idPhoto, String description, String author, String date) {
        this.idPhoto = idPhoto;
        this.description = description;
        this.author = author;
        this.date = date;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public String getDesc() {
        if(description.length() > 110) {
            return description.substring(0,110) + "...";
        }
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
