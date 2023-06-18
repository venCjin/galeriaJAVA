package com.jsuchinski.galeria.model;

public class GalleryTileData {
    int idAlbum;
    int idPhoto;
    String title;
    String author;
    String date;

    public GalleryTileData(int idAlbum, int idPhoto, String title, String author, String date) {
        this.idAlbum = idAlbum;
        this.idPhoto = idPhoto;
        this.title = title;
        this.author = author;
        this.date = date;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }
}
